package com.example.namo2.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class BaseResponse<T> {
	private final String message;
	private final int code;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;

	public static BaseResponse ok() {
		return new BaseResponse();
	}

	private BaseResponse() {
		this.message = BaseResponseStatus.SUCCESS.getMessage();
		this.code = BaseResponseStatus.SUCCESS.getCode();
	}

	// 요청에 성공한 경우
	public BaseResponse(T result) {
		this.message = BaseResponseStatus.SUCCESS.getMessage();
		this.code = BaseResponseStatus.SUCCESS.getCode();
		this.result = result;
	}

	// 요청에 실패한 경우
	public BaseResponse(BaseResponseStatus status) {
		this.message = status.getMessage();
		this.code = status.getCode();
	}
}
