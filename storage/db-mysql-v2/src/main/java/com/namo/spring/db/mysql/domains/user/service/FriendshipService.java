package com.namo.spring.db.mysql.domains.user.service;

import java.time.LocalDate;
import java.util.List;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.dto.FriendBirthdayQuery;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.repository.FriendshipRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;

    public List<Friendship> readFriendshipsByMemberIdAndFriendIds(Long memberId, List<Long> members) {
        return friendshipRepository.findAcceptedFriendshipsByMemberIdAndFriendIds(memberId, members);
    }

    public boolean existsByMemberIdAndFriendId(Long memberId, Long friendId) {
        return friendshipRepository.existsByMemberIdAndFriendId(memberId, friendId);
    }

    /**
     * startDate부터 endDate까지의 날짜가 생일인
     * 친구의 정보와 생일을 조회합니다.
     * @param memberId
     * @param startDate
     * @param endDate
     * @return 친구 memberId, nickname, birthday
     */
    @Transactional(readOnly = true)
    public List<FriendBirthdayQuery> readBirthdayVisibleFriendsByPeriod(Long memberId, LocalDate startDate,
                                                                        LocalDate endDate){
        return friendshipRepository.findBirthdayVisibleFriendIdsByPeriod(memberId, startDate, endDate);
    }
}
