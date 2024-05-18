package com.example.namo2.global.feignclient.naver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.Response;
import feign.codec.ErrorDecoder;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

public class NaverFeignException implements ErrorDecoder {
	private final Logger logger = LoggerFactory.getLogger(NaverFeignException.class);

	@Override
	public Exception decode(String methodKey, Response response) {

		if (response.status() >= 400 && response.status() < 500) {
			return switch (response.status()) {
				case 401 -> new BaseException(BaseResponseStatus.NAVER_UNAUTHORIZED);
				case 403 -> new BaseException(BaseResponseStatus.NAVER_FORBIDDEN);
				case 404 -> new BaseException(BaseResponseStatus.NAVER_NOT_FOUND);
				default -> new BaseException(BaseResponseStatus.SOCIAL_WITHDRAWAL_FAILURE);
			};
		} else {
			return new BaseException(BaseResponseStatus.FEIGN_SERVER_ERROR);
		}
	}
}
