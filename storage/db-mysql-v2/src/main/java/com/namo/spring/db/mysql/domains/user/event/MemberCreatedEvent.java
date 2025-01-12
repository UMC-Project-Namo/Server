package com.namo.spring.db.mysql.domains.user.event;

import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCreatedEvent {
    private final Member member;
}
