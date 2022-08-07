package com.scloudic.rabbitframework.core.test.notification;

import com.scloudic.rabbitframework.core.notification.NotificationServerManager;

public class NotificationTestTwo {
    public static void main(String[] args) {
        TestNotificationServerListener listener = new TestNotificationServerListener();
        NotificationServerManager manager = new NotificationServerManager();
        manager.registerListener(TestNotificationEvent.class, listener);
        TestNotificationBean bean = new TestNotificationBean();
        bean.setName("1111");
        TestNotificationEvent testNotificationEvent = new TestNotificationEvent(bean, 0);
        manager.notifyListeners(testNotificationEvent);
        System.out.println(TestNotificationEvent.class.isAssignableFrom(testNotificationEvent.getClass()));
        System.out.println(testNotificationEvent.getClass());
    }
}
