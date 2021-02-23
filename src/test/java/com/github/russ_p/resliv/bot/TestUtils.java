package com.github.russ_p.resliv.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestUtils {

	private static ObjectMapper mapper;

	public static String json(Object value) {
		try {
			String str = objectMapper().writeValueAsString(value);
			return str;
		} catch (JsonProcessingException e) {
			return "{}";
		}
	}

	private static ObjectMapper objectMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		}
		return mapper;
	}
}
