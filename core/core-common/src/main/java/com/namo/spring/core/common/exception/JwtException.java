package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;

public class JwtException extends GeneralException {
	public JwtException(BaseErrorCode code) {
		super(code);
	}
}
