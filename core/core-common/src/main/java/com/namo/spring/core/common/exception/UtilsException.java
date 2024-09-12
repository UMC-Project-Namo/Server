package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;

public class UtilsException extends GeneralException {
    public UtilsException(BaseErrorCode code) {
        super(code);
    }
}
