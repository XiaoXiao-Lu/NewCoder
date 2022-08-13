package com.kinnon.controller.advice;

import com.kinnon.util.NewCoderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.Http2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Kinnon
 * @create 2022-08-09 16:51
 */
@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        log.error("服务器发生异常", e.getMessage());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            log.error(stackTraceElement.toString());
        }

        String xRequestWith = request.getHeader("x-requested-with");
        if ( xRequestWith.equals("XMLHttpRequest")) {
           response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(NewCoderUtil.getJSONString(1, "服务器发生异常"));
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }

    }

}
