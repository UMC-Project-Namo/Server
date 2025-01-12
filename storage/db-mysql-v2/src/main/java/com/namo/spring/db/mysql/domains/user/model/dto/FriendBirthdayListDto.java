package com.namo.spring.db.mysql.domains.user.model.dto;

import com.namo.spring.db.mysql.domains.user.model.query.FriendshipWithBirthdayQuery;

import java.time.LocalDate;
import java.util.List;

public record FriendBirthdayListDto(List<FriendBirthdayDto> friendsBirthdayDtoList) {
    public record FriendBirthdayDto(Long memberId, String nickname, LocalDate birthday) {}

    public static FriendBirthdayListDto of(List<FriendshipWithBirthdayQuery> queryList) {
        List<FriendBirthdayDto> friendsBirthdayDtoList = queryList.stream()
                .map(query -> new FriendBirthdayDto(
                        query.memberId(),
                        query.nickname(),
                        query.birthday()))
                .toList();

        return new FriendBirthdayListDto(friendsBirthdayDtoList);
    }
}
