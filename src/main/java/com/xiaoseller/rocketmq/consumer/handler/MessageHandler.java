package com.xiaoseller.rocketmq.consumer.handler;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

import com.xiaoseller.rocketmq.consumer.serializer.BodyReader;

public interface MessageHandler {

	/**
	 * mq topic
	 * @author zhang peng
	 * @version2017年3月18日 下午1:30:33
	 * @return
	 */
	String getTopic();
	
	/**
	 * mq tag
	 * @author zhang peng
	 * @version2017年3月18日 下午2:59:57
	 * @return
	 */
	String getTag();
	
	/**
	 * 消息处理
	 * @author zhang peng
	 * @version2017年3月18日 下午2:52:32
	 */
	ConsumeConcurrentlyStatus handle(String key, BodyReader reader);
	
}
