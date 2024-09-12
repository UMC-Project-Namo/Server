package com.namo.spring.core.common.code;

import com.namo.spring.core.common.response.ResponseDto;

public interface BaseCode {

    public ResponseDto.ReasonDto getReason();

    public ResponseDto.ReasonDto getReasonHttpStatus();
}
