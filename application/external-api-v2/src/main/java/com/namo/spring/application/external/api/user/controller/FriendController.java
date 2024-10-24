package com.namo.spring.application.external.api.user.controller;

import static com.namo.spring.core.common.code.status.ErrorStatus.*;

import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v2/friends")
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
    public ResponseDto<FriendshipResponse.FriendRequestListDto> getFriendRequestList(
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

    @Operation(summary = "내 친구 목록을 조회합니다. [20명씩 조회]", description = "내 친구 리스트를 보는 API 입니다. "
            + "즐겨찾기에 등록된 친구가 가장 먼저 나오고, 그 후에 닉네임 사전 순으로 정렬됩니다. "
            + "검색 조건이 있는경우 search에 넣어 주세요 아닌경우 사용하지 않으셔도 됩니다.")
    @GetMapping("")
    public ResponseDto<FriendshipResponse.FriendListDto> getFriendList(
            @AuthenticationPrincipal SecurityUserDetails member,
            @Parameter(description = "1부터 시작하는 페이지 번호입니다. 20명씩 조회됩니다.", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "검색 기능 사용시에만 값을 넣어주세요 (닉네임 or 이름 사용가능)", example = "캐슬")
            @RequestParam(required = false) String search
    ){
        return ResponseDto.onSuccess(friendUseCase
                .getFriendList(member.getUserId(), page, search));
    }

    @Operation(summary = "친구 즐겨찾기 등록/해제", description = "특정 친구를 즐겨찾기에 등록하거나, 이미 등록되어 있다면 해제합니다.")
    @PatchMapping("/{friendId}/toggle-favorite")
    @ApiErrorCodes(value = {
            NOT_FOUND_FRIENDSHIP_FAILURE,
    })
    public ResponseDto<String> toggleFavorite(@AuthenticationPrincipal SecurityUserDetails member,
            @PathVariable Long friendId) {
        boolean isFavorite = friendUseCase.toggleFavorite(member.getUserId(), friendId);
        String message = isFavorite ? "친구를 즐겨찾기에 등록했습니다." : "친구를 즐겨찾기에서 해제했습니다.";
        return ResponseDto.onSuccess(message);
    }
}
