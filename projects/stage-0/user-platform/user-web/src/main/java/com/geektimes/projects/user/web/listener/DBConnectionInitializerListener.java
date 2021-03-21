package com.geektimes.projects.user.web.listener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接初始化监听器
 */
@WebListener
public class DBConnectionInitializerListener implements ServletContextListener {

    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        System.out.println("启动");
        Connection connection = getConnection();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    protected Connection getConnection() {
        Context context = null;
        Connection connection = null;
        try {
            context = new InitialContext();
            Context envCtx = (Context) context.lookup("java:comp/env");
            DataSource dataSource = (DataSource) envCtx.lookup("jdbc/UserPlatformDB");
            connection = dataSource.getConnection();
        } catch (NamingException | SQLException e) {
            servletContext.log(e.getMessage(), e);
        }
        if(connection!=null){
            servletContext.log("获取JDNI数据库连接成功");
        }
        return connection;
    }
}
