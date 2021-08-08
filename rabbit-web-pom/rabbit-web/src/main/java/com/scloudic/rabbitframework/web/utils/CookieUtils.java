package com.scloudic.rabbitframework.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static String getCookie(HttpServletRequest request, String cookieName) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 添加cookies,默认当前有效,httpOnly为false
     *
     * @param response response
     * @param name     名称key
     * @param value    值
     * @param path     路径
     */
    public static void addCookies(HttpServletResponse response, String name,
                                  String value, String path) {
        addCookies(response, name, value, path, false);
    }

    /**
     * 添加cookies,默认当前有效
     *
     * @param response response
     * @param name     名称key
     * @param value    值
     * @param path     路径
     * @param httpOnly 是否只读
     */
    public static void addCookies(HttpServletResponse response, String name, String value,
                                  String path, boolean httpOnly) {
        addCookies(response, name, value, -1, path, httpOnly);
    }

    public static void addCookies(HttpServletResponse response, String name, String value,
                                  String path, int expiry) {
        addCookies(response, name, value, expiry, path, false);
    }


    public static void addCookies(HttpServletResponse response, String name,
                                  String value, int expiry, String path, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(expiry);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void clearCookie(HttpServletResponse response, String key, String path) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath(path);
        response.addCookie(cookie);
    }
}
