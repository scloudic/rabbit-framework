package com.rabbitframework.example.web.rest.test;

import com.rabbitframework.example.web.biz.TestBiz;
import com.rabbitframework.example.web.rest.ExmAbstractContextResource;
import com.rabbitframework.web.annotations.FormValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;

@Component
@Singleton
@Path("/test")
public class TestResource extends ExmAbstractContextResource {
	private static final Logger logger = LoggerFactory.getLogger(TestResource.class);
	@Autowired
	private TestBiz testBiz;

	@GET
	@Path("getData")
	public Object getData() {
		logger.debug("getData");
		return getSimpleResponse(true);
	}

	@GET
	@Path("getParams")
	@FormValid
	public Object getParams(@NotBlank(message = "{name.null}") @QueryParam("name") String name) {
		String value = testBiz.test(name);
		return getSimpleResponse(true, value);
	}

	@POST
	@Path("postParams")
	public Object postParams(@FormParam("name") String name) {
		return getSimpleResponse(true, name);
	}
}