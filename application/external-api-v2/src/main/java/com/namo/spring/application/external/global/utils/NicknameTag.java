package com.namo.spring.application.external.global.utils;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameTag {

    private static final int NICKNAME_TAG_PARTS = 2;
    private static final int TAG_LENGTH = 4;
    private static final String NICKNAME_TAG_SEPARATOR = "#";
    private static final String TAG_PATTERN = "\\d{4}";

    private final String nickname;
    private final String tag;


    public static NicknameTag from(String nicknameTag) {
        String[] parts = nicknameTag.split(NICKNAME_TAG_SEPARATOR);
        validateParts(parts);
        return new NicknameTag(parts[0], parts[1]);
    }

    private static void validateParts(String[] parts) {
        if (parts.length != NICKNAME_TAG_PARTS) {
            throw new MemberException(ErrorStatus.INVALID_NICKNAME_TAG_FORMAT);
        }
        if (parts[0].isEmpty()) {
            throw new MemberException(ErrorStatus.INVALID_NICKNAME_TAG_FORMAT);
        }
        if (parts[1].length() != TAG_LENGTH && !parts[1].matches(TAG_PATTERN)) {
            throw new MemberException(ErrorStatus.INVALID_TAG_FORMAT);
        }
    }

}
