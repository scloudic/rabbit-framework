package com.scloudic.rabbitframework.core.test.notification;

import com.scloudic.rabbitframework.core.notification.NotificationServerManager;
import org.junit.Test;

public class NotificationTest {
    @Test
    public void testListener() {
        TestNotificationServerListener listener = new TestNotificationServerListener();
        TestNotificationServerListenerTwo listenerTwo = new TestNotificationServerListenerTwo();
        NotificationServerManager manager = new NotificationServerManager();
        manager.registerListener(TestNotificationEvent.class, listener);
        manager.registerListener(TestNotificationEventTwo.class, listenerTwo);
        TestNotificationBean bean = new TestNotificationBean();
        bean.setName("1111");

        TestNotificationBean bean1 = new TestNotificationBean();
        bean1.setName("1111Two");
        TestNotificationEvent testNotificationEvent = new TestNotificationEvent(bean, 0);
        TestNotificationEventTwo testNotificationEventTwo = new TestNotificationEventTwo(bean1, 0);
        manager.notifyListeners(testNotificationEvent);
        manager.notifyListeners(testNotificationEventTwo);
    }
}
