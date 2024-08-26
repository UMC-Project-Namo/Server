package com.namo.spring.application.external.api.schedule.api;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "모임 일정", description = "모임 일정 관련 API")
public interface MeetingScheduleApi {
    @Operation(summary = "모임 일정 생성", description = "모임 일정을 생성합니다. 요청 성공 시 모임 일정 ID를 전송합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 일정 생성 성공", value = """
            {
            	"isSuccess": true,
            	"code": 200,
            	"message": "성공",
            	"result": 1
            }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
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
            @ExampleObject(name = "요청 실패 - 시작 날짜가 종료 날짜 이전 이어야 합니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "시작 날짜가 종료 날짜 이전 이어야 합니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 중복되는 참여자입니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "중복되는 참여자입니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 친구인 유저를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "친구인 유저를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 모임 일정은 최소 1명, 최대 9명까지 초대 가능합니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "모임 일정은 최소 1명, 최대 9명까지 초대 가능합니다."
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
    ResponseDto<Long> createMeetingSchedule(@Valid @RequestPart ScheduleRequest.PostMeetingScheduleDto dto,
                                            @RequestPart(required = false) MultipartFile image,
                                            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "모임 일정 목록 조회", description = "모임 일정 목록을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 일정 목록 조회 성공", value = """
            {
                "isSuccess": true,
                "code": 200,
                "message": "성공",
                "result": [
                  {
                    "meetingScheduleId": 1,
                    "title": "나모 정기 회의",
                    "startDate": 1722999600,
                    "imageUrl": "https://namo-public-image.s3.ap-northeast-2.amazonaws.com/mongi_512.png",
                    "participantsNum": 9,
                    "participantsNickname": "뚜뚜, 코코아, 다나, 캐슬, 짱구, 연현, 램프, 반디, 유즈"
                  }
                ]
              }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
            {
            	"isSuccess": false,
            	"code": 404,
            	"message": "유저를 찾을 수 없습니다."
            }
            """)
    }))
    ResponseDto<List<MeetingScheduleResponse.GetMeetingScheduleItemDto>> getMyMeetingSchedules(@AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "모임 초대자 월간 일정 조회", description = "모임에 초대할 유저들의 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 초대자 월간 일정 조회 성공", value = """
            {
                "isSuccess": true,
                "code": 200,
                "message": "성공",
                "result": [
                  {
                    "scheduleId": 1,
                    "name": "나모 정기 회의",
                    "startDate": 1722124800,
                    "endDate": 1722124800,
                    "interval": 3,
                    "participants": [
                      {
                        "memberId": 1,
                        "nickname": "다나",
                        "color": 5
                      },
                      {
                        "memberId": 2,
                        "nickname": "테스트",
                        "color": 6
                      }
                    ],
                    "longitude": 0,
                    "latitude": 0,
                    "locationName": "스타벅스 강남역점",
                    "kakaoLocationId": "string"
                  }
                ]
              }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
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
    @ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN, ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "모임 일정 참여자 월간 일정 조회 성공", value = """
            {
                "isSuccess": true,
                "code": 200,
                "message": "성공",
                "result": [
                  {
                    "scheduleId": 1,
                    "name": "나모 정기 회의",
                    "startDate": 1722124800,
                    "endDate": 1722124800,
                    "interval": null,
                    "participants": [
                      {
                        "participantId": 2,
                        "memberId": 1,
                        "anonymousId": null,
                        "nickname": "다나",
                        "color": 5
                      },
                      {
                        "participantId": 3,
                        "memberId": 2,
                        "anonymousId": null,
                        "nickname": "몽이",
                        "color": 6
                      },
                    ],
                    "isCurMeetingSchedule": true,
                    "longitude": 0,
                    "latitude": 0,
                    "locationName": "스타벅스 강남역점",
                    "kakaoLocationId": "string"
                  }
                ]
              }
            """)}))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "요청 실패 - 모임 일정에 참여할 유저를 찾을 수 없습니다.", value = """
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

}
