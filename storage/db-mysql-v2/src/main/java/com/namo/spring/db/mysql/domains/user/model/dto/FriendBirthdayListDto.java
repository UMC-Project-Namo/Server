package com.namo.spring.db.mysql.domains.user.model.dto;

import com.namo.spring.db.mysql.domains.user.model.query.FriendBirthdayQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FriendBirthdayListDto {
    private List<FriendBirthdayDto> friendsBirthdayDtoList;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FriendBirthdayDto {
        private Long memberId;
        private String nickname;
        private LocalDate birthday;
    }

    public static FriendBirthdayListDto of(List<FriendBirthdayQuery> queryList) {
        List<FriendBirthdayDto> friendsBirthdayDtoList = queryList.stream()
                .map(query -> FriendBirthdayDto.builder()
                        .memberId(query.getMemberId())
                        .nickname(query.getNickname())
                        .birthday(query.getBirthday())
                        .build())
                .toList();

        return FriendBirthdayListDto.builder()
                .friendsBirthdayDtoList(friendsBirthdayDtoList)
                .build();
    }
}
