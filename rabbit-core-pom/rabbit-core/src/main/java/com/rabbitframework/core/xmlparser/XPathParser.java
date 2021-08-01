package com.rabbitframework.core.xmlparser;
import com.rabbitframework.core.exceptions.BuilderException;
import com.rabbitframework.core.propertytoken.PropertyParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class XPathParser {
	private Document document;
	private boolean validation;
	private EntityResolver entityResolver;
	private Properties variables;
	private XPath xpath;

	public XPathParser(String xml, ErrorHandler errorHandler) {
		commonConstructor(false, null, null);
		this.document = createDocument(new InputSource(new StringReader(xml)),
				errorHandler);
	}

	public XPathParser(Reader reader, ErrorHandler errorHandler) {
		commonConstructor(false, null, null);
		this.document = createDocument(new InputSource(reader), errorHandler);
	}

	public XPathParser(InputStream inputStream, ErrorHandler errorHandler) {
		commonConstructor(false, null, null);
		this.document = createDocument(new InputSource(inputStream),
				errorHandler);
	}

	public XPathParser(Document document) {
		commonConstructor(false, null, null);
		this.document = document;
	}

	public XPathParser(String xml, boolean validation, ErrorHandler errorHandler) {
		commonConstructor(validation, null, null);
		this.document = createDocument(new InputSource(new StringReader(xml)),
				errorHandler);
	}

	public XPathParser(Reader reader, boolean validation,
			ErrorHandler errorHandler) {
		commonConstructor(validation, null, null);
		this.document = createDocument(new InputSource(reader), errorHandler);
	}

	public XPathParser(InputStream inputStream, boolean validation,
			ErrorHandler errorHandler) {
		commonConstructor(validation, null, null);
		this.document = createDocument(new InputSource(inputStream),
				errorHandler);
	}

	public XPathParser(Document document, boolean validation) {
		commonConstructor(validation, null, null);
		this.document = document;
	}

	public XPathParser(String xml, boolean validation, Properties variables,
			ErrorHandler errorHandler) {
		commonConstructor(validation, variables, null);
		this.document = createDocument(new InputSource(new StringReader(xml)),
				errorHandler);
	}

	public XPathParser(Reader reader, boolean validation, Properties variables,
			ErrorHandler errorHandler) {
		commonConstructor(validation, variables, null);
		this.document = createDocument(new InputSource(reader), errorHandler);
	}

	public XPathParser(InputStream inputStream, boolean validation,
			Properties variables, ErrorHandler errorHandler) {
		commonConstructor(validation, variables, null);
		this.document = createDocument(new InputSource(inputStream),
				errorHandler);
	}

	public XPathParser(Document document, boolean validation,
			Properties variables) {
		commonConstructor(validation, variables, null);
		this.document = document;
	}

	public XPathParser(String xml, boolean validation, Properties variables,
			EntityResolver entityResolver, ErrorHandler errorHandler) {
		commonConstructor(validation, variables, entityResolver);
		this.document = createDocument(new InputSource(new StringReader(xml)),
				errorHandler);
	}

	public XPathParser(Reader reader, boolean validation, Properties variables,
			EntityResolver entityResolver, ErrorHandler errorHandler) {
		commonConstructor(validation, variables, entityResolver);
		this.document = createDocument(new InputSource(reader), errorHandler);
	}

	public XPathParser(InputStream inputStream, boolean validation,
			Properties variables, EntityResolver entityResolver,
			ErrorHandler errorHandler) {
		commonConstructor(validation, variables, entityResolver);
		this.document = createDocument(new InputSource(inputStream),
				errorHandler);
	}

	public XPathParser(Document document, boolean validation,
			Properties variables, EntityResolver entityResolver) {
		commonConstructor(validation, variables, entityResolver);
		this.document = document;
	}

	public void setVariables(Properties variables) {
		this.variables = variables;
	}

	public String evalString(String expression) {
		return evalString(document, expression);
	}

	public String evalString(Object root, String expression) {
		String result = (String) evaluate(expression, root,
				XPathConstants.STRING);
		result = PropertyParser.parseDollar(result, variables);
		return result;
	}

	public Boolean evalBoolean(String expression) {
		return evalBoolean(document, expression);
	}

	public Boolean evalBoolean(Object root, String expression) {
		return (Boolean) evaluate(expression, root, XPathConstants.BOOLEAN);
	}

	public Short evalShort(String expression) {
		return evalShort(document, expression);
	}

	public Short evalShort(Object root, String expression) {
		return Short.valueOf(evalString(root, expression));
	}

	public Integer evalInteger(String expression) {
		return evalInteger(document, expression);
	}

	public Integer evalInteger(Object root, String expression) {
		return Integer.valueOf(evalString(root, expression));
	}

	public Long evalLong(String expression) {
		return evalLong(document, expression);
	}

	public Long evalLong(Object root, String expression) {
		return Long.valueOf(evalString(root, expression));
	}

	public Float evalFloat(String expression) {
		return evalFloat(document, expression);
	}

	public Float evalFloat(Object root, String expression) {
		return Float.valueOf(evalString(root, expression));
	}

	public Double evalDouble(String expression) {
		return evalDouble(document, expression);
	}

	public Double evalDouble(Object root, String expression) {
		return (Double) evaluate(expression, root, XPathConstants.NUMBER);
	}

	public List<XNode> evalNodes(String expression) {
		return evalNodes(document, expression);
	}

	public List<XNode> evalNodes(Object root, String expression) {
		List<XNode> xnodes = new ArrayList<XNode>();
		NodeList nodes = (NodeList) evaluate(expression, root,
				XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			xnodes.add(new XNode(this, nodes.item(i), variables));
		}
		return xnodes;
	}

	public XNode evalNode(String expression) {
		return evalNode(document, expression);
	}

	public XNode evalNode(Object root, String expression) {
		Node node = (Node) evaluate(expression, root, XPathConstants.NODE);
		if (node == null) {
			return null;
		}
		return new XNode(this, node, variables);
	}

	private Object evaluate(String expression, Object root, QName returnType) {
		try {
			return xpath.evaluate(expression, root, returnType);
		} catch (Exception e) {
			throw new BuilderException("Error evaluating XPath.  Cause: " + e,
					e);
		}
	}

	private Document createDocument(InputSource inputSource,
			ErrorHandler errorHandler) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(validation);

			factory.setNamespaceAware(false);
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(false);
			factory.setCoalescing(false);
			factory.setExpandEntityReferences(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(entityResolver);
			if (errorHandler == null) {
				builder.setErrorHandler(new DefaultErrorhandler());
			} else {
				builder.setErrorHandler(errorHandler);
			}

			return builder.parse(inputSource);
		} catch (Exception e) {
			throw new BuilderException(
					"Error creating document instance.  Cause: " + e, e);
		}
	}

	private void commonConstructor(boolean validation, Properties variables,
			EntityResolver entityResolver) {
		this.validation = validation;
		this.entityResolver = entityResolver;
		this.variables = variables;
		XPathFactory factory = XPathFactory.newInstance();
		this.xpath = factory.newXPath();
	}

}
