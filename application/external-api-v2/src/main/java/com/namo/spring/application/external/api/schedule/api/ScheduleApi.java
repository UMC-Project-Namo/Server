package com.namo.spring.application.external.api.schedule.api;

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
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ScheduleApi {
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
