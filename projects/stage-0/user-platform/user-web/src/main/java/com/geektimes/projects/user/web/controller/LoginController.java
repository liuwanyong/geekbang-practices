package com.geektimes.projects.user.web.controller;

import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/login")
public class LoginController implements PageController {

    @Path("")
    @GET
    @POST
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        String method = request.getMethod();
//        if ("GET".equals(method)) {
//        }
        return "/login-form.jsp";
    }

}
