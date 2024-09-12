package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;

public class GroupException extends GeneralException {
    public GroupException(BaseErrorCode code) {
        super(code);
    }
}
