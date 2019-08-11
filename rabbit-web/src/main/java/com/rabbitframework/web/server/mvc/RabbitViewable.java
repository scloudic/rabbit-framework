package com.rabbitframework.web.server.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.glassfish.jersey.server.mvc.Viewable;

public class RabbitViewable extends Viewable {
	private static final String CONTEXT_PATH = "cpath";

	/**
	 * Construct a new viewable type with a template name.
	 * <p/>
	 * The model will be set to {@code null}.
	 *
	 * @param templateName
	 *            the template name, shall not be {@code null}.
	 * @throws IllegalArgumentException
	 *             if the template name is {@code null}.
	 */
	public RabbitViewable(String templateName, HttpServletRequest request) throws IllegalArgumentException {
		this(templateName, new HashMap<String, Object>(), request);
	}

	/**
	 * Construct a new viewable type with a template name and a model.
	 *
	 * @param templateName
	 *            the template name, shall not be {@code null}.
	 * @param model
	 *            the model, may be {@code null}.
	 * @throws IllegalArgumentException
	 *             if the template name is {@code null}.
	 */
	public RabbitViewable(String templateName, Map<String, Object> model, HttpServletRequest request)
			throws IllegalArgumentException {
		super(templateName, model);
		if (getModel() != null) {
			model.put(CONTEXT_PATH, request.getContextPath());
		}
	}
}
