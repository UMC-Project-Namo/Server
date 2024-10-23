package com.namo.spring.db.mysql.domains.user.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.dto.FriendBirthdayQuery;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.repository.FriendshipRepository;
import com.namo.spring.db.mysql.domains.user.type.FriendshipStatus;

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

    public Friendship createFriendShip(Friendship friendship){
        return friendshipRepository.save(friendship);
    }

    public Page<Friendship> readAllFriendshipByStatus(Long memberId, FriendshipStatus status, Pageable pageable) {
        return friendshipRepository.findAllByFriendIdAndStatus(memberId, status, pageable);
    }

    public Optional<Friendship> readFriendshipByStatus(Long friendshipId, FriendshipStatus status){
        return friendshipRepository.findByIdAndStatus(friendshipId, status);
    }

    public Optional<Friendship> readFriendshipByStatus(Long memberId, Long friendId, FriendshipStatus status){
        return friendshipRepository.findByMemberIdAndFriendIdAndStatus(memberId,friendId, status);
    }


    public void delete(Friendship friendship) {
        friendshipRepository.delete(friendship);
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
