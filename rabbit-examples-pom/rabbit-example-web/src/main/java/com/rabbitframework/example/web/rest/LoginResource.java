package com.rabbitframework.example.web.rest;

import com.rabbitframework.security.LoginFailException;
import com.rabbitframework.security.SecurityUtils;
import com.rabbitframework.web.AbstractContextResource;
import com.rabbitframework.web.annotations.FormValid;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/")
@Singleton
public class LoginResource extends AbstractContextResource {
    @POST
    @Path("login")
    @FormValid
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Object login(@NotBlank @FormParam("loginName") String loginName,
                        @NotBlank @FormParam("password") String password) {
        boolean isLogin = SecurityUtils.userLogin(loginName, password);
        if (!isLogin) {
            throw new LoginFailException("login.error");
        }
        String userId = SecurityUtils.getUserId();
        System.out.println(userId);
        return getSimpleResponse(true);
    }
}
