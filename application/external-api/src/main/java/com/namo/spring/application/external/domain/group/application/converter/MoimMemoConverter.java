package com.namo.spring.application.external.domain.group.application.converter;

import com.namo.spring.application.external.domain.group.domain.MoimSchedule;
import com.namo.spring.application.external.domain.group.domain.MoimMemo;

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
