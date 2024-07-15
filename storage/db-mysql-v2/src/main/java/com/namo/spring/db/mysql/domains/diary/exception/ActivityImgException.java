package com.namo.spring.db.mysql.domains.diary.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class ActivityImgException extends GeneralException {
	public ActivityImgException(BaseErrorCode code) {
		super(code);
	}
}
