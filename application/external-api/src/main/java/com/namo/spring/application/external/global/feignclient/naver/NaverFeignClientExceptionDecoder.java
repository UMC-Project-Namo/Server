package com.namo.spring.application.external.global.feignclient.naver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.NaverClientException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class NaverFeignClientExceptionDecoder implements ErrorDecoder {
	private final Logger logger = LoggerFactory.getLogger(NaverFeignClientExceptionDecoder.class);

	@Override
	public Exception decode(String methodKey, Response response) {

		if (response.status() >= 400 && response.status() < 500) {
			return switch (response.status()) {
				case 401 -> new NaverClientException(ErrorStatus.NAVER_UNAUTHORIZED);
				case 403 -> new NaverClientException(ErrorStatus.NAVER_FORBIDDEN);
				case 404 -> new NaverClientException(ErrorStatus.NAVER_NOT_FOUND);
				default -> new NaverClientException(ErrorStatus.SOCIAL_WITHDRAWAL_FAILURE);
			};
		} else {
			return new NaverClientException(ErrorStatus.FEIGN_SERVER_ERROR);
		}
	}
}
