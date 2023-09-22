package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdaptorFinder;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappings;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMapping handlerMapping;
    private HandlerAdaptorFinder handlerAdaptorFinder;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initHandlerMapping();
        initHandlerAdaptorFinder();
    }

    private void initHandlerMapping() {
        handlerMapping = new HandlerMappings(
            List.of(new AnnotationHandlerMapping(Application.class.getPackageName() + ".*"),
                new ManualHandlerMapping()));
        handlerMapping.initialize();
    }

    private void initHandlerAdaptorFinder() {
        handlerAdaptorFinder = new HandlerAdaptorFinder(List.of(new AnnotationHandlerAdapter(), new ManualHandlerAdapter()));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMapping.getHandler(request);
            final HandlerAdapter handlerAdapter = handlerAdaptorFinder.find(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (HandlerNotFoundException e) {
            renderNotFoundPage(response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String viewName = modelAndView.getView().getName();
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }

    private void renderNotFoundPage(final HttpServletResponse response) {
        try {
            final String notFoundViewName = "/404.jsp";
            response.sendRedirect(notFoundViewName);
        } catch (IOException e) {
            throw new RuntimeException("404페이지 리다이렉션을 실패했습니다.");
        }
    }
}
