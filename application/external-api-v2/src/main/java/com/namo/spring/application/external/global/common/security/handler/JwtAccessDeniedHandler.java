package com.namo.spring.application.external.global.common.security.handler;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        ErrorStatus errorStatus = ErrorStatus.FORBIDDEN;

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorStatus.getCode());
        ResponseDto<Object> body = ResponseDto.onFailure(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                null
        );
        objectMapper.writeValue(response.getWriter(), body);
    }
}
