package com.scloudic.rabbitframework.core.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class NotificationServerManager implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServerManager.class);
    private final ConcurrentMap<Class<? extends NotificationEvent>, Listener> eventsMap;
    private final BlockingDeque<NotificationEvent> eventQueue;
    private ExecutorService executorService = null;
    private volatile boolean disposed = false;

    public NotificationServerManager() {
        eventsMap = new ConcurrentHashMap<Class<? extends NotificationEvent>, Listener>();
        eventQueue = new LinkedBlockingDeque<NotificationEvent>();
    }

    public void start() {
        disposed = false;
        executorService = Executors.newCachedThreadPool();
        executorService.execute(this);
        logger.info("启动消息通知服务");
    }

    /**
     * 注册监听事件
     *
     * @param eventType
     * @param listener
     */
    public void registerListener(Class<? extends NotificationEvent> eventType,
                                 NotificationServerListener listener) {
        Listener l = new Listener(listener, eventType);
        eventsMap.putIfAbsent(eventType, l);
    }


    public void unregisterListener(Class<? extends NotificationEvent> eventType) {
        try {
            eventsMap.remove(eventType);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
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

    public void dispose() {
        this.disposed = true;
        eventsMap.clear();
        eventQueue.clear();
        if (executorService != null) {
            executorService.shutdown();
        }
        logger.info("停止消息通知服务");
    }

    public void notifyListeners(NotificationEvent notificationEvent) {
        if (disposed) {
            return;
        }
        Listener listener = eventsMap.get(notificationEvent.getClass());
        if (listener != null && listener.matches(notificationEvent)) {
            listener.getListenerObject().onNotification(notificationEvent);
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
        private final NotificationServerListener listener;
        private Class<? extends NotificationEvent> eventType;

        public Listener(NotificationServerListener listener,
                        Class<? extends NotificationEvent> eventType) {
            this.listener = listener;
            this.eventType = eventType;
        }

        public NotificationServerListener getListenerObject() {
            return listener;
        }

        public Class<? extends NotificationEvent> getEventType() {
            return eventType;
        }

        public boolean matches(NotificationEvent notificationEvent) {
            return eventType.isAssignableFrom(notificationEvent.getClass());
        }
    }
}