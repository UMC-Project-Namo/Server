package com.namo.spring.db.mysql.domains.user.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class TermException extends GeneralException {
	public TermException(BaseErrorCode code) {
		super(code);
	}
}
