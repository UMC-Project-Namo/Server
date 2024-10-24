package com.namo.spring.application.external.api.user.service;

import java.time.LocalDate;
import java.util.List;

import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.user.dto.FriendBirthdayQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.user.converter.FriendshipConverter;
import com.namo.spring.application.external.global.utils.FriendshipValidator;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.FriendshipService;
import com.namo.spring.db.mysql.domains.user.type.FriendshipStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendManageService {

    private static final Integer REQUEST_PAGE_SIZE = 20;

    private final FriendshipService friendshipService;
    private final FriendshipValidator friendshipValidator;

    /**
     * 친구 요청을 생성하는 메서드입니다.
     * !! 이미 친구 요청 정보가 있는지 검증합니다.
     * TODO: 알람 전송 구현이 필요합니다.
     * @param me 요청을 보내는 사람
     * @param target 친구 요청을 받는 사람
     */
    public void requestFriendShip(Member me, Member target) {
        friendshipValidator.validateAlreadyFriendship(me, target);
        friendshipService.createFriendShip(FriendshipConverter.toFriendShip(me, target));
    }

    /**
     * 나에게 온 친구 요청 목록을 페이징하여 조회하는 메서드입니다.
     * !! PENDING 상태의 요청만 조회됩니다. (거절, 수락된 친구관계 조회 x)
     * @param memberId 요청을 받는 사용자의 ID
     * @return PENDING 상태의 친구 요청 목록
     */
    public Page<Friendship> getReceivedFriendRequests(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page - 1, REQUEST_PAGE_SIZE);
        return friendshipService.readAllReceivedFriendshipByStatus(memberId, FriendshipStatus.PENDING, pageable);
    }

    /**
     * 단일 PENDING 친구 요청을 조회하는 메서드입니다.
     * @param friendshipId 친구 요청 ID
     * @return PENDING 상태의 친구 요청
     */
    public Friendship getPendingFriendship(Long friendshipId) {
        return friendshipService.readFriendshipByStatus(friendshipId, FriendshipStatus.PENDING)
                .orElseThrow(() -> new MemberException(ErrorStatus.NOT_FOUND_FRIENDSHIP_REQUEST));
    }

    /**
     * 친구 요청을 수락하는 메서입니다.
     * !! 나에게 온 요청이 맞는지 검증합니다.
     * @param memberId 수락할 사람 ID
     * @param friendship 수락할 요청 건
     */
    public void acceptRequest(Long memberId, Friendship friendship) {
        friendshipValidator.validateFriendshipToMember(friendship, memberId);
        friendship.accept();
        Friendship reverse = friendshipService.createFriendShip(
                FriendshipConverter.toFriendShip(friendship.getFriend(), friendship.getMember()));
        reverse.accept();
    }

    /**
     * 친구 요청을 거절하는 메서드입니다. (친구 요청 자체가 삭제됩니다)
     * !! 나에게 온 요청이 맞는지 검증합니다.
     * @param memberId 요청을 받은 사람의 ID
     * @param friendship 수락할 요청 건
     */
    public void rejectRequest(Long memberId, Friendship friendship) {
        friendshipValidator.validateFriendshipToMember(friendship, memberId);
        friendshipService.delete(friendship);
    }

    /**
     * 요청한 기간에 포함된 날짜가
     * 생일인 친구의 정보와 생일을 조회합니다.
     * @param memberId
     * @param startDate
     * @param endDate
     * @return 친구 memberId, nickname, birthday
     */
    public List<FriendBirthdayQuery> getMonthlyFriendsBirthday(Long memberId, LocalDate startDate, LocalDate endDate){
        return friendshipService.readBirthdayVisibleFriendsByPeriod(memberId, startDate, endDate);
    }

    public void checkMemberIsFriend(Long memberId, Long friendId) {
        if (!friendshipService.existsByMemberIdAndFriendId(memberId, friendId)) {
            throw new ScheduleException(ErrorStatus.NOT_FRIENDSHIP_MEMBER);
        }
    }

    /**
     * 나의 친구 목록을 가져옵니다.
     * !! 즐겨찾기에 등록된 친구가 가장 먼저 나오고, 그 후에 친구 닉네임 사전순으로 조회됩니다.
     *
     */
    public Page<Friendship> getAcceptedFriendship(Long memberId, int page, String search) {
        Pageable pageable = PageRequest.of(page - 1, REQUEST_PAGE_SIZE,
                Sort.by(Sort.Order.desc("isFavorite"), Sort.Order.asc("friend.nickname")));
        return friendshipService.readAllRequestFriendshipByStatus(memberId, FriendshipStatus.ACCEPTED, pageable, search);
    }

    public Friendship getAcceptedFriendship(Long memberId, Long friendId){
        return friendshipService.readFriendshipByStatus(memberId, friendId, FriendshipStatus.ACCEPTED)
                .orElseThrow(() -> new MemberException(ErrorStatus.NOT_FOUND_FRIENDSHIP_FAILURE));
    }
}
