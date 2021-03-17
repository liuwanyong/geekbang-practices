package org.geektimes.web.mvc.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PageController extends Controller{
    String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable;
}


