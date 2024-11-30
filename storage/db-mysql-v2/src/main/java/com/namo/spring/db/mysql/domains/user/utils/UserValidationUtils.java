package com.namo.spring.db.mysql.domains.user.utils;

import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class UserValidationUtils {

    private static final int MAX_NICKNAME_LENGTH = 12;
    private static final int MAX_BIO_LENGTH = 50;
    private static final String DEFAULT_PROFILE_IMAGE = "https://static.namong.shop/resized/origin/user/profile/namo-default-profile.png";

    private UserValidationUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static String validateNickname(String nickname) {
        if (!StringUtils.hasText(nickname)) {
            throw new IllegalArgumentException("닉네임은 null이거나 빈 문자열일 수 없습니다.");
        }
        if (nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new IllegalArgumentException(String.format("닉네임은 %d자 이내여야 합니다.", MAX_NICKNAME_LENGTH));
        }
        return nickname.trim();
    }

    public static String validateTag(String tag){
        if (!StringUtils.hasText(tag)) {
            throw new IllegalArgumentException("tag는 null이거나 빈 문자열일 수 없습니다.");
        }
        return tag;
    }

    public static LocalDate validateBirthday(LocalDate birthday) {
        if (birthday == null) {
            throw new IllegalArgumentException("생일은 null일 수 없습니다.");
        }
        if (birthday.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("생일은 미래 날짜일 수 없습니다.");
        }
        return birthday;
    }

    public static String validateBio(String bio) {
        if (bio == null) {
            return null; // 한 줄 소개는 선택적 필드로 허용
        }
        if (bio.length() > MAX_BIO_LENGTH) {
            throw new IllegalArgumentException(String.format("한 줄 소개는 %d자 이내여야 합니다.", MAX_BIO_LENGTH));
        }
        return bio.trim();
    }

    public static String validateProfileImage(String profileImage) {
        if (profileImage == null) {
            return DEFAULT_PROFILE_IMAGE; // 기본 프로필 이미지
        }
        String regex = "^(http|https)://.*\\.(jpg|jpeg|png|gif)$";
        if (!profileImage.matches(regex)) {
            throw new IllegalArgumentException("프로필 이미지는 유효한 이미지 URL이어야 합니다. (jpg, jpeg, png, gif 확장자만 허용)");
        }
        return profileImage;
    }
}
