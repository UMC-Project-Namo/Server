package com.namo.spring.db.mysql.domains.record.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class ActivityException extends GeneralException {
	public ActivityException(BaseErrorCode code) {
		super(code);
	}
}
