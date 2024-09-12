package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;

public class AppleClientException extends GeneralException {
    public AppleClientException(BaseErrorCode code) {
        super(code);
    }
}
