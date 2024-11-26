package com.namo.spring.db.mysql.domains.user.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.namo.spring.db.mysql.domains.user.model.dto.FriendBirthdayListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.model.query.FriendBirthdayQuery;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.repository.FriendshipRepository;
import com.namo.spring.db.mysql.domains.user.type.FriendshipStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.common.util.StringUtils;

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

    /**
     * memberId(나)에게 도착한 친구 관계로 탐색
     */
    public Page<Friendship> readAllReceivedFriendshipByStatus(Long memberId, FriendshipStatus status, Pageable pageable) {
        return friendshipRepository.findAllByFriendIdAndStatus(memberId, status, pageable);
    }

    /**
     * memberId(나)에서 시작한 친구 관계로 탐색
     * @return 검색어 있는 경우 // 검색어 없는 경우
     */
    public Page<Friendship> readAllRequestFriendshipByStatus(Long memberId, FriendshipStatus status, Pageable pageable, String search) {
        if (!StringUtils.isBlank(search)) {
            return friendshipRepository.findAllByMemberIdAndStatusAndSearch(memberId, status, search, pageable);
        }
        return friendshipRepository.findAllByMemberIdAndStatusFetchJoin(memberId, status, pageable);
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

    public void deleteAll(List<Friendship> friendships){
        friendshipRepository.deleteAll(friendships);
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
    public FriendBirthdayListDto readBirthdayVisibleFriendsByPeriod(Long memberId, LocalDate startDate,
                                                                        LocalDate endDate){
        return FriendBirthdayListDto.of(friendshipRepository.findBirthdayVisibleFriendIdsByPeriod(memberId, startDate, endDate));
    }

    /**
     * 모든 친구들의 생일을 조회합니다.
     * @param memberId
     * @return 친구 memberId, nickname, birthday
     */
    @Transactional(readOnly = true)
    public FriendBirthdayListDto readBirthdayVisibleFriendsByPeriod(Long memberId){
        return FriendBirthdayListDto.of(friendshipRepository.findBirthdayVisibleFriends(memberId));
    }
}
