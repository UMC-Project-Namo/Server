package com.namo.spring.db.redis.cache.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvitationCodeService {
    private final InvitationCodeRepository invitationCodeRepository;

    public void save(InvitationCode invitationCode) {
        invitationCodeRepository.save(invitationCode);
    }

    public String createInvitationCode(Long scheduleId, long ttl) {
        String inviteCode = generateInviteCode(scheduleId);
        invitationCodeRepository.save(InvitationCode.of(inviteCode, scheduleId, ttl));
        return inviteCode;
    }

    private String generateInviteCode(Long scheduleId) {
        String meetingScheduleId = "MEETING" + ":" + scheduleId;
        return Base64.getUrlEncoder().withoutPadding().encodeToString(meetingScheduleId.getBytes());
    }

    public void delete(InvitationCode invitationCode) {
        invitationCodeRepository.delete(invitationCode);
    }

    public Long getScheduleId(String code) {
        InvitationCode invitationCode = invitationCodeRepository.findById(code).orElseThrow();
        return invitationCode.getScheduleId();
    }
}
