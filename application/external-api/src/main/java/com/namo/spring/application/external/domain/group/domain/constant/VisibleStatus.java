package com.namo.spring.application.external.domain.group.domain.constant;

public enum VisibleStatus {
	/**
	 * ALL: 개인과 모임 모두에서 보임
	 * NOT_SEEN_PERSONAL_SCHEDULE: 개인 스케줄에서 모임 스케줄에서 삭제한 경우
	 * NOT_SEEN_MEMO: 모임 메모를 삭제하는 경우 -> 스케줄에 대해서는 보여야함
	 */
	ALL, NOT_SEEN_MEMO, NOT_SEEN_PERSONAL_SCHEDULE
}
