package com.namo.spring.application.external.api.user.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.user.converter.FriendshipConverter;
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

    /**
     * 친구 요청을 생성하는 메서드입니다.
     * !! 이미 친구 요청 정보가 있는지 검증합니다.
     * TODO: 알람 전송 구현이 필요합니다.
     * @param me 요청을 보내는 사람
     * @param target 친구 요청을 받는 사람
     */
    public void requestFriendShip(Member me, Member target) {
        if (friendshipService.existsByMemberIdAndFriendId(me.getId(), target.getId())){
            throw new MemberException(ErrorStatus.AlREADY_FRIENDSHIP_MEMBER);
        }
        friendshipService.createFriendShip(FriendshipConverter.toFriendShip(me, target));
    }

    /**
     * 나에게 온 친구 요청 목록을 페이징하여 조회하는 메서드입니다.
     * !! PENDING 상태의 요청만 조회됩니다. (거절, 수락된 친구관계 조회 x)
     * @param memberId 요청을 받는 사용자의 ID
     * @return PENDING 상태의 친구 요청 목록
     */
    public List<Friendship> getReceivedFriendRequests(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page - 1, REQUEST_PAGE_SIZE);
        return friendshipService.readAllFriendshipByStatus(memberId, FriendshipStatus.PENDING, pageable);
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
        if (!friendship.getFriend().getId().equals(memberId)){
            throw new MemberException(ErrorStatus.NOT_MY_FRIENDSHIP_REQUEST);
        }
        friendship.accept();
    }
}