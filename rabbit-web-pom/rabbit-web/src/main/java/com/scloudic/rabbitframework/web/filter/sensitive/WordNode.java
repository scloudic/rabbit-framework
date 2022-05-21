package com.scloudic.rabbitframework.web.filter.sensitive;

import java.util.LinkedList;
import java.util.List;

public class WordNode {

    private int value; // 节点名称

    private List<WordNode> subNodes; // 子节点

    private boolean isLast;// 默认false

    public WordNode(int value) {
        this.value = value;
    }

    public WordNode(int value, boolean isLast) {
        this.value = value;
        this.isLast = isLast;
    }


    private WordNode addSubNode(final WordNode subNode) {
        if (subNodes == null)
            subNodes = new LinkedList<WordNode>();
        subNodes.add(subNode);
        return subNode;
    }

    //有就直接返回该子节点， 没有就创建添加并返回该子节点
    public WordNode addIfNoExist(final int value, final boolean isLast) {
        if (subNodes == null) {
            return addSubNode(new WordNode(value, isLast));
        }
        for (WordNode subNode : subNodes) {
            if (subNode.value == value) {
                if (!subNode.isLast && isLast)
                    subNode.isLast = true;
                return subNode;
            }
        }
        return addSubNode(new WordNode(value, isLast));
    }

    public WordNode querySub(final int value) {
        if (subNodes == null) {
            return null;
        }
        for (WordNode subNode : subNodes) {
            if (subNode.value == value)
                return subNode;
        }
        return null;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

    @Override
    public int hashCode() {
        return value;
    }

}