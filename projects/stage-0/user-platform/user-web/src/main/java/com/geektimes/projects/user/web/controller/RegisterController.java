package com.geektimes.projects.user.web.controller;

import com.geektimes.projects.user.context.ComponentContext;
import com.geektimes.projects.user.domain.User;
import com.geektimes.projects.user.repository.DatabaseUserRepository;
import com.geektimes.projects.user.repository.UserRepository;
import com.geektimes.projects.user.service.UserService;
import com.geektimes.projects.user.service.impl.UserServiceImpl;
import com.geektimes.projects.user.sql.DBConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geektimes.web.mvc.controller.PageController;
import org.hibernate.dialect.Database;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.IOException;

/**
 * 用户注册
 */
@Path("/register")
public class RegisterController implements PageController {

    private static final Log log = LogFactory.getLog(RegisterController.class);

    UserService userService;

    public RegisterController(){
        userService= ComponentContext.getInstance().getComponent("bean/UserService");
    }

    @GET
    @POST
    @Path("")
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        String method = request.getMethod();
        if ("GET".equals(method)) {
            return "register-form.jsp";
        }
        User user = getUser(request);
//        UserService userService = (UserService)ComponentContext.getInstance().getComponent("bean/UserService");

//        DBConnectionManager dbConnectionManager=new DBConnectionManager();
//        dbConnectionManager.init();

//        DBConnectionManager dbConnectionManager = (DBConnectionManager) request.getServletContext().getAttribute("dbConnectionManager");

//        UserRepository userRepository = new DatabaseUserRepository(dbConnectionManager);
//        UserRepository userRepository=new DatabaseUserRepository();
//        UserService userService = new UserServiceImpl(userRepository);


        try {
            userService.register(user);
        } catch (Exception ex) {
            request.setAttribute("errors", ex.getMessage());
            request.getRequestDispatcher("register-failed.jsp").forward(request, response);
        }

        return "register-success.jsp";
    }

    /**
     * 获取注册用户信息
     *
     * @param request
     * @return
     */
    private User getUser(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        return user;
    }
}
