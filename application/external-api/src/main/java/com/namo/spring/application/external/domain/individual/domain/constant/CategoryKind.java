package com.namo.spring.application.external.domain.individual.domain.constant;

public enum CategoryKind {
	/*
	 SCHEDULE: 기본
	 MOIM: MOIM 기본
	 CUSTOM: 유저가 생성한 카테고리
	 */
	SCHEDULE("일정"), MOIM("모임"), CUSTOM("커스텀");

	private String categoryName;

	CategoryKind(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}
}
