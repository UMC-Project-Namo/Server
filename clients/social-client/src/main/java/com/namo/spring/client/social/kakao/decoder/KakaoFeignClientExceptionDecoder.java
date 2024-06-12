package com.namo.spring.client.social.kakao.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.KakaoClientException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class KakaoFeignClientExceptionDecoder implements ErrorDecoder {
	Logger logger = LoggerFactory.getLogger(KakaoFeignClientExceptionDecoder.class);

	@Override
	public Exception decode(String methodKey, Response response) {
		logger.debug("response : {}", response);

		if (response.status() >= 400 && response.status() <= 499) {
			logger.debug("feign 400번대 에러 발생 : {}", response.reason());
			return switch (response.status()) {
				case 401 -> new KakaoClientException(ErrorStatus.KAKAO_UNAUTHORIZED);
				case 403 -> new KakaoClientException(ErrorStatus.KAKAO_FORBIDDEN);
				default -> new KakaoClientException(ErrorStatus.SOCIAL_WITHDRAWAL_FAILURE);
			};
		} else {
			logger.debug("feign client  500번대 에러 발생 : {}", response.reason());

			return switch (response.status()) {
				case 500 -> new KakaoClientException(ErrorStatus.KAKAO_INTERNAL_SERVER_ERROR);
				case 502 -> new KakaoClientException(ErrorStatus.KAKAO_BAD_GATEWAY);
				case 503 -> new KakaoClientException(ErrorStatus.KAKAO_SERVICE_UNAVAILABLE);
				default -> new KakaoClientException(ErrorStatus.FEIGN_SERVER_ERROR);
			};
		}
	}
}
