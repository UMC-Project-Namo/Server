package com.namo.spring.db.mysql.domains.user.model.query;

import java.time.LocalDate;

public record FriendshipWithBirthdayQuery(
        Long memberId,
        String nickname,
        LocalDate birthday) {
}
