package com.namo.spring.application.external.api.user.controller;

import com.namo.spring.application.external.api.user.dto.UserRequest;
import com.namo.spring.application.external.api.user.dto.UserResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "1. Auth", description = "로그인, 회원가입 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/auths")
public class AuthController {

    @Operation(summary = "카카오 회원가입", description = "카카오 소셜 로그인을 통한 회원가입")
    @PostMapping(value = "/kakao/signup")
    @ApiErrorCodes(value = {
            ErrorStatus.USER_POST_ERROR,
            ErrorStatus.KAKAO_UNAUTHORIZED,
            ErrorStatus.KAKAO_FORBIDDEN,
            ErrorStatus.KAKAO_BAD_GATEWAY,
            ErrorStatus.KAKAO_SERVICE_UNAVAILABLE,
            ErrorStatus.KAKAO_INTERNAL_SERVER_ERROR,
            ErrorStatus.SOCIAL_LOGIN_FAILURE,
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @PreAuthorize("isAnonymous()")
    public ResponseDto<UserResponse.SignUpDto> kakaoSignup(
            @Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
    ) {
        return null;
    }

    @Operation(summary = "네이버 회원가입", description = "네이버 소셜 로그인을 통한 회원가입")
    @PostMapping(value = "/naver/signup")
    @ApiErrorCodes(value = {
            ErrorStatus.USER_POST_ERROR,
            ErrorStatus.NAVER_UNAUTHORIZED,
            ErrorStatus.NAVER_FORBIDDEN,
            ErrorStatus.SOCIAL_LOGIN_FAILURE,
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @PreAuthorize("isAnonymous()")
    public ResponseDto<UserResponse.SignUpDto> naverSignup(
            @Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
    ) {
        return null;
    }

    @Operation(summary = "애플 회원가입", description = "애플 소셜 로그인을 통한 회원가입.")
    @PostMapping(value = "/apple/signup")
    @ApiErrorCodes(value = {
            ErrorStatus.USER_POST_ERROR,
            ErrorStatus.MAKE_PUBLIC_KEY_FAILURE,
            ErrorStatus.APPLE_REQUEST_ERROR,
            ErrorStatus.APPLE_UNAUTHORIZED,
            ErrorStatus.SOCIAL_LOGIN_FAILURE,
            ErrorStatus.FEIGN_SERVER_ERROR
    })
    @PreAuthorize("isAnonymous()")
    public ResponseDto<UserResponse.SignUpDto> appleSignup(
            @Valid @RequestBody UserRequest.AppleSignUpDto dto
    ) {
        return null;
    }

    @Operation(summary = "토큰 재발급", description = "토큰 재발급")
    @PostMapping(value = "/reissuance")
    @ApiErrorCodes(value = {
            ErrorStatus.USER_POST_ERROR,
            ErrorStatus.SOCIAL_LOGIN_FAILURE,
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @PreAuthorize("isAnonymous()")
    public ResponseDto<UserResponse.ReissueDto> reissueAccessToken(
            @Valid @RequestBody UserRequest.SignUpDto signUpDto
    ) {
        return null;
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping(value = "/logout")
    @ApiErrorCodes(value = {
            ErrorStatus.USER_POST_ERROR,
            ErrorStatus.SOCIAL_LOGIN_FAILURE,
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<Void> logout(
            @Valid @RequestBody UserRequest.LogoutDto logoutDto
    ) {
        return null;
    }

    @Operation(summary = "카카오 회원 탈퇴", description = "카카오 회원 탈퇴")
    @PostMapping("/kakao/delete")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<?> removeKakaoUser(
            HttpServletRequest request) {
        return null;
    }

    @Operation(summary = "네이버 회원 탈퇴", description = "네이버 회원 탈퇴")
    @PostMapping("/naver/delete")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<?> removeNaverUser(
            HttpServletRequest request
    ) {
        return null;
    }

    @SuppressWarnings({"checkstyle:WhitespaceAround", "checkstyle:RegexpMultiline"})
    @Operation(summary = "애플 회원 탈퇴", description = "애플 회원 탈퇴")
    @PostMapping("/apple/delete")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<?> removeAppleUser(
            HttpServletRequest request
    ) {
        return null;
    }
}
