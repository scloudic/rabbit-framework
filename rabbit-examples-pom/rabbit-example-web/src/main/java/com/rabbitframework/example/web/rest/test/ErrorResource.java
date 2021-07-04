package com.rabbitframework.example.web.rest.test;

import com.rabbitframework.core.utils.CommonResponseUrl;
import com.rabbitframework.core.utils.StatusCode;
import com.rabbitframework.example.web.rest.ExmAbstractContextResource;
import org.glassfish.jersey.server.mvc.Viewable;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Component
@Singleton
@Path("/")
public class ErrorResource extends ExmAbstractContextResource {
    @GET
    @Path("404")
    public Object to404() {
        return null;
    }
}
