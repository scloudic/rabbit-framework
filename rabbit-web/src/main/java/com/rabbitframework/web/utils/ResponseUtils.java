package com.rabbitframework.web.utils;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class ResponseUtils {
	public static final int STATUS = 0;

	public static Response ok(Object entity) {
		return Response.ok(entity).build();
	}

	public static Response ok() {
		return Response.ok().build();
	}

	public static Response ok(Object entity,MediaType type) {
		return Response.ok(entity, type).build();
	}
	
	/**
	 * 服务器内部500异常
	 * @return
	 */
	public static Response serverError() {
		return Response.serverError().build();
	}
	
	public static Response getResponse(int status) {
		return getResponse(status,null,null);
	}
	
	public static Response getResponse(int status,Object data) {
		return getResponse(status,data,null);
	}
	
	public static Response getResponse(int status, Object data, URI location) {
		ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_OK);
		if (status != STATUS) {
			responseBuilder.status(status);
		}
		if (data != null) {
			responseBuilder.entity(data);
		}
		if (location != null) {
			responseBuilder.location(location);
		}
		return responseBuilder.build();
	}

}
