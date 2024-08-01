package com.namo.spring.application.external.api.group.controller;

import com.namo.spring.application.external.api.group.api.GroupApi;
import com.namo.spring.application.external.api.group.dto.GroupRequest;
import com.namo.spring.application.external.api.group.dto.GroupResponse;
import com.namo.spring.application.external.api.group.facade.GroupFacade;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;
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
@RequestMapping("/api/v1/groups")
public class GroupController implements GroupApi {
    private final GroupFacade groupFacade;

    /**
     * 그룹 생성
     */
    @PostMapping(value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseDto<GroupResponse.GroupIdDto> createGroup(
            @RequestPart(name = "img", required = false) MultipartFile img,
            @RequestPart(name = "groupName", required = true) String groupName,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        GroupResponse.GroupIdDto groupIdDto = groupFacade.createGroup(
                user.getUserId(),
                groupName,
                img
        );
        return ResponseDto.onSuccess(groupIdDto);
    }

    /**
     * 그룹 목록 조회
     */
    @GetMapping("")
    public ResponseDto<List<GroupResponse.GetGroupDto>> getGroups(
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<GroupResponse.GetGroupDto> groups = groupFacade.getGroups(user.getUserId());
        return ResponseDto.onSuccess(groups);
    }

    /**
     * 그룹 이름 수정
     */
    @PatchMapping("/name")
    public ResponseDto<Long> modifyGroupName(
            @Valid @RequestBody GroupRequest.PatchGroupNameDto patchGroupNameDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        Long groupId = groupFacade.modifyGroupName(patchGroupNameDto, user.getUserId());
        return ResponseDto.onSuccess(groupId);
    }

    /**
     * 그룹 참여
     */
    @PatchMapping("/participate/{code}")
    public ResponseDto<GroupResponse.GroupParticipantDto> createGroupAndUser(
            @PathVariable(name = "code") String code,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        GroupResponse.GroupParticipantDto groupParticipantDto = groupFacade.createGroupAndUser(
                user.getUserId(),
                code);
        return ResponseDto.onSuccess(groupParticipantDto);
    }

    /**
     * 그룹 탈퇴
     */
    @DeleteMapping("/withdraw/{groupId}")
    public ResponseDto<Void> removeGroupAndUser(
            @PathVariable(name = "groupId") Long groupId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        groupFacade.removeGroupAndUser(user.getUserId(), groupId);
        return ResponseDto.onSuccess(null);
    }
}
