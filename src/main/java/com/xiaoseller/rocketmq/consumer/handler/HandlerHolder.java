package com.xiaoseller.rocketmq.consumer.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * handlers holder
 * @author zhang peng
 * @version 2017年3月18日 下午3:09:23
 */
public class HandlerHolder {
	private static Map<String, MessageHandler> cacheHandlers = new HashMap<String, MessageHandler>();
	
	private HandlerHolder() {
		// nothing to do
	}
	
	public static MessageHandler getHandler(String key) {
		return cacheHandlers.get(key);
	}
	
	public static void addHandler(String key, MessageHandler handler) {
		cacheHandlers.put(key, handler);
	}
}
