package com.example.web.domain.common;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CommonVo implements Serializable {
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
