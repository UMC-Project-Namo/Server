package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;

public class IndividualException extends GeneralException {
	public IndividualException(BaseErrorCode code) {
		super(code);
	}
}
