package com.namo.spring.application.external.api.schedule.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "모임 일정", description = "모임 일정 관련 API")
public interface MeetingScheduleApi {
    @Operation(summary = "모임 일정 생성", description = "모임 일정을 생성합니다. 요청 성공 시 모임 일정 ID를 전송합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 시작 날짜가 종료 날짜 이전 이어야 합니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 400,
                    	"message": "시작 날짜가 종료 날짜 이전 이어야 합니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 초대자 중에 중복되는 참여자가 있습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 400,
                    	"message": "중복되는 참여자입니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 모임 일정은 최소 1명, 최대 9명까지 초대 가능합니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 400,
                    	"message": "모임 일정은 최소 1명, 최대 9명까지 초대 가능합니다."
                    }
                    """),}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저를 찾을 수 없습니다."
                    }
                    """), @ExampleObject(name = "요청 실패 - 모임 일정 카테고리를 찾을 수 없습니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "카테고리를 찾을 수 없습니다."
            }
            """),
            @ExampleObject(name = "요청 실패 - 친구인 유저를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "친구인 유저를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 모임 일정에 대한 이미지 파일 업로드 과정에서 오류가 발생하였습니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "파일 업로드 과정에서 오류가 발생하였습니다."
                    }
                    """)
    }))
    ResponseDto<Long> createMeetingSchedule(
            @Parameter(description = "생성 요청 dto") @Valid @RequestPart MeetingScheduleRequest.PostMeetingScheduleDto dto,
            @Parameter(description = "모임 일정 프로필 이미지") @RequestPart(required = false) MultipartFile image,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "모임 일정 목록 조회", description = "모임 일정 목록을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저를 찾을 수 없습니다."
                    }
                    """)
    }))
    ResponseDto<List<MeetingScheduleResponse.GetMeetingScheduleSummaryDto>> getMyMeetingSchedules(
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "모임 초대자 월간 일정 조회", description = "모임에 초대할 유저들의 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 유효한 날짜 값을 입력해주세요.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유효한 날짜 값을 입력해주세요."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 모임 일정은 최소 1명, 최대 9명까지 초대 가능합니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "모임 일정은 최소 1명, 최대 9명까지 초대 가능합니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 초대자 중에 중복되는 참여자가 있습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "중복되는 참여자입니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 초대자 중에 친구가 아닌 유저가 있습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "친구인 유저를 찾을 수 없습니다."
                    }
                    """)
    }))
    ResponseDto<List<MeetingScheduleResponse.GetMonthlyMembersScheduleDto>> getMonthlyParticipantSchedules(
            @Parameter(description = "연도") @RequestParam Integer year,
            @Parameter(description = "월") @RequestParam Integer month,
            @Parameter(description = "초대자 ID 목록") @RequestParam List<Long> participantIds,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "모임 월간 일정 조회", description = "모임에 있는 유저들의 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 유효한 날짜 값을 입력해주세요.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 400,
                    	"message": "유효한 날짜 값을 입력해주세요."
                    }
                    """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 일정을 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "일정을 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 일정을 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "일정 또는 일정의 참여자를 찾을 수 없습니다."
                    }
                    """)
    }))
    ResponseDto<List<MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto>> getMonthlyMeetingParticipantSchedules(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @Parameter(description = "연도") @RequestParam Integer year,
            @Parameter(description = "월") @RequestParam Integer month,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "모임 일정 수정", description = "모임 일정을 수정합니다. 수정 권한은 모임의 방장에게만 있습니다.")
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 유효한 날짜 값을 입력해주세요.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 400,
                    	"message": "유효한 날짜 값을 입력해주세요."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 모임 일정은 최소 1명, 최대 9명까지 초대 가능합니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 400,
                    	"message": "모임 일정은 최소 1명, 최대 9명까지 초대 가능합니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 초대자 중에 중복되는 참여자가 있습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 400,
                    	"message": "중복되는 참여자입니다."
                    }
                    """),}))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 모임 일정의 수정 권한이 업습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 403,
                    	"message": "해당 일정의 생성자가 아닙니다."
                    }
                    """)
    }))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 모임 일정이 아닙니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "모임 일정이 아닙니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 초대자 중에 친구가 아닌 유저가 있습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "친구인 유저를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 입력한 값의 참여자를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "일정의 참여자를 찾을 수 없습니다."
                    }
                    """)
    }))
    ResponseDto<String> updateMeetingSchedule(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @Parameter(description = "수정 요청 dto") @RequestBody @Valid MeetingScheduleRequest.PatchMeetingScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails memberInfo);

    @Operation(summary = "모임 상세 조회", description = "모임 일정을 상세 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 해당 일정에 대한 읽기 권한이 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 403,
                    	"message": "해당 일정의 참여자가 아닙니다."
                    }
                    """)
    }))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 일정을 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "일정을 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 모임 일정이 아닙니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "모임 일정이 아닙니다."
                    }
                    """)
    }))
    ResponseDto<MeetingScheduleResponse.GetMeetingScheduleDto> getMeetingSchedule(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @AuthenticationPrincipal SecurityUserDetails memberInfo);
}
