package com.xiaoseller.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dianwoba.wireless.rocketmq.spring.boot.autoconfigure.RocketMqAutoConfiguration;
import com.xiaoseller.rocketmq.consumer.handler.HandlerInitalizing;

@Configuration
@ConditionalOnClass({ MessageListener.class })
@AutoConfigureBefore(RocketMqAutoConfiguration.class)
public class RocketMqHandlerAutoConfiguration {
	
	@Bean
	public HandlerInitalizing initHandler() {
		return new HandlerInitalizing();
	}
	
	@Bean
	@ConditionalOnMissingBean(MessageListener.class)
	@ConditionalOnProperty(value = "rocketmq.consumerGroupName")
    public MessageListener messageListener() {
        return new MessageListenerSupport();
    }

	@Bean
	@ConditionalOnMissingBean(TransactionCheckListener.class)
	public TransactionCheckListener transactionCheckListener() {
		return new TransactionCheckListener() {

			@Override
			public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
				return LocalTransactionState.COMMIT_MESSAGE;
			}
		};
	}
}
