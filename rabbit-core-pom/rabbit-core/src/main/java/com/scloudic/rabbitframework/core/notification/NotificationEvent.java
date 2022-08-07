package com.scloudic.rabbitframework.core.notification;

import java.util.EventObject;

/**
 * 通知事件
 *
 * @author justin.liang
 */
public abstract class NotificationEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    protected static final Object NULL_MESSAGE = "";
    protected static final int NULL_ACTION = 0;
    private long timestamp;

    private int action = NULL_ACTION;
    private final String eventName = getClassName(getClass());

    public NotificationEvent(Object message, int action) {
        super((message == null ? NULL_MESSAGE : message));
        this.action = action;
        timestamp = System.currentTimeMillis();
    }

    public int getAction() {
        return action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String toString() {
        return eventName + "{" + "action=" + action + ",  timestamp=" + timestamp + "}";
    }

    protected String getPayloadToString() {
        return source.toString();
    }


    public <T> T getObject() {
        return (T) getSource();
    }
    
    public static String getClassName(Class clazz) {
        if (clazz == null) {
            return null;
        }
        String name = clazz.getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public String getActionName() {
        return getActionName(action);
    }

    protected abstract String getActionName(int action);

}
