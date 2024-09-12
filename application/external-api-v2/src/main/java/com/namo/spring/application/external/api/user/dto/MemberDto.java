package com.namo.spring.application.external.api.user.dto;

import com.namo.spring.db.mysql.domains.user.entity.Member;

public class MemberDto {
    public record MemberCreationRecord(Member member, boolean isNewUser) {
    }

}
