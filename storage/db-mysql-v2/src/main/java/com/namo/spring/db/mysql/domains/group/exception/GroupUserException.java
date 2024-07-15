package com.namo.spring.db.mysql.domains.group.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class GroupUserException extends GeneralException {
	public GroupUserException(BaseErrorCode code) {
		super(code);
	}
}
