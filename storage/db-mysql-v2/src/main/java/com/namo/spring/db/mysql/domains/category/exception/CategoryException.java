package com.namo.spring.db.mysql.domains.category.exception;

import com.namo.spring.core.common.code.BaseErrorCode;
import com.namo.spring.core.common.exception.GeneralException;

public class CategoryException extends GeneralException {
    public CategoryException(BaseErrorCode code) {
        super(code);
    }
}
