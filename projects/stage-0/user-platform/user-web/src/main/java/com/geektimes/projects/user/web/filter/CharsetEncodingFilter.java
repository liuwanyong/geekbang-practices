package com.geektimes.projects.user.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CharsetEncodingFilter implements Filter {

    private String encoding=null;
    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding=filterConfig.getInitParameter("encoding");
        this.servletContext=filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest){
            HttpServletRequest httpServletRequest=(HttpServletRequest) request;
            HttpServletResponse httpServletResponse=(HttpServletResponse) response;
            httpServletRequest.setCharacterEncoding(this.encoding);
            httpServletResponse.setCharacterEncoding(this.encoding);
            this.servletContext.log("当前编码已设置为：" + this.encoding);
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
