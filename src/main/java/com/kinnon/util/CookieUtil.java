package com.kinnon.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Kinnon
 * @create 2022-08-06 12:33
 */

public class CookieUtil {

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request == null || name == null) {
            throw new RuntimeException("request or name is null");
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;

    }
}
