package com.namo.spring.application.external.api.record.Controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.application.external.api.record.dto.DiaryResponse;
import com.namo.spring.application.external.api.record.usecase.DiaryUseCase;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;
import com.namo.spring.db.mysql.domains.record.exception.DiaryException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "기록(일기)", description = "기록(일기:diary) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/diaries")
public class DiaryController {

    private final DiaryUseCase diaryUseCase;

    @Operation(summary = "기록 생성", description = "기록(일기) 생성 API 입니다. 개인, 단체 일정 모두 공통으로 사용합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.ALREADY_WRITTEN_DIARY_FAILURE,
            ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE,
    })
    @PostMapping("")
    public ResponseDto<String> createDiary(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @RequestBody DiaryRequest.CreateDiaryDto request
    ) {
        diaryUseCase.createDiary(memberInfo, request);
        return ResponseDto.onSuccess("기록 생성 성공");
    }

    @PatchMapping("/{diaryId}")
    @Operation(summary = "기록 수정", description = "기록(일기) 수정 API 입니다. 모든 이미지 URL을 새로 보내주어야 합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_FOUND_DIARY_FAILURE,
            ErrorStatus.NOT_MY_DIARY_FAILURE,
    })
    public ResponseDto<String> updateDiary(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @PathVariable Long diaryId,
            @RequestBody DiaryRequest.UpdateDiaryDto request
    ) {
        diaryUseCase.updateDiary(diaryId, memberInfo, request);
        return ResponseDto.onSuccess("기록 수정 성공");
    }

    @Operation(summary = "기록 조회", description = "기록(일기) 단일 상세 조회 API 입니다. 개인, 단체 일정 모두 공통으로 사용합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE,
            ErrorStatus.NOT_WRITTEN_DIARY_FAILURE,
    })
    @GetMapping("/{scheduleId}")
    public ResponseDto<DiaryResponse.DiaryDto> getDiary(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @PathVariable Long scheduleId
    ) {
        return ResponseDto.onSuccess(diaryUseCase
                .getScheduleDiary(scheduleId, memberInfo));
    }

    @Operation(summary = "기록 보관함 조회", description = "기록(일기) 보관함 조회 API 입니다. ")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_FILTERTYPE_OF_ARCHIVE,
    })
    @GetMapping("/archive")
    public ResponseDto<List<DiaryResponse.DiaryArchiveDto>> getDiaryArchive(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "ScheduleName(스케쥴 이름 조회) || DiaryContent(일기 내용 조회) || MemberNickname (참여자 조회)")
            @RequestParam(required = false) String filterType,
            @Parameter(description = "각 필터에 맞는 검색 단어를 입력하시면 됩니다.")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "1 부터 시작하는 page입니다. 5개의 일기씩 내림차순 조회됩니다. ")
            @RequestParam int page
    ) {
        return ResponseDto.onSuccess(diaryUseCase
                .getDiaryArchive(memberInfo.getUserId(), page, filterType, keyword));
    }

    @Operation(summary = "기록 캘린더 조회", description = "기록이 존재하는 달력 확인 API 입니다. 월별로 일기가 존재하는 날짜를 반환합니다. ")
    @ApiErrorCodes(value = {
            ErrorStatus.INVALID_FORMAT_FAILURE,
    })
    @GetMapping("/calendar/{yearMonth}")
    public ResponseDto<DiaryResponse.DiaryExistDateDto> getArchiveCalender(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "조회할 달력의 년도-월 입니다.", example = "2024-08")
            @PathVariable String yearMonth
    ) {
        try {
            YearMonth requestedYearMonth = YearMonth.parse(yearMonth);
            return ResponseDto.onSuccess(diaryUseCase
                    .getExistDiaryDate(memberInfo.getUserId(), requestedYearMonth));

        } catch (DateTimeParseException e) {
            throw new DiaryException(ErrorStatus.INVALID_FORMAT_FAILURE);
        }
    }

    @Operation(summary = "날짜별 기록 정보 조회", description = "날짜 별 기록이 존재하는 스케줄 정보가 확인됩니다. 기록 캘린더 조회에서 요일을 선택하여 상세 정보를 확인할 때 사용하시면 됩니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.INVALID_FORMAT_FAILURE,
    })
    @GetMapping("/date/{date}")
    public ResponseDto<List<DiaryResponse.DayOfDiaryDto>> getDayDiary(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "조회할 날짜입니다.", example = "2024-09-01")
            @PathVariable String date) {
        try {
            LocalDate requestedDate = LocalDate.parse(date);
            return ResponseDto.onSuccess(diaryUseCase
                    .getDayDiaryList(memberInfo.getUserId(), requestedDate));
        } catch (DateTimeParseException e) {
            throw new DiaryException(ErrorStatus.INVALID_FORMAT_FAILURE);
        }
    }

    @Operation(summary = "기록 삭제 API", description = "입력받은 기록 ID를 삭제합니다 (본인의 기록만 삭제할 수 있습니다)")
    @DeleteMapping("/{diaryId}")
    public ResponseDto<String> deleteDiary(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "삭제할 기록 ID 입니다.", example = "1")
            @PathVariable Long diaryId
    ){
        diaryUseCase.deleteDiary(memberInfo.getUserId(), diaryId);
        return ResponseDto.onSuccess("기록 삭제에 성공했습니다.");
    }
}
