package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.category.type.ColorChip;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.namo.spring.application.external.global.utils.MeetingValidationUtils.validateParticipantNumber;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantManageService {
    private static final Long MEETING_SCHEDULE_OWNER_PALETTE_ID = ColorChip.getMeetingScheduleOwnerPaletteId();
    private final ParticipantMaker participantMaker;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final PaletteService paletteService;

    @Transactional
    public void createPersonalScheduleParticipant(Member member, Schedule schedule, Long categoryId) {
        Category category = categoryService.readCategoryByMemberAndId(categoryId, member);
        participantMaker.makeScheduleOwner(schedule, member, category, null);
    }

    @Transactional
    public void createMeetingScheduleParticipants(Member scheduleOwner, Schedule schedule, List<Member> participants) {
        Category category = categoryService.readMeetingCategoryByMember(scheduleOwner);
        Palette palette = paletteService.getPalette(MEETING_SCHEDULE_OWNER_PALETTE_ID);
        participantMaker.makeScheduleOwner(schedule, scheduleOwner, category, palette);

        participants.forEach(participant -> participantMaker.makeMeetingScheduleParticipant(schedule, participant));
    }

    @Transactional(readOnly = true)
    public List<Member> getValidatedMeetingParticipants(List<Long> memberIds) {
        validateParticipantNumber(memberIds.size());
        List<Member> participants = memberService.readMembersById(memberIds);
        if (participants.size() != memberIds.size()) {
            throw new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE);
        } else return participants;
    }

}
