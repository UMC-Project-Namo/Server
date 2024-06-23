package com.namo.spring.application.external.api.group.converter;

import com.namo.spring.db.mysql.domains.group.domain.MoimMemo;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;

public class MoimMemoConverter {
	private MoimMemoConverter() {
		throw new IllegalStateException("Utill Classes");
	}

	public static MoimMemo toMoimMemo(MoimSchedule moimSchedule) {
		return MoimMemo.builder()
			.moimSchedule(moimSchedule)
			.build();
	}
}
