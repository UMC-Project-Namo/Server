package com.namo.spring.core.common.exception;

import com.namo.spring.core.common.code.BaseErrorCode;

public class NaverClientException extends GeneralException {
    public NaverClientException(BaseErrorCode code) {
        super(code);
    }
}
