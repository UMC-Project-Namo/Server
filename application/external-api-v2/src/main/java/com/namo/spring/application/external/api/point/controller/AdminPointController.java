package com.namo.spring.application.external.api.point.controller;

import static com.namo.spring.core.common.code.status.ErrorStatus.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.point.dto.PointResponse;
import com.namo.spring.application.external.api.point.usecase.PointChargeUseCase;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "12. 포인트 - 관리자", description = "포인트 관련 관리자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/points/transactions")
public class AdminPointController {

    private final PointChargeUseCase pointChargeUseCase;

    @Operation(summary = "포인트 충전 요청 내역 조회", description = "전체 유저의 포인트 충전 요청 내역을 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseDto<PointResponse.ChargePointRequestListDto> getAllChargeRequests(
            @Parameter(description = "1 부터 시작하는 페이지 번호입니다 (기본값 1)", example = "1")
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        PointResponse.ChargePointRequestListDto transactions = pointChargeUseCase.getAllChargeRequests(page);
        return ResponseDto.onSuccess(transactions);
    }

    @Operation(summary = "유저의 포인트 충전 요청 수락", description = "유저가 요청한 포인트 충전 요청을 수락합니다.")
    @ApiErrorCodes(value = {
            NOT_FOUND_POINT_REQUEST,
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{pointTransactionId}/accept")
    public ResponseDto<String> acceptChargeRequest(
            @Parameter(description = "수락하려는 요청 pointTransactionId", example = "1")
            @PathVariable Long pointTransactionId
    ) {
        pointChargeUseCase.acceptRequest(pointTransactionId);
        return ResponseDto.onSuccess(pointTransactionId + " 수락 완료");
    }
}
