package com.namo.spring.db.mysql.domains.schedule.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class PersonalScheduleException extends GeneralException {
	public PersonalScheduleException(BaseErrorCode code) {
		super(code);
	}
}
