package com.namo.spring.db.mysql.domains.group.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class GroupException extends GeneralException {
	public GroupException(BaseErrorCode code) {
		super(code);
	}
}
