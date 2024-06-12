package com.namo.spring.core.common.code;

import com.namo.spring.core.common.response.ResponseDto;

public interface BaseErrorCode {

	public ResponseDto.ErrorReasonDto getReason();

	public ResponseDto.ErrorReasonDto getReasonHttpStatus();
}

