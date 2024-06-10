package com.example.namo2.global.common.exception;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.debug(e.getMessage());
		BaseResponseStatus errorStatus = BaseResponseStatus.INTERNET_SERVER_ERROR;
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(makeErrorResponse(errorStatus.getCode(), errorStatus.getMessage()));
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<ErrorResponse> handleIOException(BaseException e) {
		BaseResponseStatus statusEnum = e.getStatus();
		log.debug(e.getMessage());
		return ResponseEntity
			.status(NON_AUTHORITATIVE_INFORMATION)
			.body(makeErrorResponse(statusEnum.getCode(), statusEnum.getMessage()));
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
		BaseResponseStatus statusEnum = e.getStatus();
		//        log.debug(e.getMessage());
		e.printStackTrace();
		return ResponseEntity
			.status(statusEnum.getCode())
			.body(makeErrorResponse(statusEnum.getCode(), statusEnum.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotArgumentException(MethodArgumentNotValidException e) {
		log.debug(e.getMessage());
		return ResponseEntity
			.badRequest()
			.body(makeErrorResponse(HttpStatus.BAD_REQUEST.value(), makeErrorMessage(e)));
	}

	private String makeErrorMessage(MethodArgumentNotValidException e) {
		StringBuilder sb = new StringBuilder();
		e.getBindingResult().getAllErrors()
			.forEach(objectError -> sb
				.append("[")
				.append("error field: ")
				.append(((FieldError)objectError).getField())
				.append(" - ")
				.append(objectError.getDefaultMessage())
				.append("]"));
		return sb.toString();
	}

	private ErrorResponse makeErrorResponse(Integer code, String message) {
		return ErrorResponse.builder()
			.code(code)
			.message(message)
			.build();
	}

}
