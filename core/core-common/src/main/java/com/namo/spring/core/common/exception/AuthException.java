package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode code) {
        super(code);
    }
}
