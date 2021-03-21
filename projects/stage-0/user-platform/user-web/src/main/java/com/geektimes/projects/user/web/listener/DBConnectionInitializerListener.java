package com.geektimes.projects.user.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 数据库连接初始化监听器
 */
@WebListener
public class DBConnectionInitializerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("启动");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
