package com.namo.spring.core.infra.common.constant;

import lombok.Getter;

@Getter
public enum FilePath {
    CDN_PATH("namong", "https://static.namong.shop/"),
    //v1
    MEETING_ACTIVITY_IMG("", "group/activity/"),
    MEETING_PROFILE_IMG("", "group/profile/"),
    INVITATION_ACTIVITY_IMG("", "invitation/activity/"),
    //v2
    ACTIVITY_IMG("activity", "origin/activity"),
    RESIZED_ACTIVITY_IMG("activity", "resized/origin/activity"),
    DIARY_IMG("diary", "origin/diary"),
    RESIZED_DIARY_IMG("diary", "resized/origin/diary");

    private final String prefix;
    private final String path;

    FilePath(String prefix, String path) {
        this.prefix = prefix;
        this.path = path;
    }

    public static String getPathForPrefix(String prefix) {
        for (FilePath filePath : values()) {
            if (filePath.prefix.equals(prefix)) {
                return filePath.path;
            }
        }
        throw new IllegalArgumentException("Invalid prefix: " + prefix);
    }
}
