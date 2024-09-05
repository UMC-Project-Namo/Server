package com.namo.spring.db.mysql.domains.notification.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class NotificationException extends GeneralException {
    public NotificationException(BaseErrorCode code) {
        super(code);
    }
}
