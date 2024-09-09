package com.namo.spring.application.external.api.schedule.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleResponse;
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
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Tag(name = "개인 일정", description = "개인 일정 관련 API")
public interface PersonalScheduleApi {
    @Operation(summary = "개인 일정 생성", description = "개인 일정을 생성합니다. 요청 성공 시 개인 일정 ID를 전송합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 시작 날짜가 종료 날짜 이전 이어야 합니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 400,
            	"message": "시작 날짜가 종료 날짜 이전 이어야 합니다."
            }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 유저를 찾을 수 없습니다.", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "유저를 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 카테고리를 찾을 수 없습니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "카테고리를 찾을 수 없습니다."
            }
            """)}))
    ResponseDto<Long> createPersonalSchedule(@Parameter(description = "개인 일정 생성 요청 dto") @Valid @RequestBody PersonalScheduleRequest.PostPersonalScheduleDto dto,
                                             @AuthenticationPrincipal SecurityUserDetails member) throws JsonProcessingException;

    @Operation(summary = "개인 월간 일정 조회", description = "개인 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    ResponseDto<List<PersonalScheduleResponse.GetMonthlyScheduleDto>> getMyMonthlySchedules(
            @Parameter(description = "연도") @RequestParam Integer year,
            @Parameter(description = "월") @RequestParam Integer month,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "친구 월간 일정 조회", description = "친구의 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 요청한 회원과 친구가 아닙니다.", value = """
            {
            	"isSuccess": false,
            	"code": 403,
            	"message": "요청한 회원과 친구가 아닙니다."
            }
            """)}))
    ResponseDto<List<PersonalScheduleResponse.GetFriendMonthlyScheduleDto>> getFriendMonthlySchedules(
            @Parameter(description = "연도") @RequestParam Integer year,
            @Parameter(description = "월") @RequestParam Integer month,
            @Parameter(description = "친구 유저 ID") @RequestParam Long memberId,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "개인 일정 내용 수정", description = "개인 일정 내용을 수정합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 시작 날짜가 종료 날짜 이전 이어야 합니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 400,
            	"message": "개인 일정이 아닙니다."
            }
            """),
            @ExampleObject(name = "요청 실패 - 시작 날짜가 종료 날짜 이전 이어야 합니다. ", value = """
                    {
                    "isSuccess": false,
                    "code": 400,
                    "message": "시작 날짜가 종료 날짜 이전 이어야 합니다."
                    }
                    """)
    }))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 모임 일정의 수정 권한이 업습니다.", value = """
            {
            	"isSuccess": false,
            	"code": 403,
            	"message": "해당 일정의 생성자가 아닙니다."
            }
            """)
    }))
    ResponseDto<String> updatePersonalSchedules(@Parameter(description = "일정 ID") @PathVariable Long scheduleId,
                                                @Parameter(description = "일정 내용 수정 요청 dto") @Valid @RequestBody PersonalScheduleRequest.PatchPersonalScheduleDto dto,
                                                @AuthenticationPrincipal SecurityUserDetails member);
}
