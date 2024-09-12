package com.namo.spring.application.external.global.common.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namo.spring.core.common.exception.JwtException;
import com.namo.spring.core.common.response.ResponseDto;
import com.namo.spring.core.common.utils.JwtErrorCodeUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtExceptionFilter 클래스는 JWT 관련 예외를 처리하는 필터입니다. <br/>
 * 이 클래스는 HTTP 요청을 처리하는 동안 발생하는 {@link JwtException}을 잡아서 적절한 HTTP 응답을 생성합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    /**
     * HTTP 요청을 처리하는 동안 발생하는 예외를 잡는 메서드입니다. <br/>
     * 이 메서드는 {@link JwtException}을 잡아서 {@link #sendAuthError(HttpServletResponse, JwtException)} 메서드를 호출하여 HTTP 응답을 생성합니다.
     *
     * @param request     : HTTP 요청
     * @param response    : HTTP 응답
     * @param filterChain : 필터 체인
     * @throws ServletException : 서블릿 예외
     * @throws IOException      : 입출력 예외
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (!response.isCommitted()) {
                JwtException exception = JwtErrorCodeUtil.determineAuthErrorException(e);
                sendAuthError(response, exception);
            }
        }
    }

    /**
     * 주어진 {@link JwtException}에 대한 HTTP 응답을 생성하는 메서드입니다. <br/>
     * 이 메서드는 {@link JwtException}의 {@link com.namo.spring.core.common.code.status.ErrorStatus}를 사용하여 {@link ResponseDto} 객체를 생성하고, 이를 JSON 형식으로 변환하여 응답 본문에 쓰는 역할을 합니다.
     *
     * @param response : HTTP 응답
     * @param e        : JWT 예외
     * @throws IOException : 입출력 예외
     */
    private void sendAuthError(HttpServletResponse response, JwtException e) throws IOException {
        if (!response.isCommitted()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(e.getErrorCode().getCode());

            ResponseDto<Object> body = ResponseDto.onFailure(
                    e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    null
            );
            objectMapper.writeValue(response.getWriter(), body);
        }
    }

}
