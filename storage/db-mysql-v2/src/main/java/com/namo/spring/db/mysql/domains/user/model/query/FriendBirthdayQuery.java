package com.namo.spring.db.mysql.domains.user.model.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendBirthdayQuery {
    private Long memberId;
    private String nickname;
    private LocalDate birthday;
}
