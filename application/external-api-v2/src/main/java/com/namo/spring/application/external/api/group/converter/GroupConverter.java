package com.namo.spring.application.external.api.group.converter;

import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.type.GroupStatus;

public class GroupConverter {
    private GroupConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static Group toGroup(String name, String profileImg, GroupStatus status) {
        return Group.builder()
                .name(name)
                .profileImg(profileImg)
                .status(status)
                .build();
    }
}
