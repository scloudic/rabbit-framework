package com.rabbitframework.web.springboot.configure.servlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;

@FunctionalInterface
public interface RabbitWebApplicationPath {
    /**
     * Returns the configured path of the application.
     *
     * @return the configured path
     */
    String getPath();

    /**
     * Return a form of the given path that's relative to the Jersey application path.
     *
     * @param path the path to make relative
     * @return the relative path
     */
    default String getRelativePath(String path) {
        String prefix = getPrefix();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return prefix + path;
    }

    /**
     * Return a cleaned up version of the path that can be used as a prefix for URLs. The
     * resulting path will have path will not have a trailing slash.
     *
     * @return the prefix
     * @see #getRelativePath(String)
     */
    default String getPrefix() {
        String result = getPath();
        int index = result.indexOf('*');
        if (index != -1) {
            result = result.substring(0, index);
        }
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * Return a URL mapping pattern that can be used with a
     * {@link ServletRegistrationBean} to map Jersey's servlet.
     *
     * @return the path as a servlet URL mapping
     */
    default String getUrlMapping() {
        String path = getPath();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.equals("/")) {
            return "/*";
        }
        if (path.contains("*")) {
            return path;
        }
        if (path.endsWith("/")) {
            return path + "*";
        }
        return path + "/*";
    }
}
