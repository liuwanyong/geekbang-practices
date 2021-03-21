package com.geektimes.projects.user.context;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.sql.Connection;
import java.util.NoSuchElementException;

/**
 * 组件上下文（Web 应用全局使用）
 */
public class ComponentContext {

    public static final String CONTEXT_NAME = ComponentContext.class.getName();

    private Context context = null;

    private static ServletContext servletContext;

    public static ComponentContext getInstance() {
        return (ComponentContext) servletContext.getAttribute(CONTEXT_NAME);
    }


    public void init(ServletContext servletContext) {
        Connection connection = null;
        try {
            this.context = (Context) new InitialContext().lookup("java:comp/env");
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
        servletContext.setAttribute(CONTEXT_NAME, this);
        ComponentContext.servletContext = servletContext;
    }

    /**
     * 通过名称
     *
     * @param name
     * @param <C>
     * @return
     */
    public <C> C getComponent(String name) {
        C component = null;
        try {
            component = (C) context.lookup(name);
        } catch (NamingException e) {
            throw new NoSuchElementException(name);
        }
        return component;
    }

    public void destroy() {
        if (this.context != null) {
            try {
                this.context.close();
            } catch (NamingException e) {
                throw new NoSuchElementException(e.getMessage());
            }
        }
    }
}
