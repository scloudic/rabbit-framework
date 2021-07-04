package com.rabbitframework.core.notification;

/**
 * 通知服务监听器
 * 
 * @author justin.liang
 */
public interface NotificationServerListener {
	/**
	 * 发起通知事件
	 * 
	 * @param notificationEvent
	 */
	void onNotification(NotificationEvent notificationEvent);
}
