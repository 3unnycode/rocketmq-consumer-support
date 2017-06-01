package com.xiaoseller.rocketmq.consumer.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.xiaoseller.rocketmq.consumer.serializer.BodyReader;

public class JsonSerializerReader implements BodyReader {

	private final String body;

	public JsonSerializerReader(String body) {
		this.body = body;
	}

	@Override
	public <T> T getBody(Class<T> cls) {
		if (null == body || "".equals(body.trim())) {
			return null;
		}
		return (T) JSON.parseObject(body, cls);
	}

}
