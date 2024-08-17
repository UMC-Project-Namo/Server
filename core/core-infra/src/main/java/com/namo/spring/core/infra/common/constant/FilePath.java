package com.namo.spring.core.infra.common.constant;

import lombok.Getter;

@Getter
public enum FilePath {
    MEETING_ACTIVITY_IMG("group/activity/"),
    MEETING_PROFILE_IMG("group/profile/"),
    INVITATION_ACTIVITY_IMG("invitation/activity/");

    private final String path;

    FilePath(String path) {
        this.path = path;
    }

}
