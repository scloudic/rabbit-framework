package com.scloudic.rabbitframework.core.utils;

/**
 * 通用响应地址
 *
 * @since 3.3.1
 */
public class CommonResponseUrl {
    //是否前后端分离
    private boolean frontBlack = true;
    //登录界面跳转地址 401
    private String loginUrl = "";
    //权限跳转地址 407
    private String unauthorizedUrl = "";
    //系统异常,500错误
    private String sys500ErrorUrl = "";
    //404错误跳转地址
    private String sys404ErrorUrl = "";
    //405错误跳转地址
    private String sys405ErrorUrl = "";

    private String otherError = "";

    public boolean isFrontBlack() {
        return frontBlack;
    }

    public void setFrontBlack(boolean frontBlack) {
        this.frontBlack = frontBlack;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String getSys500ErrorUrl() {
        return sys500ErrorUrl;
    }

    public void setSys500ErrorUrl(String sys500ErrorUrl) {
        this.sys500ErrorUrl = sys500ErrorUrl;
    }

    public String getSys404ErrorUrl() {
        return sys404ErrorUrl;
    }

    public void setSys404ErrorUrl(String sys404ErrorUrl) {
        this.sys404ErrorUrl = sys404ErrorUrl;
    }

    public String getSys405ErrorUrl() {
        return sys405ErrorUrl;
    }

    public void setSys405ErrorUrl(String sys405ErrorUrl) {
        this.sys405ErrorUrl = sys405ErrorUrl;
    }

    public String getOtherError() {
        return otherError;
    }

    public void setOtherError(String otherError) {
        this.otherError = otherError;
    }
}
