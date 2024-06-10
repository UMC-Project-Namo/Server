package com.example.namo2.global.config.properties;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExampleHolder {
	private Example example;
	private String name;
	private int code;
}
