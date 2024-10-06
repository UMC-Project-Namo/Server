package com.namo.spring.application.external.api.user.controller;

import static com.namo.spring.core.common.code.status.ErrorStatus.*;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.user.dto.FriendshipResponse;
import com.namo.spring.application.external.api.user.usecase.FriendUseCase;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "8. 친구", description = "친구 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {

    private final FriendUseCase friendUseCase;

    @Operation(summary = "친구를 요청합니다.", description = "친구 요청 API 입니다. 상대방이 수락을 해야 친구가 됩니다."
            + "만약 상대방이 친구 요청을 보낸 상태이면 요청할 수 없습니다.")
    @ApiErrorCodes(value = {
            NOT_FOUND_USER_FAILURE,
            AlREADY_FRIENDSHIP_MEMBER
    })
    @PostMapping("/{nickname-tag}")
    public ResponseDto<String> requestFriend(
            @AuthenticationPrincipal SecurityUserDetails member,
            @Parameter(description = "친구 요청할 상대방 사용자의 닉네임#태그 입니다.", example = "캐슬#2342")
            @PathVariable(name = "nickname-tag") String nicknameTag
    ) {
        friendUseCase.requestFriendship(member.getUserId(), nicknameTag);
        return ResponseDto.onSuccess("친구 요청이 전송되었습니다.");
    }

    @Operation(summary = "나에게 온 친구 요청 목록을 조회합니다.", description = "친구 요청 목록 조회 API 입니다. 수락 또는 거절한 친구 요청은 표시되지 않습니다.")
    @GetMapping("/requests")
    public ResponseDto<List<FriendshipResponse.FriendRequestDto>> getFriendRequestList(
            @AuthenticationPrincipal SecurityUserDetails member,
            @Parameter(description = "1 부터 시작하는 페이지 번호입니다 (기본값 1)", example = "1")
            @RequestParam(value = "page", defaultValue = "1") int page
    ){
        return ResponseDto.onSuccess(friendUseCase
                .getFriendshipRequest(member.getUserId(), page));
    }

    @Operation(summary = "친구 요청 수락", description = "수신한 친구 요청을 수락합니다.")
    @ApiErrorCodes(value = {
            NOT_FOUND_FRIENDSHIP_REQUEST,
            NOT_MY_FRIENDSHIP_REQUEST
    })
    @PatchMapping("/requests/{friendshipId}/accept")
    public ResponseDto<String> acceptFriendRequest(
            @AuthenticationPrincipal SecurityUserDetails member,
            @Parameter(description = "수락할 친구 요청 정보 ID (memberId가 아닙니다)", example = "4")
            @PathVariable Long friendshipId
    ){
        friendUseCase.acceptRequest(member.getUserId(), friendshipId);
        return ResponseDto.onSuccess("친구 요청을 수락했습니다.");
    }

    @PatchMapping("/requests/{friendshipId}/reject")
    @ApiErrorCodes(value = {
            NOT_FOUND_FRIENDSHIP_REQUEST,
            NOT_MY_FRIENDSHIP_REQUEST
    })
    @Operation(summary = "친구 요청을 거절합니다.", description = "수신한 친구 요청을 거절하는 API 입니다.")
    public ResponseDto<String> rejectFriendRequest(
            @AuthenticationPrincipal SecurityUserDetails member,
            @Parameter(description = "거절할 친구 요청 정보 ID (memberId가 아닙니다)", example = "3")
            @PathVariable Long friendshipId
    ) {
        friendUseCase.rejectFriendRequest(member.getUserId(), friendshipId);
        return ResponseDto.onSuccess("친구 요청을 거절했습니다.");
    }

}
