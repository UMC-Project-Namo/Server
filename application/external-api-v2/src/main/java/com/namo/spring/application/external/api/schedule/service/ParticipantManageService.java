package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.category.type.ColorChip;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.FriendshipService;
import com.namo.spring.db.mysql.domains.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantManageService {
    private static final Long MEETING_SCHEDULE_OWNER_PALETTE_ID = ColorChip.getMeetingScheduleOwnerPaletteId();
    private final ParticipantMaker participantMaker;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final PaletteService paletteService;
    private final FriendshipService friendshipService;
    private final ParticipantService participantService;

    public void createPersonalScheduleParticipant(Member member, Schedule schedule, Long categoryId) {
        Category category = categoryService.readCategoryByMemberAndId(categoryId, member);
        participantMaker.makeScheduleOwner(schedule, member, category, null);
    }

    public void createMeetingScheduleParticipants(Member owner, Schedule schedule, List<Member> participants) {
        Category category = categoryService.readMeetingCategoryByMember(owner);
        Palette palette = paletteService.getPalette(MEETING_SCHEDULE_OWNER_PALETTE_ID);
        participantMaker.makeScheduleOwner(schedule, owner, category, palette);
        schedule.addActiveParticipant(owner.getNickname());
        participants.forEach(participant -> participantMaker.makeMeetingScheduleParticipant(schedule, participant));
    }

    public List<Member> getValidatedMeetingParticipants(Member owner, List<Long> memberIds) {
//        List<Member> participants = memberService.readMembersById(memberIds);
//        if (memberIds.size() != participants.size()) {
//            throw new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE);
//        }
        return validateParticipantsAreFriend(owner.getId(), memberIds);
    }

    private List<Member> validateParticipantsAreFriend(Long memberId, List<Long> memberIds) {
        List<Member> friends = friendshipService.readFriendshipsByMember(memberId, memberIds).stream()
                .map(Friendship::getFriend)
                .collect(Collectors.toList());
        if (memberIds.size() != friends.size()) {
            throw new MemberException(ErrorStatus.NOT_FOUND_FRIENDSHIP_FAILURE);
        }
        return friends;
    }

    private void checkMemberIsOwner(Long scheduleId, Long memberId) {
        if (!participantService.existsParticipantByMemberIdAndScheduleId(scheduleId, memberId)) {
            throw new ScheduleException(ErrorStatus.NOT_SCHEDULE_OWNER);
        }
    }

}
