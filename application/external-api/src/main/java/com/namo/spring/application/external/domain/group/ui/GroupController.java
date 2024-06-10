package com.example.namo2.domain.group.ui;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.namo2.domain.group.application.MoimFacade;
import com.example.namo2.domain.group.ui.dto.GroupRequest;
import com.example.namo2.domain.group.ui.dto.GroupResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "6. Group", description = "그룹 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
	private final MoimFacade moimFacade;

	@Operation(summary = "그룹 생성", description = "그룹 생성 API")
	@PostMapping(value = "",
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<GroupResponse.GroupIdDto> createMoim(
		@Parameter(description = "그룹 프로필 img") @RequestPart(required = false) MultipartFile img,
		@Parameter(description = "그룹명") @RequestPart(required = true) String groupName,
		HttpServletRequest request
	) {
		GroupResponse.GroupIdDto groupIdDto = moimFacade.createMoim(
			(Long)request.getAttribute("userId"),
			groupName,
			img
		);

		return new BaseResponse<>(groupIdDto);
	}

	@Operation(summary = "그룹 조회", description = "유저가 참여중인 그룹 조회 API")
	@GetMapping("")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<List<GroupResponse.GroupDto>> findMoims(
		HttpServletRequest request
	) {
		List<GroupResponse.GroupDto> moims = moimFacade.getMoims((Long)request.getAttribute("userId"));
		return new BaseResponse<>(moims);
	}

	@Operation(summary = "그룹 이름 수정", description = "그룹 이름 수정 API, 변경자의 입장에서만 수정되어 적용됨")
	@PatchMapping("/name")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Long> modifyMoimName(
		@Valid @RequestBody GroupRequest.PatchGroupNameDto patchGroupNameDto,
		HttpServletRequest request
	) {
		Long moimId = moimFacade.modifyMoimName(patchGroupNameDto, (Long)request.getAttribute("userId"));
		return new BaseResponse<>(moimId);
	}

	@Operation(summary = "그룹 참여", description = "그룹 참여 API")
	@PatchMapping("/participate/{code}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<GroupResponse.GroupParticipantDto> createMoimAndUser(
		@Parameter(description = "그룹 참여용 코드") @PathVariable("code") String code,
		HttpServletRequest request
	) {
		GroupResponse.GroupParticipantDto groupParticipantDto = moimFacade.createMoimAndUser(
			(Long)request.getAttribute("userId"),
			code);
		return new BaseResponse<>(groupParticipantDto);
	}

	@Operation(summary = "그룹 탈퇴", description = "그룹 탈퇴 API")
	@DeleteMapping("/withdraw/{groupId}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse removeMoimAndUser(
		@Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId,
		HttpServletRequest request
	) {
		moimFacade.removeMoimAndUser((Long)request.getAttribute("userId"), groupId);
		return BaseResponse.ok();
	}
}
