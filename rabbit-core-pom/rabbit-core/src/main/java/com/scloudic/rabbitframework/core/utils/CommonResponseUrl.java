package com.scloudic.rabbitframework.core.utils;

/**
 * 通用响应地址
 *
 * @since 3.3.1
 */
public class CommonResponseUrl {
    //是否前后端分离
    private static boolean frontBlack = true;
    //是否404跳转,默认false以免没有404接口或界面
    private static boolean page404 = false;
    //登录界面跳转地址 401
    private static String loginUrl = "/toLogin";
    //权限跳转地址 407
    private static String unauthorizedUrl = "/unauthorized";
    //系统异常,500错误
    private static String sys500ErrorUrl = "/500";
    //404错误跳转地址
    private static String sys404ErrorUrl = "/404";
    //405错误跳转地址
    private static String sys405ErrorUrl = "/405";

    private static String otherError = "/otherError";

    public static boolean isFrontBlack() {
        return frontBlack;
    }

    public void setFrontBlack(boolean frontBlack) {
        CommonResponseUrl.frontBlack = frontBlack;
    }

    public static String getLoginUrl() {
        return CommonResponseUrl.loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        CommonResponseUrl.loginUrl = loginUrl;
    }

    public static String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        CommonResponseUrl.unauthorizedUrl = unauthorizedUrl;
    }

    public static String getSys500ErrorUrl() {
        return CommonResponseUrl.sys500ErrorUrl;
    }

    public void setSys500ErrorUrl(String sys500ErrorUrl) {
        CommonResponseUrl.sys500ErrorUrl = sys500ErrorUrl;
    }

    public static String getSys404ErrorUrl() {
        return CommonResponseUrl.sys404ErrorUrl;
    }

    public void setSys404ErrorUrl(String sys404ErrorUrl) {
        CommonResponseUrl.sys404ErrorUrl = sys404ErrorUrl;
    }

    public static String getSys405ErrorUrl() {
        return CommonResponseUrl.sys405ErrorUrl;
    }

    public void setSys405ErrorUrl(String sys405ErrorUrl) {
        CommonResponseUrl.sys405ErrorUrl = sys405ErrorUrl;
    }

    public static String getOtherError() {
        return CommonResponseUrl.otherError;
    }

    public void setOtherError(String otherError) {
        CommonResponseUrl.otherError = otherError;
    }

    public static boolean isPage404() {
        return CommonResponseUrl.page404;
    }

    public void setPage404(boolean page404) {
        CommonResponseUrl.page404 = page404;
    }

    /**
     * 去掉url的首斜线,web在307跳转时不需要首斜线
     *
     * @param url
     * @return
     */
    public static String dislodgeFirstSlash(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        if (url.charAt(0) == '/') {
            ;
            return url.substring(1);
        }
        return url;
    }

    public static void main(String[] args) {
        String a = "/23232/dddd";
        System.out.println(dislodgeFirstSlash(a));
    }
}
