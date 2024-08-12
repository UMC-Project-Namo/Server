package com.namo.spring.db.mysql.domains.record.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class ActivityParticipantException extends GeneralException {
    public ActivityParticipantException(BaseErrorCode code) {
        super(code);
    }
}
