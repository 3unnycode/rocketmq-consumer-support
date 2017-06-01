package com.xiaoseller.rocketmq.consumer.handler;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class HandlerInitalizing{
	private final static Logger LOGGER = LoggerFactory.getLogger(HandlerInitalizing.class);

	@Autowired(required = false)
	public void  init(List<MessageHandler> messageHandlers)throws Exception {
		if (null == messageHandlers) {
			return;
		}
		for (MessageHandler handler : messageHandlers) {
			if (null == handler) {
				return;
			}
			if (null == handler.getTopic() || "".equals(handler.getTopic().trim())) {
				throw new IllegalArgumentException(handler.getClass() + " handler's topic is empty.");
			}
			if (null == handler.getTag() || "".equals(handler.getTag().trim())) {
				throw new IllegalArgumentException(handler.getClass() + " handler's tag is empty.");
			}
			String key = handler.getTopic() + handler.getTag();
			HandlerHolder.addHandler(key.toLowerCase(), handler);
			LOGGER.info("init rocket mq handler process, add topic:{}, tags:{}, handler:{}, handlerKey:{}", handler.getTopic(), handler.getTag(), handler.getClass(), key.toLowerCase());
		}
	}
}
