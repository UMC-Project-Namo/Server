package com.namo.spring.application.external.api.point.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.point.usecase.PointChargeUseCase;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "11. 포인트 - 관리자", description = "포인트 관련 관리자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/points")
public class AdminPointController {

    private final PointChargeUseCase pointChargeUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{pointTransactionId}/accept")
    public ResponseDto<String> acceptChargeRequest(
            @PathVariable Long pointTransactionId
    ) {
        pointChargeUseCase.acceptRequest(pointTransactionId);
        return ResponseDto.onSuccess(pointTransactionId + " 수락 완료");
    }
}
