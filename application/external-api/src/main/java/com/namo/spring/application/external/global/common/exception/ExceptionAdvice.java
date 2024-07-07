package com.namo.spring.application.external.global.common.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GeneralException;
import com.namo.spring.core.common.response.ResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
		String errorMessage = e.getConstraintViolations().stream()
			.map(ConstraintViolation::getMessage)
			.findFirst()
			.orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

		return handleExceptionInternalConstraint(e, ErrorStatus.valueOf(errorMessage), HttpHeaders.EMPTY, request);
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException e,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		Map<String, String> errors = new LinkedHashMap<>();

		e.getBindingResult()
			.getFieldErrors()
			.forEach(fieldError -> {
				String fieldName = fieldError.getField();
				String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
				errors.merge(fieldName, errorMessage,
					(existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
			});

		return handleExceptionInternalArgs(
			e,
			HttpHeaders.EMPTY,
			ErrorStatus.BAD_REQUEST,
			request,
			errors
		);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler
	public ResponseEntity<Object> exception(Exception e, WebRequest request) {
		e.printStackTrace();

		return handleExceptionInternalFalse(
			e,
			ErrorStatus.INTERNAL_SERVER_ERROR,
			HttpHeaders.EMPTY,
			ErrorStatus.INTERNAL_SERVER_ERROR.getHttpStatus(),
			request,
			e.getMessage()
		);
	}

	@ExceptionHandler(value = GeneralException.class)
	public ResponseEntity onThrowException(GeneralException generalException, HttpServletRequest request) {
		ResponseDto.ErrorReasonDto errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
		return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
	}

	private ResponseEntity<Object> handleExceptionInternal(
		Exception e,
		ResponseDto.ErrorReasonDto reason,
		HttpHeaders headers,
		HttpServletRequest request
	) {
		ResponseDto<Object> body = ResponseDto.onFailure(reason.getCode(), reason.getMessage(), null);

		WebRequest webRequest = new ServletWebRequest(request);
		return super.handleExceptionInternal(
			e,
			body,
			headers,
			reason.getStatus(),
			webRequest
		);
	}

	private ResponseEntity<Object> handleExceptionInternalFalse(
		Exception e,
		ErrorStatus errorStatus,
		HttpHeaders headers,
		HttpStatus status,
		WebRequest request,
		String errorPoint
	) {
		ResponseDto<Object> body = ResponseDto.onFailure(errorStatus.getCode(), errorStatus.getMessage(),
			errorPoint);
		return super.handleExceptionInternal(
			e,
			body,
			headers,
			status,
			request
		);
	}

	private ResponseEntity<Object> handleExceptionInternalArgs(
		Exception e,
		HttpHeaders headers,
		ErrorStatus errorStatus,
		WebRequest request,
		Map<String, String> errorArgs
	) {
		ResponseDto<Object> body = ResponseDto.onFailure(
			errorStatus.getCode(),
			errorStatus.getMessage(),
			errorArgs
		);

		return super.handleExceptionInternal(
			e,
			body,
			headers,
			errorStatus.getHttpStatus(),
			request
		);
	}

	private ResponseEntity<Object> handleExceptionInternalConstraint(
		Exception e,
		ErrorStatus errorCommonStatus,
		HttpHeaders headers,
		WebRequest request
	) {
		ResponseDto<Object> body = ResponseDto.onFailure(
			errorCommonStatus.getCode(),
			errorCommonStatus.getMessage(),
			null
		);

		return super.handleExceptionInternal(
			e,
			body,
			headers,
			errorCommonStatus.getHttpStatus(),
			request
		);
	}
}
