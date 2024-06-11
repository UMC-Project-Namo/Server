package com.namo.spring.application.external.global.feignclient.apple;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.AppleClientException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class AppleFeignClientExceptionDecoder implements ErrorDecoder {
	@Override
	public Exception decode(String methodKey, Response response) {
		if (response.status() >= 400 && response.status() <= 499) {
			return switch (response.status()) {
				case 400 -> new AppleClientException(ErrorStatus.APPLE_UNAUTHORIZED);
				default -> new AppleClientException(ErrorStatus.SOCIAL_WITHDRAWAL_FAILURE);
			};
		} else {
			return new AppleClientException(ErrorStatus.FEIGN_SERVER_ERROR);
		}
	}
}
