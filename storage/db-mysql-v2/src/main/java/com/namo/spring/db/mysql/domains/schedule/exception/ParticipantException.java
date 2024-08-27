package com.namo.spring.db.mysql.domains.schedule.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class ParticipantException extends GeneralException {
    public ParticipantException(BaseErrorCode code) {
        super(code);
    }
}
