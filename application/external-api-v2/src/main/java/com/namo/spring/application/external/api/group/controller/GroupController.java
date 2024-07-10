package com.namo.spring.application.external.api.group.controller;

import com.namo.spring.application.external.api.group.dto.GroupRequest;
import com.namo.spring.application.external.api.group.dto.GroupResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "6. Group", description = "그룹 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/groups")
public class GroupController {

    @Operation(summary = "그룹 생성", description = "그룹 생성 API")
    @PostMapping(value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<GroupResponse.GroupIdDto> createGroup(
            @Parameter(description = "그룹 프로필 img") @RequestPart(required = false) MultipartFile img,
            @Parameter(description = "그룹명") @RequestPart(required = true) String groupName,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "그룹 조회", description = "유저가 참여중인 그룹 조회 API")
    @GetMapping("")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<List<GroupResponse.GroupDto>> findGroups(
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "그룹 이름 수정", description = "그룹 이름 수정 API, 변경자의 입장에서만 수정되어 적용됨")
    @PatchMapping("/name")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Long> modifyGroupName(
            @Valid @RequestBody GroupRequest.PatchGroupNameDto patchGroupNameDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "그룹 참여", description = "그룹 참여 API")
    @PatchMapping("/participate/{code}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<GroupResponse.GroupParticipantDto> createGroupAndUser(
            @Parameter(description = "그룹 참여용 코드") @PathVariable("code") String code,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "그룹 탈퇴", description = "그룹 탈퇴 API")
    @DeleteMapping("/withdraw/{groupId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Void> removeGroupAndUser(
            @Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }
}
