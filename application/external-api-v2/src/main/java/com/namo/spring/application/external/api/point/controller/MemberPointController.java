package com.namo.spring.application.external.api.point.controller;

import static com.namo.spring.core.common.code.status.ErrorStatus.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.point.usecase.PointChargeUseCase;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "11. 포인트 - 유저", description = "포인트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users/points")
public class MemberPointController {

    private final PointChargeUseCase pointChargeUseCase;

    @Operation(summary = "포인트 충전 요청", description = "포인트 충전 요청을 진행합니다.")
    @ApiErrorCodes(value = {
            NOT_FOUND_ACTIVE_USER_FAILURE,
    })
    @PostMapping("/charge/{amount}")
    public ResponseDto<String> requestChargePoints(
            @AuthenticationPrincipal Long memberId,
            @Parameter(description = "충전 요청 포인트 금액", example = "5000")
            @PathVariable Long amount
    ){
        pointChargeUseCase.requestChargePoints(memberId, amount);
        return ResponseDto.onSuccess(amount + "충전 요청 완료");
    }

}
