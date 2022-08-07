package com.scloudic.rabbitframework.core.test.notification;

import com.scloudic.rabbitframework.core.notification.NotificationEvent;
import com.scloudic.rabbitframework.core.notification.NotificationServerListener;

public class TestNotificationServerListenerTwo implements NotificationServerListener {
    @Override
    public void onNotification(NotificationEvent notificationEvent) {
        TestNotificationBean bean = notificationEvent.getObject();
        System.out.println("打印通知的值:" + bean.getName());
    }
}
