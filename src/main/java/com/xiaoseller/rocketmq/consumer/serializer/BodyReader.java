package com.xiaoseller.rocketmq.consumer.serializer;


public interface BodyReader {
	
	<T> T getBody(Class<T> cls);
}
