package com.namo.spring.db.mysql.domains.user.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class UserException extends GeneralException {
	public UserException(BaseErrorCode code) {
		super(code);
	}
}
