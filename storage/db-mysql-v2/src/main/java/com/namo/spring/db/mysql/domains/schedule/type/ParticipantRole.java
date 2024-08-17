package com.namo.spring.db.mysql.domains.schedule.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParticipantRole {
    NON_OWNER(0),
    OWNER(1);

    private final int value;

    public int getValue() {
        return value;
    }
}
