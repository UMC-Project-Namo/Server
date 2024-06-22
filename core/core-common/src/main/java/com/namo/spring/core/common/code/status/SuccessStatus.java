package com.namo.spring.core.common.code.status;

import org.springframework.http.HttpStatus;

import com.namo.spring.core.common.code.BaseCode;
import com.namo.spring.core.common.response.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

	// 성공
	SUCCESS(HttpStatus.OK, "성공"),

	;

	private final HttpStatus code;
	private final String message;

	public int getCode() {
		return code.value();
	}

	@Override
	public ResponseDto.ReasonDto getReason() {
		return ResponseDto.ReasonDto.builder()
			.isSuccess(true)
			.code(getCode())
			.message(message)
			.build();
	}

	@Override
	public ResponseDto.ReasonDto getReasonHttpStatus() {
		return ResponseDto.ReasonDto.builder()
			.status(this.code)
			.isSuccess(true)
			.code(getCode())
			.message(message)
			.build();
	}
}
