package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.code.status.ErrorStatus;

public class JwtException extends GeneralException {
	public JwtException(BaseErrorCode code) {
		super(code);
	}

	// HACK: 2024.07.02. JwtErrorCodeUtil을 위한 임시 method - 루카
	public ErrorStatus getErrorCode() {
		return (ErrorStatus)super.getCode();
	}
}
