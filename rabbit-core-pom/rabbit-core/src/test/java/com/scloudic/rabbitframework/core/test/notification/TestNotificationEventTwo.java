package com.scloudic.rabbitframework.core.test.notification;

import com.scloudic.rabbitframework.core.notification.NotificationEvent;

public class TestNotificationEventTwo extends NotificationEvent {
    public TestNotificationEventTwo(Object message, int action) {
        super(message, action);
    }

    @Override
    protected String getActionName(int action) {
        return null;
    }
}
