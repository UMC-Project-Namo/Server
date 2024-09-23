package com.namo.spring.application.external.api.user.service;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.user.converter.FriendShipConverter;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.FriendshipService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendManageService {

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
        friendshipService.createFriendShip(FriendShipConverter.toFriendShip(me, target));
    }
}
