package com.scloudic.rabbitframework.security.web.filter;

/**
 * @description: 父级安全过滤器
 * @author: juyang.liang
 * @since: 2024-03-31 23:23
 * @updatedUser:
 * @updatedDate:
 * @updatedRemark:
 * @version:
 */
public interface RabbitSecurityFilter {
    /**
     * 是否前端分离
     *
     * @param frontEndSeparate
     */
    public void setFrontEndSeparate(boolean frontEndSeparate);
}
