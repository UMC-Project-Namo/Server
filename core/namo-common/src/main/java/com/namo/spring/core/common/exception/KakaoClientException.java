package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;

public class KakaoClientException extends GeneralException {
	public KakaoClientException(BaseErrorCode code) {
		super(code);
	}
}
