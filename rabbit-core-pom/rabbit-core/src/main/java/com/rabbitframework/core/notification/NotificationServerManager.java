package com.rabbitframework.core.notification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
//@ManagedResource(objectName = NotificationServerManager.MBEAN_NAME, description = "消息通知服务")
public class NotificationServerManager implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServerManager.class);
    public static final String NULL_SUBSCRIPTION = "NULL";
    public static final String MBEAN_NAME = "auto:name=NotificationServerManager";
    private final ConcurrentMap<Class<? extends NotificationServerListener>, Class<? extends NotificationEvent>> eventsMap;
    private final BlockingDeque<NotificationEvent> eventQueue;
    private ExecutorService executorService = null;
    private final Set listeners;
    private volatile boolean disposed = false;
    // private Object lock = new Object();

    public NotificationServerManager() {
        eventsMap = new ConcurrentHashMap<Class<? extends NotificationServerListener>, Class<? extends NotificationEvent>>();
        eventQueue = new LinkedBlockingDeque<NotificationEvent>();
        listeners = new ConcurrentHashSet();
    }

    // @ManagedOperation(description = "启动服务")
    public void start() {
        disposed = false;
        executorService = Executors.newCachedThreadPool();
        executorService.execute(this);
        // new Thread(this).start();
        logger.info("启动消息通知服务");
    }

    /**
     * 注册事件类型
     *
     * @param eventType
     * @param listenerType
     */
    public void registerEventType(Class<? extends NotificationEvent> eventType,
                                  Class<? extends NotificationServerListener> listenerType) {
        eventsMap.putIfAbsent(listenerType, eventType);
    }

    public void registerListener(NotificationServerListener listener) {
        registerListener(listener, null);
    }

    public void registerListener(NotificationServerListener listener, String subscription) {
        listeners.add(new Listener(listener, subscription));
    }

    public void unregisterListener(NotificationServerListener listener) {
        for (Iterator iterator = listeners.iterator(); iterator.hasNext(); ) {
            Listener l = (Listener) iterator.next();
            if (l.getListenerObject().equals(listener)) {
                listeners.remove(l);
            }
        }
    }

    public void fireEvent(NotificationEvent notificationEvent) {
        if (disposed) {
            return;
        }
        try {
            eventQueue.put(notificationEvent);
            logger.info("事件列表中放入新事件：" + notificationEvent.toString());
            logger.info("当前队列数：" + eventQueue.size());
        } catch (Exception e) {
            if (!disposed) {
                logger.error("Failed to queue notification:" + notificationEvent, e);
            }
        }
    }

    // @ManagedOperation(description = "停止服务")
    public void dispose() {
        this.disposed = true;
        eventsMap.clear();
        eventQueue.clear();
        listeners.clear();
        if (executorService != null) {
            executorService.shutdown();
        }
        logger.info("停止消息通知服务");
    }

    protected void notifyListeners(NotificationEvent notificationEvent) {
        if (disposed) {
            return;
        }
        for (Iterator iterator = listeners.iterator(); iterator.hasNext(); ) {
            Listener listener = (Listener) iterator.next();
            if (listener.matches(notificationEvent)) {
                listener.getListenerObject().onNotification(notificationEvent);
            }
        }
    }

    public void release() {
        this.dispose();
    }

    @Override
    public void run() {
        while (!disposed) {
            try {
                NotificationEvent notificationEvent = eventQueue.take();
                if (notificationEvent != null) {
                    logger.info("处理事件：" + notificationEvent.toString());
                    this.notifyListeners(notificationEvent);
                }
            } catch (Exception e) {
                logger.error("Failed to take notification from queue", e);
            }
        }
    }

    public class Listener {
        private static final String NULL_SUBSCRIPTION = "NULL";
        private final NotificationServerListener listener;
        private final List notificationClazz;
        private final String subscription;

        public Listener(NotificationServerListener listener, String subscription) {
            this.listener = listener;
            this.subscription = subscription == null ? NULL_SUBSCRIPTION : subscription;
            notificationClazz = new ArrayList();
            for (Iterator iterator = eventsMap.keySet().iterator(); iterator.hasNext(); ) {
                Class clazz = (Class) iterator.next();
                if (clazz.isAssignableFrom(listener.getClass())) {
                    notificationClazz.add(eventsMap.get(clazz));
                }
            }
        }

        public NotificationServerListener getListenerObject() {
            return listener;
        }

        public List getNotificationClazz() {
            return notificationClazz;
        }

        public String getSubscription() {
            return subscription;
        }

        public boolean matches(NotificationEvent notificationEvent) {
            for (Iterator iterator = notificationClazz.iterator(); iterator.hasNext(); ) {
                Class notificationClass = (Class) iterator.next();
                if (notificationClass.isAssignableFrom(notificationEvent.getClass())) {
                    return true;
                }
            }
            return false;
        }
    }
}