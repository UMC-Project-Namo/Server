package com.namo.spring.application.external.global.feignclient.apple;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.AppleClientException;

public class AppleResponseConverter {
	private AppleResponseConverter() {
		throw new IllegalStateException("Utility class.");
	}

	public static AppleResponse.ApplePublicKeyDto toApplePublicKey(
		AppleResponse.ApplePublicKeyListDto applePublicKeys,
		Object alg,
		Object kid
	) {
		return applePublicKeys.getKeys().stream()
			.filter(key -> key.getAlg().equals(alg) && key.getKid().equals(kid))
			.findFirst()
			.orElseThrow(() -> new AppleClientException(ErrorStatus.APPLE_REQUEST_ERROR));
	}
}
