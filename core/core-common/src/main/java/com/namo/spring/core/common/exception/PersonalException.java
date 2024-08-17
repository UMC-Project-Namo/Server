package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;

public class PersonalException extends GeneralException {
    public PersonalException(BaseErrorCode code) {
        super(code);
    }
}
