package com.namo.spring.application.external.api.guest.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.namo.spring.application.external.global.config.properties.WebUrlConfig;
import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.guest.dto.GuestParticipantRequest;
import com.namo.spring.application.external.api.schedule.service.ParticipantMaker;
import com.namo.spring.application.external.api.user.service.TagGenerator;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.exception.PaletteException;
import com.namo.spring.db.mysql.domains.category.type.PaletteEnum;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ParticipantException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import com.namo.spring.db.mysql.domains.user.dto.AnonymousInviteCodeQuery;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import com.namo.spring.db.mysql.domains.user.exception.AnonymousException;
import com.namo.spring.db.mysql.domains.user.service.AnonymousService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestManageService {
    private static final long[] PALETTE_IDS = PaletteEnum.getBasicColorIds();
    private final AnonymousService anonymousService;
    private final ParticipantService participantService;
    private final ParticipantMaker participantMaker;
    private final TagGenerator tagGenerator;
    private final WebUrlConfig webUrlConfig;

    public Anonymous createAnonymous(GuestParticipantRequest.PostGuestParticipantDto dto, Schedule schedule,
            String code, String tag) {
        Anonymous anonymous = Anonymous.of(null, null, tag, dto.getNickname(), dto.getPassword(), code);
        return anonymousService.createAnonymous(anonymous);
    }

    private Participant createGuest(GuestParticipantRequest.PostGuestParticipantDto dto, Schedule schedule,
            String code) {
        String tag = tagGenerator.generateTag(dto.getNickname());
        Anonymous anonymous = createAnonymous(dto, schedule, code, tag);
        Long paletteId = selectPaletteColorId(schedule.getId());
        return participantMaker.makeGuestParticipant(schedule, anonymous, paletteId);
    }

    public Participant getAnonymousParticipant(Long anonymousId, Long scheduleId) {
        return participantService.readAnonymousParticipant(anonymousId, scheduleId)
                .orElseThrow(() -> new ParticipantException(ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE));
    }

    public Anonymous getAnonymousByTagAndNickname(String tag, String nickname) {
        return anonymousService.readAnonymousByTagAndNickname(tag, nickname)
                .orElseThrow(() -> new AnonymousException(ErrorStatus.NOT_FOUND_USER_FAILURE));
    }

    /**
     * 게스트 유저가 모임 일정에서 표시될 고유 색상을 부여합니다.
     */
    private Long selectPaletteColorId(Long scheduleId) {
        List<Long> participantsColors = participantService.readParticipantsByScheduleIdAndScheduleType(scheduleId,
                        ScheduleType.MEETING)
                .stream().map(Participant::getPalette).map(Palette::getId).collect(Collectors.toList());
        return Arrays.stream(PALETTE_IDS)
                .filter((color) -> !participantsColors.contains(color))
                .findFirst()
                .orElseThrow(() -> new PaletteException(ErrorStatus.NOT_FOUND_COLOR));
    }

    private Participant getValidatedGuest(Anonymous anonymous, GuestParticipantRequest.PostGuestParticipantDto dto,
            Long scheduleId) {
        if (anonymous.getNickname().equals(dto.getNickname()) && anonymous.getPassword()
                .isSamePassword(dto.getPassword())) {
            return getAnonymousParticipant(anonymous.getId(), scheduleId);
        } else
            throw new AnonymousException(ErrorStatus.ANONYMOUS_LOGIN_FAILURE);
    }

    /**
     * 참여 코드에 대한 anonymous 유저가 존재할 시 로그인을,
     * 없을 시에는 비회원 정보를 저장하여
     * 해당 비회원에 대한 Participant를 반환합니다.
     */
    public Participant createOrValidateGuest(GuestParticipantRequest.PostGuestParticipantDto dto, Schedule schedule,
            String code) {
        return anonymousService.readAnonymousByInviteCode(code)
                .map(anonymous -> getValidatedGuest(anonymous, dto, schedule.getId()))
                .orElseGet(() -> createGuest(dto, schedule, code));
    }

    public String generateInvitationUrl(Long scheduleId){
        return webUrlConfig.getInvitation()+generateInviteCode(scheduleId);
    }

    /**
     * 일정 ID 값을 담은 참여 코드를 생성합니다.
     *
     * @param scheduleId
     * @return 참여 코드
     */
    private String generateInviteCode(Long scheduleId) {
        List<String> existCodes = anonymousService.readAllInviteCodes()
                .stream()
                .map(AnonymousInviteCodeQuery::getCode)
                .toList();
        String inviteCode;
        do {
            String uuidPart = UUID.randomUUID().toString().substring(0, 8);
            String rawCode = scheduleId + ":" + uuidPart;
            inviteCode = Base64.getUrlEncoder().withoutPadding().encodeToString(rawCode.getBytes());
        } while (existCodes.contains(inviteCode));
        return inviteCode;
    }

    /**
     * 참여 코드를 디코딩하여 일정 ID를 추출합니다,.
     *
     * @param inviteCode
     * @return scheduleId
     */
    public Long decodeInviteCode(String inviteCode) {
        try {
            // Base64 디코딩
            byte[] decodedBytes = Base64.getUrlDecoder().decode(inviteCode);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

            // 디코딩된 문자열 파싱
            String[] parts = decodedString.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid invite code format");
            }

            return Long.parseLong(parts[0]);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid invite code", e);
        }
    }

}
