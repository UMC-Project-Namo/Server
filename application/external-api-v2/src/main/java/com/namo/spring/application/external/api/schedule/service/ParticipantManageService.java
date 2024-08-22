package com.namo.spring.application.external.api.schedule.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.type.ColorChip;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.FriendshipService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantManageService {
	private static final Long MEETING_SCHEDULE_OWNER_PALETTE_ID = ColorChip.getMeetingScheduleOwnerPaletteId();
	private final ParticipantMaker participantMaker;
	private final FriendshipService friendshipService;
	private final ParticipantService participantService;

	public void createPersonalScheduleParticipant(Member member, Schedule schedule, Long categoryId) {
		participantMaker.makeScheduleOwner(schedule, member, categoryId, null);
	}

	public void createMeetingScheduleParticipants(Member owner, Schedule schedule, List<Member> participants) {
		participantMaker.makeScheduleOwner(schedule, owner, null, MEETING_SCHEDULE_OWNER_PALETTE_ID);
		schedule.addActiveParticipant(owner.getNickname());
		participants.forEach(participant -> participantMaker.makeMeetingScheduleParticipant(schedule, participant));
	}

	public List<Member> getFriendshipValidatedParticipants(Long ownerId, List<Long> memberIds) {
		List<Member> friends = friendshipService.readFriendshipsByMemberIdAndFriendIds(ownerId, memberIds).stream()
			.map(Friendship::getFriend)
			.collect(Collectors.toList());
		if (memberIds.size() != friends.size()) {
			throw new MemberException(ErrorStatus.NOT_FOUND_FRIENDSHIP_FAILURE);
		}
		return friends;
	}

	public List<Participant> getMeetingScheduleParticipants(Long scheduleId, ParticipantStatus status) {
		List<Participant> participants = participantService.readParticipantsByScheduleIdAndScheduleType(scheduleId,
			ScheduleType.MEETING, status);
		if (participants.isEmpty()) {
			throw new ScheduleException(ErrorStatus.SCHEDULE_PARTICIPANT_IS_EMPTY_ERROR);
		} else
			return participants;
	}

	// =======
	// 	private static final Long MEETING_SCHEDULE_OWNER_PALETTE_ID = ColorChip.getMeetingScheduleOwnerPaletteId();
	// 	private final ParticipantMaker participantMaker;
	// 	private final ParticipantService participantService;
	// 	private final MemberService memberService;
	// 	private final CategoryService categoryService;
	// 	private final PaletteService paletteService;
	//
	public Participant getParticipantForDiary(Long memberId, Long scheduleId) {
		Participant participant = participantService.readParticipants(memberId, scheduleId)
			.orElseThrow(() -> new MemberException(ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE));
		if (participant.isHasDiary())
			throw new MemberException(ErrorStatus.ALREADY_WRITTEN_DIARY_FAILURE);
		return participant;
	}
	//
	// 	@Transactional(readOnly = true)
	// 	public List<Member> getValidatedMeetingParticipants(List<Long> memberIds) {
	// 		validateParticipantNumber(memberIds.size());
	// 		List<Member> participants = memberService.readMembersById(memberIds);
	// 		if (participants.size() != memberIds.size()) {
	// 			throw new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE);
	// 		} else
	// 			return participants;
	// 	}
	//
	// 	@Transactional
	// 	public void createPersonalScheduleParticipant(Member member, Schedule schedule, Long categoryId) {
	// 		Category category = categoryService.readCategoryByMemberAndId(categoryId, member);
	// 		participantMaker.makeScheduleOwner(schedule, member, category, null);
	// 	}
	//
	// 	@Transactional
	// 	public void createMeetingScheduleParticipants(Member scheduleOwner, Schedule schedule, List<Member> participants) {
	// 		Category category = categoryService.readMeetingCategoryByMember(scheduleOwner);
	// 		Palette palette = paletteService.getPalette(MEETING_SCHEDULE_OWNER_PALETTE_ID);
	// 		participantMaker.makeScheduleOwner(schedule, scheduleOwner, category, palette);
	//
	// 		participants.forEach(participant -> participantMaker.makeMeetingScheduleParticipant(schedule, participant));
	// 	}
	// >>>>>>> 735748e8 (:sparkles: Feat: 다이어리 작성을 위한 Participant 조회 메서드 구현 - getParticipantForDiary())

}
