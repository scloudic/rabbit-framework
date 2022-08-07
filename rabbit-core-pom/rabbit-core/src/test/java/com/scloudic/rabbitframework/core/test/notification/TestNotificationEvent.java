package com.scloudic.rabbitframework.core.test.notification;

import com.scloudic.rabbitframework.core.notification.NotificationEvent;

public class TestNotificationEvent  extends NotificationEvent {
    public TestNotificationEvent(Object message, int action) {
        super(message, action);
    }

    @Override
    protected String getActionName(int action) {
        return null;
    }
}
