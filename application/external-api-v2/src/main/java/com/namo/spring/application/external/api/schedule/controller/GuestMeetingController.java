package com.namo.spring.application.external.api.schedule.controller;

import com.namo.spring.application.external.api.schedule.dto.GuestParticipantRequest;
import com.namo.spring.application.external.api.schedule.dto.GuestParticipantResponse;
import com.namo.spring.application.external.api.schedule.usecase.GuestMeetingUsecase;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게스트", description = "게스트 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/guests")
@PreAuthorize("isAnonymous()")
public class GuestMeetingController {
    private final GuestMeetingUsecase guestMeetingUsecase;

    /**
     * 게스트 로그인 / 모임 가입 API
     */
    @PostMapping(path = "/invitations")
    public ResponseDto<GuestParticipantResponse.PostGuestParticipantDto> guest(
            @RequestParam String code,
            @Valid @RequestBody GuestParticipantRequest.PostGuestParticipantDto dto) {
        return ResponseDto.onSuccess(guestMeetingUsecase.createOrValidateGuestParticipant(dto, code));
    }
}
