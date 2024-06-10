package com.example.namo2.global.feignclient.kakao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.Response;
import feign.codec.ErrorDecoder;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

public class KakaoFeignClientExceptionDecoder implements ErrorDecoder {
	Logger logger = LoggerFactory.getLogger(KakaoFeignClientExceptionDecoder.class);

	@Override
	public Exception decode(String methodKey, Response response) {
		logger.debug("response : {}", response);

		if (response.status() >= 400 && response.status() <= 499) {
			logger.debug("feign 400번대 에러 발생 : {}", response.reason());
			return switch (response.status()) {
				case 401 -> new BaseException(BaseResponseStatus.KAKAO_UNAUTHORIZED);
				case 403 -> new BaseException(BaseResponseStatus.KAKAO_FORBIDDEN);
				default -> new BaseException(BaseResponseStatus.SOCIAL_WITHDRAWAL_FAILURE);
			};
		} else {
			logger.debug("feign client  500번대 에러 발생 : {}", response.reason());

			return switch (response.status()) {
				case 500 -> new BaseException(BaseResponseStatus.KAKAO_INTERNAL_SERVER_ERROR);
				case 502 -> new BaseException(BaseResponseStatus.KAKAO_BAD_GATEWAY);
				case 503 -> new BaseException(BaseResponseStatus.KAKAO_SERVICE_UNAVAILABLE);
				default -> new BaseException(BaseResponseStatus.FEIGN_SERVER_ERROR);
			};
		}
	}
}
