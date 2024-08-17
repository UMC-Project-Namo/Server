package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantManageService {
    private final ParticipantMaker participantMaker;
    private final MemberService memberService;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public Member getMember(Long memberId) {
        return memberService.readMember(memberId).orElseThrow(
                () -> new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE)
        );
    }

    @Transactional
    public void createPersonalScheduleParticipant(Long memberId, Schedule schedule, Long categoryId) {
        Member member = getMember(memberId);
        Category category = categoryService.readCategoryByMemberAndId(categoryId, member);
        participantMaker.makePersonalScheduleOwner(schedule, member, category);
    }

    @Transactional
    public void createMeetingScheduleParticipants(Long memberId, Schedule schedule, List<Long> members) {
        Member member = getMember(memberId);
        List<Member> participants = getValidatedMeetingParticipants(members);
        Category category = categoryService.readMeetingCategoryByMember(member);
        participantMaker.makeMeetingScheduleOwner(schedule, member, category);

        for (Member participant : participants) {
            participantMaker.makeMeetingScheduleParticipant(schedule, participant);
        }
    }

    private List<Member> getValidatedMeetingParticipants(List<Long> participantIds) {
        List<Member> participants = memberService.readMembersById(participantIds);
        if (participants.size() != participantIds.size()) {
            throw new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE);
        } else return participants;
    }

}
