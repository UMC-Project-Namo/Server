package com.example.namo2.global.common.exception;

import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
	private BaseResponseStatus status;
}
