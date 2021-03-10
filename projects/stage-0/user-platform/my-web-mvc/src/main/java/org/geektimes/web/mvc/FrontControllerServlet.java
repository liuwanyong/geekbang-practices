package org.geektimes.web.mvc;

import org.apache.commons.lang.StringUtils;
import org.geektimes.web.mvc.controller.Controller;
import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;

import javax.imageio.IIOException;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandleInfo;
import java.lang.reflect.Method;
import java.util.*;

import static org.apache.commons.lang.StringUtils.substringAfter;

public class FrontControllerServlet extends HttpServlet {

    private Map<String, Controller> controllersMapping = new HashMap<>();

    private Map<String, HandlerMethodInfo> handleMethodInfoMapping = new HashMap<>();

    public void init(ServletConfig servletConfig) {
        initHandleMethods();
    }

    private void initHandleMethods() {
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String requestPath = pathFromClass.value();
            Method[] publicMethods = controllerClass.getMethods();

            for (Method method : publicMethods) {
                Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                Path pathFromMethod = method.getAnnotation(Path.class);
                if (pathFromMethod != null) {
                    requestPath += pathFromMethod.value();
                }
                handleMethodInfoMapping.put(requestPath, new HandlerMethodInfo(requestPath, method, supportedHttpMethods));
            }
            controllersMapping.put(requestPath, controller);

        }
    }

    private Set<String> findSupportedHttpMethods(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            HttpMethod httpMethod = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }
        if (supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(Arrays.asList(HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }
        return supportedHttpMethods;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        String servletContextPath = req.getContextPath();

        String prefixPath = servletContextPath;

        String requestMappingPath = substringAfter(requestURI, StringUtils.replace(prefixPath, "//", "/"));

        Controller controller = controllersMapping.get(requestMappingPath);
        if (controller != null) {
            HandlerMethodInfo handlerMethodInfo = handleMethodInfoMapping.get(requestMappingPath);
            try {
                if (handlerMethodInfo != null) {
                    String httpMethod = req.getMethod();
                    if (!handlerMethodInfo.getSupportedHttpMethods().contains(httpMethod)) {
                        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        return;
                    }
                }
                if (controller instanceof PageController) {
                    PageController pageController = PageController.class.cast(controller);
                    String viewPath = pageController.execute(req, res);
                    ServletContext servletContext = req.getServletContext();
                    if (!viewPath.startsWith("/")) {
                        viewPath = "/" + viewPath;
                    }
                    RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                    requestDispatcher.forward(req, res);
                    return;
                } else if (controller instanceof RestController) {

                }
            } catch (Throwable throwable) {
                if (throwable.getCause() instanceof IOException) {
                    throw (IOException) throwable.getCause();
                } else {
                    throw new ServletException(throwable.getCause());
                }
            }
        }
    }
}
