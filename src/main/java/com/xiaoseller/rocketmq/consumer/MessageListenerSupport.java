package com.xiaoseller.rocketmq.consumer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.xiaoseller.rocketmq.consumer.handler.HandlerHolder;
import com.xiaoseller.rocketmq.consumer.handler.MessageHandler;
import com.xiaoseller.rocketmq.consumer.serializer.BodyReader;
import com.xiaoseller.rocketmq.consumer.serializer.BodyReaderBuilder;

public class MessageListenerSupport implements MessageListenerConcurrently {
	private static final Logger logger = LoggerFactory.getLogger(MessageListenerSupport.class);
	@Value("${rocketmq.consumerGroupName}")
	public String consumerGroupName;
	
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		MessageExt msg = msgs.get(0);
		byte[] body = msg.getBody();
		String bodyString = null;
		if (null != body) {
			try {
				bodyString = new String(body, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("can not parse to string with UTF-8", e);
			}
		}
		String key = msg.getKeys();
		String topic = msg.getTopic();
		String tags = msg.getTags();
		logger.info(Thread.currentThread().getName() + " Receive New Messages: " + bodyString + " ,key:" + key
				+ ",tags:" + tags + ",topic:" + topic);
		long bornTimestamp = msg.getBornTimestamp();
		long currentTimeMillis = System.currentTimeMillis();
		long timeElapsedFromStoreInMqToReceiveMsg = currentTimeMillis - bornTimestamp;
		ConsumeConcurrentlyStatus consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		// 300s = 5min 作为一个消费阈值，超过这个值的消息都作为无效消息判断
		if (timeElapsedFromStoreInMqToReceiveMsg >= 300000) {
			logger.warn("msg:{} is invalid, it was born {}s ago", msg, timeElapsedFromStoreInMqToReceiveMsg / 1000);
			return consumeConcurrentlyStatus;
		}
		String handlerKey = topic + tags;
		MessageHandler handler = HandlerHolder.getHandler(handlerKey.toLowerCase());
		if (null != handler) {
			try {
				BodyReader reader = BodyReaderBuilder.build(bodyString);
				consumeConcurrentlyStatus = handler.handle(key, reader);
			} catch (Throwable t) {
				logger.error("mq handler error, msginfo:{}", msg, t);
				throw t;
			}
		}
		return consumeConcurrentlyStatus;
	}
}
