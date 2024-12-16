package com.namo.spring.application.external.api.point.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.point.usecase.PointChargeUseCase;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "11. 포인트 ", description = "포인트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users/points")
public class PointController {

    private final PointChargeUseCase pointChargeUseCase;

    @PostMapping("/charge/{amount}")
    public ResponseDto<String> requestChargePoints(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long amount
    ){
        pointChargeUseCase.requestChargePoints(memberId, amount);
        return ResponseDto.onSuccess(amount + "충전 요청 완료");
    }
}
