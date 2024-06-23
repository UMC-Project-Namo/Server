package com.namo.spring.application.external.api.group.converter;

import com.namo.spring.db.mysql.domains.group.domain.Moim;

public class MoimConverter {
	private MoimConverter() {
		throw new IllegalStateException("Util Class");
	}

	public static Moim toMoim(String name, String imgUrl) {
		return Moim.builder()
			.name(name)
			.imgUrl(imgUrl)
			.build();
	}
}
