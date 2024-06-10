package com.example.namo2.global.feignclient.apple;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import com.example.namo2.global.common.exception.BaseException;

public class AppleResponseConverter {
	public AppleResponseConverter() {
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
			.orElseThrow(() -> new BaseException(APPLE_REQUEST_ERROR));
	}
}
