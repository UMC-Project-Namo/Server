package com.namo.spring.application.external.api.user.converter;

import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;

public class FriendShipConverter {

    public static Friendship toFriendShip(Member request, Member target){
        return Friendship.builder()
                .member(request)
                .friend(target)
                .build();
    }
}
