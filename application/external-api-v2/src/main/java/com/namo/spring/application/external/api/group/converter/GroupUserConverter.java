package com.namo.spring.application.external.api.group.converter;

import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.user.entity.User;

public class GroupUserConverter {
    private GroupUserConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static GroupUser toGroupUser(User user, Group group, int color) {
        return GroupUser.builder()
                .group(group)
                .user(user)
                .customGroupName(group.getName())
                .color(color)
                .build();
    }
}
