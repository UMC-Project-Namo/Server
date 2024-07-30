package com.namo.spring.application.external.api.group.api;

import com.namo.spring.application.external.api.group.dto.GroupScheduleRequest;
import com.namo.spring.application.external.api.group.dto.GroupScheduleResponse;
import com.namo.spring.application.external.api.group.dto.MeetingScheduleRequest;
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

import java.util.List;

@Tag(name = "7. Schedule (모임)", description = "모임 일정 관련 API")
public interface MeetingScheduleApi {
    @Operation(summary = "모임 일정 생성", description = "그룹의 모임 일정을 생성합니다. 요청 성공 시 모임 일정 ID를 전송합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 일정 생성 성공", value = """
            {
            	"isSuccess": true,
            	"code": 200,
            	"message": "성공",
            	"result": 1
            }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 잘못된 그룹 ID 입니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "그룹을 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "모임 일정에 참여할 유저가 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저가 그룹에 포함되지 않습니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "유저가 그룹에 포함되어 있지 않습니다."
            }
            """)}))
    ResponseDto<Long> createMeetingSchedule(@AuthenticationPrincipal SecurityUserDetails user, @Valid @RequestBody MeetingScheduleRequest.PostMeetingScheduleDto scheduleReq);

    @Operation(summary = "모임 일정 수정", description = "그룹의 모임 일정을 수정합니다. 그룹에 소속된 사용자라면 누구나 수정 가능합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR, ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 일정 수정 성공", value = """
            {
            	"isSuccess": true,
            	"code": 200,
            	"message": "성공",
            	"result": null
            }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 잘못된 모임 일정 ID 입니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "일정을 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 수정 전의 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "모임 일정에 참여할 유저가 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 모임 일정에 새로 참여할 유저가 그룹에 포함되지 않습니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "유저가 그룹에 포함되어 있지 않습니다."
            }
            """)}))
    ResponseDto<Long> modifyGroupSchedule(@Valid @RequestBody GroupScheduleRequest.PatchGroupScheduleDto scheduleReq);

    @Operation(summary = "모임 일정 카테고리 수정", description = "모임 일정의 카테고리를 수정하여 커스텀합니다. 기존의 모임 일정 카테고리는 변경되지 않습니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR, ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 일정 카테고리 수정 성공", value = """
            {
            	"isSuccess": true,
            	"code": 200,
            	"message": "성공",
            	"result": null
            }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 잘못된 모임 일정 ID 입니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "일정을 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 로그인한 사용자를 찾을 수 없습니다.", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "유저를 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 잘못된 카테고리 ID입니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "카테고리를 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 로그인한 사용자가 모임 일정의 참여자가 아닙니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "모임 일정의 참여자가 아닙니다."
            }
            """)}))
    ResponseDto<Long> modifyGroupScheduleCategory(@Valid @RequestBody GroupScheduleRequest.PatchGroupScheduleCategoryDto scheduleReq, @AuthenticationPrincipal SecurityUserDetails user);

    @Operation(summary = "모임 일정 삭제", description = "모임 일정을 삭제합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 일정 카테고리 수정 성공", value = """
            {
            	"isSuccess": true,
            	"code": 200,
            	"message": "성공",
            	"result": null
            }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 잘못된 모임 일정 ID 입니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "일정을 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 현재 로그인한 사용자를 찾을 수 없습니다.", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "유저를 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 로그인한 사용자가 그룹의 구성원 아닙니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "그룹 구성원이 아닙니다."
            }
            """), @ExampleObject(name = "요청 실패 - 모임 일정에 대한 이미지 파일 삭제 과정에서 오류가 발생하였습니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "파일 삭제 과정에서 오류가 발생하였습니다."
            }
            """)}))
    ResponseDto<Long> removeMeetingSchedule(@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId, @AuthenticationPrincipal SecurityUserDetails user);

    @Operation(summary = "월간 모임 일정 조회", description = "월간 모임 일정을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "월간 모임 일정 조회 성공", value = """
            {
            	"isSuccess": true,
            	"code": 200,
            	"message": "성공",
            	"result": [
                           {
                             "name": "나모 정기 회의",
                             "startDate": 0,
                             "endDate": 0,
                             "interval": 0,
                             "users": [
                               {
                                 "userId": 1,
                                 "userName": "유저 1",
                                 "color": 5
                               },
                               {
                                 "userId": 2,
                                 "userName": "유저 2",
                                 "color": 6
                               }
                             ],
                             "groupId": 1,
                             "moimScheduleId": 1,
                             "x": 0,
                             "y": 0,
                             "locationName": "스타벅스 강남역점",
                             "kakaoLocationId": "string",
                             "hasDiaryPlace": true,
                             "curMoimSchedule": true
                           }
                         ]
            }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 잘못된 그룹 ID 입니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "그룹을 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 로그인 한 유저가 그룹의 구성원이 아닙니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "유저가 그룹에 포함되어 있지 않습니다."
            }
            """),}))
    ResponseDto<List<GroupScheduleResponse.GetMonthlyGroupScheduleDto>> getMonthGroupSchedules(@Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId, @Parameter(description = "조회할 일정의 연도와 월을 {연도},{월} 형식으로 입력합니다.", example = "2024,7") @PathVariable("month") String month, @AuthenticationPrincipal SecurityUserDetails user);

    @Operation(summary = "모든 모임 일정 조회", description = "모든 모임 일정을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 일정 수정 성공", value = """
             {
            	"isSuccess": true,
            	"code": 200,
            	"message": "성공",
            	"result": [
                           {
                             "name": "나모 정기 회의",
                             "startDate": 0,
                             "endDate": 0,
                             "interval": 0,
                             "users": [
                               {
                                 "userId": 1,
                                 "userName": "유저 1",
                                 "color": 5
                               },
                               {
                                 "userId": 2,
                                 "userName": "유저 2",
                                 "color": 6
                               }
                             ],
                             "groupId": 1,
                             "moimScheduleId": 1,
                             "x": 0,
                             "y": 0,
                             "locationName": "스타벅스 강남역점",
                             "kakaoLocationId": "string",
                             "hasDiaryPlace": true,
                             "curMoimSchedule": true
                           }
                         ]
            }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 잘못된 그룹 ID 입니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "그룹을 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 로그인 한 유저가 그룹의 구성원이 아닙니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "유저가 그룹에 포함되어 있지 않습니다."
            }
            """)}))
    ResponseDto<List<GroupScheduleResponse.GetAllGroupScheduleDto>> getAllGroupSchedules(@Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId, @AuthenticationPrincipal SecurityUserDetails user);

    @Operation(summary = "모임 일정 알림 생성", description = "모임 일정에 대한 알림을 생성합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 일정 수정 성공", value = """
            {
            	"isSuccess": true,
            	"code": 200,
            	"message": "성공",
            	"result": null
            }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 잘못된 모임 일정 ID 입니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "일정을 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 로그인한 사용자가 모임 일정의 참여자가 아닙니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "모임 일정의 참여자가 아닙니다."
            }
            """)

    }))
    ResponseDto<Void> createGroupScheduleAlarm(@Valid @RequestBody GroupScheduleRequest.PostGroupScheduleAlarmDto postGroupScheduleAlarmDto, @AuthenticationPrincipal SecurityUserDetails user);

    @Operation(summary = "모임 일정 알림 수정", description = "모임 일정에 대한 알림을 수정합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 일정 알림 수정 성공", value = """
            {
            	"isSuccess": true,
            	"code": 200,
            	"message": "성공",
            	"result": null
            }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 잘못된 모임 일정 ID 입니다. ", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "일정을 찾을 수 없습니다."
            }
            """), @ExampleObject(name = "요청 실패 - 로그인한 사용자가 모임 일정의 참여자가 아닙니다.", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "모임 일정의 참여자가 아닙니다."
            }
            """)}))
    ResponseDto<Void> modifyGroupScheduleAlarm(@Valid @RequestBody GroupScheduleRequest.PostGroupScheduleAlarmDto postGroupScheduleAlarmDto, @AuthenticationPrincipal SecurityUserDetails user);

}
