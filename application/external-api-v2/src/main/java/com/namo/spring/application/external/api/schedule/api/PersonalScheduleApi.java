package com.namo.spring.application.external.api.schedule.api;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;


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
            """),
            @ExampleObject(name = "요청 실패 - 시작 날짜가 종료 날짜 이전 이어야 합니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "시작 날짜가 종료 날짜 이전 이어야 합니다."
                    }
                    """)}))
    ResponseDto<Long> createPersonalSchedule(@Valid @RequestBody ScheduleRequest.PostPersonalScheduleDto dto,
                                             @AuthenticationPrincipal SecurityUserDetails member);

}