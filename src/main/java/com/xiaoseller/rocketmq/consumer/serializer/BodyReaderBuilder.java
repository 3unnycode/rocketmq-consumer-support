package com.xiaoseller.rocketmq.consumer.serializer;

import com.xiaoseller.rocketmq.consumer.serializer.impl.JsonSerializerReader;

public class BodyReaderBuilder {
	
	public static BodyReader build(String body) {
		return new JsonSerializerReader(body);
	}
}
