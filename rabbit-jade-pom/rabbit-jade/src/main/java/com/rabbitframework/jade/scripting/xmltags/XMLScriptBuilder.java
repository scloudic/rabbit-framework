package com.rabbitframework.jade.scripting.xmltags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rabbitframework.jade.builder.BaseBuilder;
import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.exceptions.BuilderException;
import com.rabbitframework.jade.scripting.DynamicSqlSource;
import com.rabbitframework.jade.scripting.SqlSource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tjzq.commons.xmlparser.XNode;
import com.tjzq.commons.xmlparser.XPathParser;

/**
 * SQL脚本动态生成
 * 生成{@link SqlSource}
 * 实现{@link DynamicSqlSource}
 */
public class XMLScriptBuilder extends BaseBuilder {
    private XNode rootScriptNode;

    public XMLScriptBuilder(Configuration configuration, String sqlScript) {
        super(configuration);
        XPathParser xPathParser = new XPathParser(sqlScript, null);
        rootScriptNode = xPathParser.evalNode("/script");
    }

    public SqlSource parse() {
        List<SqlNode> sqlNodes = parseDynamicTags(rootScriptNode);
        MixedSqlNode rootSqlNode = new MixedSqlNode(sqlNodes);
        SqlSource sqlSource = new DynamicSqlSource(configuration, rootSqlNode);
        return sqlSource;
    }

    private List<SqlNode> parseDynamicTags(XNode node) {
        List<SqlNode> contents = new ArrayList<SqlNode>();
        NodeList children = node.getNode().getChildNodes();  //直接通过w3c包获取NodeList
        int childrenLength = children.getLength();
        for (int i = 0; i < childrenLength; i++) {
            XNode child = node.newXNode(children.item(i));
            Node childNode = child.getNode();
            short nodeType = childNode.getNodeType();
            // <![CDATA[]]>中括着的纯文本，它没有子节点
            if (nodeType == Node.CDATA_SECTION_NODE
                    || nodeType == Node.TEXT_NODE) {
                String data = child.getStringBody("");
                contents.add(new TextSqlNode(data));
            } else if (nodeType == Node.ELEMENT_NODE) {
                String nodeName = childNode.getNodeName();
                NodeHandler handler = nodeHandlers.get(nodeName);
                if (handler == null) {
                    throw new BuilderException("Unknown element <" + nodeName + "> in SQL statement.");
                }
                handler.handleNode(child, contents);
            }
        }
        return contents;
    }

    private Map<String, NodeHandler> nodeHandlers = new HashMap<String, NodeHandler>() {
        {
            put("trim", new TrimHandler());
            put("where", new WhereHandler());
            put("foreach", new ForEachHandler());
            put("if", new IfHandler());
            put("choose", new ChooseHandler());
            put("when", new IfHandler());
            put("otherwise", new OtherwiseHandler());
        }
    };

    private interface NodeHandler {
        void handleNode(XNode nodeToHandler, List<SqlNode> targetContents);
    }

    /**
     * trim节点处理
     */
    private class TrimHandler implements NodeHandler {
        @Override
        public void handleNode(XNode nodeToHandler, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandler);
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
            String prefix = nodeToHandler.getStringAttribute("prefix");
            String prefixOverrides = nodeToHandler.getStringAttribute("prefixOverrides");
            String suffix = nodeToHandler.getStringAttribute("suffix");
            String suffixOverrides = nodeToHandler.getStringAttribute("suffixOverrides");
            TrimSqlNode trimSqlNode = new TrimSqlNode(configuration, mixedSqlNode, prefix, prefixOverrides, suffix, suffixOverrides);
            targetContents.add(trimSqlNode);
        }
    }

    private class WhereHandler implements NodeHandler {
        @Override
        public void handleNode(XNode nodeToHandler, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandler);
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
            WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, mixedSqlNode);
            targetContents.add(whereSqlNode);
        }
    }

    private class ForEachHandler implements NodeHandler {

        @Override
        public void handleNode(XNode nodeToHandler, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandler);
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
            String collection = nodeToHandler.getStringAttribute("collection");
            String item = nodeToHandler.getStringAttribute("item");
            String index = nodeToHandler.getStringAttribute("index");
            String open = nodeToHandler.getStringAttribute("open");
            String close = nodeToHandler.getStringAttribute("close");
            String separator = nodeToHandler.getStringAttribute("separator");
            ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, mixedSqlNode,
                    collection, index, item, open, close, separator);
            targetContents.add(forEachSqlNode);
        }
    }

    private class IfHandler implements NodeHandler {

        @Override
        public void handleNode(XNode nodeToHandler, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandler);
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
            String test = nodeToHandler.getStringAttribute("test");
            IfSqlNode ifSqlNode = new IfSqlNode(mixedSqlNode, test);
            targetContents.add(ifSqlNode);
        }
    }

    private class OtherwiseHandler implements NodeHandler {

        @Override
        public void handleNode(XNode nodeToHandler, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandler);
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
            targetContents.add(mixedSqlNode);
        }
    }

    private class ChooseHandler implements NodeHandler {
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            List<SqlNode> whenSqlNodes = new ArrayList<SqlNode>();
            List<SqlNode> otherwiseSqlNodes = new ArrayList<SqlNode>();
            handleWhenOtherwiseNodes(nodeToHandle, whenSqlNodes,
                    otherwiseSqlNodes);
            SqlNode defaultSqlNode = getDefaultSqlNode(otherwiseSqlNodes);
            ChooseSqlNode chooseSqlNode = new ChooseSqlNode(whenSqlNodes,
                    defaultSqlNode);
            targetContents.add(chooseSqlNode);
        }

        private void handleWhenOtherwiseNodes(XNode chooseSqlNode,
                                              List<SqlNode> ifSqlNodes, List<SqlNode> defaultSqlNodes) {
            List<XNode> children = chooseSqlNode.getChildren();
            for (XNode child : children) {
                String nodeName = child.getNode().getNodeName();
                NodeHandler handler = nodeHandlers.get(nodeName);
                if (handler instanceof IfHandler) {
                    handler.handleNode(child, ifSqlNodes);
                } else if (handler instanceof OtherwiseHandler) {
                    handler.handleNode(child, defaultSqlNodes);
                }
            }
        }

        private SqlNode getDefaultSqlNode(List<SqlNode> defaultSqlNodes) {
            SqlNode defaultSqlNode = null;
            if (defaultSqlNodes.size() == 1) {
                defaultSqlNode = defaultSqlNodes.get(0);
            } else if (defaultSqlNodes.size() > 1) {
                throw new BuilderException(
                        "Too many default (otherwise) elements in choose statement.");
            }
            return defaultSqlNode;
        }
    }


}
