package com.namo.spring.db.mysql.domains.schedule.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class MeetingScheduleException extends GeneralException {
	public MeetingScheduleException(BaseErrorCode code) {
		super(code);
	}
}
