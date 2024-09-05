package com.namo.spring.application.external.api.schedule.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
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
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "개인 일정 생성 성공", value = """
            {
            	"isSuccess": true,
            	"code": 200,
            	"message": "성공",
            	"result": 1
            }
            """)}))
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
    ResponseDto<Long> createPersonalSchedule(@Parameter(description = "개인 일정 생성 요청 dto") @Valid @RequestBody ScheduleRequest.PostPersonalScheduleDto dto,
                                             @AuthenticationPrincipal SecurityUserDetails member) throws JsonProcessingException;

    @Operation(summary = "개인 월간 일정 조회", description = "개인 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "개인 일정 조회 성공", value = """
            {
               "isSuccess":true,
               "code":200,
               "message":"성공",
               "result":[
                  {
                     "scheduleId":1,
                     "title":"아르바이트",
                     "category":{
                        "categoryId":1,
                        "colorId":1,
                        "name":"일정"
                     },
                     "startDate":1722124800,
                     "endDate":1724803200,
                     "interval":0,
                     "location":null,
                     "hasDiary":false,
                     "isMeetingSchedule":false,
                     "meetingInfo":null,
                     "notification":null
                  },
                  {
                     "scheduleId":2,
                     "title":"나모 정기 회의",
                     "category":{
                        "categoryId":2,
                        "colorId":4,
                        "name":"모임"
                     },
                     "startDate":1725033600,
                     "endDate":1725062400,
                     "interval":0,
                     "location":null,
                     "hasDiary":false,
                     "isMeetingSchedule":true,
                     "meetingInfo":{
                        "participantCount":9,
                        "participantsNickname":"뚜뚜, 코코아, 다나, 캐슬, 짱구, 연현, 램프, 반디, 유즈",
                        "isOwner":true
                     },
                     "notification":[
                        {
                           "notificationId":1,
                           "notifyDate":1722439801
                        }
                     ]
                  }
               ]
            }
            """)}))
    ResponseDto<List<PersonalScheduleResponse.GetMonthlyScheduleDto>> getMyMonthlySchedules(
            @Parameter(description = "연도") @RequestParam Integer year,
            @Parameter(description = "월") @RequestParam Integer month,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "친구 월간 일정 조회", description = "친구의 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "친구 일정 조회 성공", value = """
            {
                 "isSuccess":true,
                 "code":200,
                 "message":"성공",
                 "result":[
                    {
                       "scheduleId":3,
                       "title":"나모 정기 회의",
                       "category":{
                          "categoryId":8,
                          "colorId":4,
                          "name":"모임",
                          "isShared":true
                       },
                       "startDate":1722124800,
                       "endDate":1724803200,
                       "interval":31
                    },
                    {
                       "scheduleId":4,
                       "title":"약속",
                       "category":{
                          "categoryId":3,
                          "colorId":4,
                          "name":"친구 약속",
                          "isShared":true
                       },
                       "startDate":1722124800,
                       "endDate":1722124800,
                       "interval":0
                    }
                 ]
              }
            """)}))
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
                                                @Parameter(description = "일정 내용 수정 요청 dto") @Valid @RequestBody ScheduleRequest.PatchPersonalScheduleDto dto,
                                                @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "일정 알림 추가/수정/삭제", description = "일정 알림을 추가/수정/삭제 합니다. 최종적으로 일정에 설정할 알림을 array로 전송하며, 알림을 모두 삭제할 때에는 empty array를 전송합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 해당 일정에 대한 읽기 권한이 없습니다.", value = """
            {
            	"isSuccess": false,
            	"code": 403,
            	"message": "해당 일정의 참여자가 아닙니다."
            }
            """)
    }))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 유저의 기기 정보를 찾을 수 없습니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저의 기기 정보를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 유저의 모바일 기기 정보를 찾을 수 없습니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저의 모바일 기기 정보를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 푸쉬 알림 전송이 지원되지 않는 기기입니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "푸쉬 알림 전송이 지원되지 않는 기기입니다."
                    }
                    """)

    }))
    ResponseDto<String> updateScheduleReminder(@Parameter(description = "일정 ID") @PathVariable Long scheduleId,
                                               @Parameter(description = "일정 알림 수정 요청 dto") @Valid @RequestBody ScheduleRequest.PutScheduleReminderDto dto,
                                               @AuthenticationPrincipal SecurityUserDetails member);
}
