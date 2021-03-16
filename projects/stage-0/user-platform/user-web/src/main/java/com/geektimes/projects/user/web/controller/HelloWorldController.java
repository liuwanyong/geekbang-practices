package com.geektimes.projects.user.web.controller;

import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
public class HelloWorldController implements PageController {

    @GET
    @Path("/world")
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "world.jsp";
    }
}
