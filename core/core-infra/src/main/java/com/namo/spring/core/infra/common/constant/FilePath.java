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
    // 활동 이미지 전용
    ACTIVITY_IMG("activity", "origin/activity"),
    RESIZED_ACTIVITY_IMG("activity", "resized/origin/activity"),
    // 일기 이미지 전용
    DIARY_IMG("diary", "origin/diary"),
    RESIZED_DIARY_IMG("diary", "resized/origin/diary"),
    // 커버 사진 전용
    COVER_IMAGE("cover", "origin/cover"),
    RESIZED_COVER_IMAGE("cover", "resized/origin/cover");


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
