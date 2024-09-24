package com.namo.spring.application.external.global.utils;

import org.springframework.stereotype.Component;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.FriendshipService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FriendshipValidator {

    private final FriendshipService friendshipService;

    public void validateAlreadyFriendship(Member me, Member target) {
        if (friendshipService.existsByMemberIdAndFriendId(me.getId(), target.getId())) {
            throw new MemberException(ErrorStatus.AlREADY_FRIENDSHIP_MEMBER);
        }
    }

    public void validateFriendshipToMember(Friendship friendship, Long memberId) {
        if (!friendship.getFriend().getId().equals(memberId)) {
            throw new MemberException(ErrorStatus.NOT_MY_FRIENDSHIP_REQUEST);
        }
    }
}
