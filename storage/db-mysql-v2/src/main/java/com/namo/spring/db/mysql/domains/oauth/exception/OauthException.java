package com.namo.spring.db.mysql.domains.oauth.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class OauthException extends GeneralException {
    public OauthException(BaseErrorCode code) {
        super(code);
    }
}
