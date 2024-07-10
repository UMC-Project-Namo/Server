package com.namo.spring.application.external.api.individual.controller;

import com.namo.spring.application.external.api.individual.dto.DiaryResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.application.external.global.utils.Converter;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "4. Diary (개인)", description = "개인 기록 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/diaries")
public class DiaryController {
    private final Converter converter;

    @Operation(summary = "기록 생성", description = "기록 생성 API")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<DiaryResponse.ScheduleIdDto> createDiary(
            @Parameter(description = "기록용 이미지") @RequestPart(required = false) List<MultipartFile> imgs,
            @Parameter(description = "일정 ID") @RequestPart String scheduleId,
            @Parameter(description = "기록 내용") @RequestPart(required = false) String content
    ) {
        return null;
    }

    @Operation(summary = "일정 기록 월간 조회", description = "일정 기록 월간 조회 API")
    @GetMapping("/month/{month}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<DiaryResponse.SliceDiaryDto> findDiaryByMonth(
            @Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
            Pageable pageable,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "개인 일정 기록 전체 조회", description = "개인 일정 기록 전체 조회 API")
    @GetMapping("/all")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<List<DiaryResponse.GetDiaryByUserDto>> findAllDiary(
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    //일정 별 기록 조회 == 1개 조회
    @Operation(summary = "일정 기록 개별 조회", description = "일정 기록 개별 조회 API")
    @GetMapping("/{scheduleId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<DiaryResponse.GetDiaryByScheduleDto> findDiaryById(
            @Parameter(description = "일정 ID") @PathVariable("scheduleId") Long scheduleId
    ) {
        return null;
    }

    @Operation(summary = "일정 기록 수정", description = "일정 기록 수정 API")
    @PatchMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<String> updateDiary(
            @Parameter(description = "기록 이미지") @RequestPart(required = false) List<MultipartFile> imgs,
            @Parameter(description = "일정 ID") @RequestPart String scheduleId,
            @Parameter(description = "기록 내용") @RequestPart(required = false) String content
    ) {
        return null;
    }

    @Operation(summary = "일정 기록 삭제", description = "일정 기록 삭제 API")
    @DeleteMapping("/{scheduleId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<String> deleteDiary(
            @Parameter(description = "일정 ID") @PathVariable("scheduleId") Long scheduleId
    ) {
        return null;
    }
}
