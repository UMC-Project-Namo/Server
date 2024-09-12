package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode code) {
        super(code);
    }
}
