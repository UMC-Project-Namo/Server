package com.namo.spring.db.mysql.domains.point.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class PointException extends GeneralException {
    public PointException(BaseErrorCode code){super(code);}
}
