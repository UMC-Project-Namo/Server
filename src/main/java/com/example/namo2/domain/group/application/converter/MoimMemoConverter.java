package com.example.namo2.domain.group.application.converter;

import com.example.namo2.domain.group.domain.MoimMemo;

import com.example.namo2.domain.group.domain.MoimSchedule;

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
