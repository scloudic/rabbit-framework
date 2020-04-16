package com.rabbitframework.security;

import java.util.HashMap;
import java.util.Map;

public class SecurityUser implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String realName;
    private String userId;
    private String avatarPath;
    private String loginName;
    private Map<String, Object> other;

    public SecurityUser() {

    }

    public void addOther(String key, Object value) {
        if (other == null) {
            other = new HashMap<String, Object>();
        }
        other.put(key, value);
    }

    public void setOther(Map<String, Object> other) {
        this.other = other;
    }

    public Map<String, Object> getOther() {
        if (other == null) {
            return new HashMap<String, Object>();
        }
        return other;
    }

    public SecurityUser(String userId, String loginName) {
        this.userId = userId;
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    @Override
    public String toString() {
        return userId;
    }
}
