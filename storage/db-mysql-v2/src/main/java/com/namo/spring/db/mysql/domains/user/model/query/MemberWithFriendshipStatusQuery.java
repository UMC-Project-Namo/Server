package com.namo.spring.db.mysql.domains.user.model.query;

import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.type.FriendshipStatus;

public record MemberWithFriendshipStatusQuery(
        Member member,
        FriendshipStatus friendshipStatus
) {
}
