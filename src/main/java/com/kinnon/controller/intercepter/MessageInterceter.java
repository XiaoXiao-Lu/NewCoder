package com.kinnon.controller.intercepter;

import com.kinnon.domain.User;
import com.kinnon.service.MessageService;
import com.kinnon.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kinnon
 * @create 2022-08-16 2:12
 */
@Component
public class MessageInterceter implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            int letterUnreadCount = messageService.getLetterUnreadCount(user.getId(),null);
            int noticeUnreadCount = messageService.getNoticeUnreadCount(user.getId(),null);
            modelAndView.addObject("noticeUnreadCount",letterUnreadCount + noticeUnreadCount);
        }

    }
}
