package com.namo.spring.db.mysql.domains.schedule.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScheduleType {
    PERSONAL(0), // 개인 스케줄
    MEETING(1); // 모임 스케줄

    private final int value;

    public int getValue() {
        return value;
    }
}
