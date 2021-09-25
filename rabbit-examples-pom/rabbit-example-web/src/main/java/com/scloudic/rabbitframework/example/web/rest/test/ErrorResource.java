package com.scloudic.rabbitframework.example.web.rest.test;

import com.scloudic.rabbitframework.web.AbstractContextResource;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Component
@Singleton
@Path("/")
public class ErrorResource extends AbstractContextResource {
    @GET
    @Path("404")
    public Object to404() {
        return null;
    }
}
