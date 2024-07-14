package com.namo.spring.db.mysql.domains.diary.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class DiaryException extends GeneralException {
	public DiaryException(BaseErrorCode code) {
		super(code);
	}
}
