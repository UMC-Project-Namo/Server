package com.namo.spring.application.external.api.record.converter;

import java.util.stream.Collectors;

import com.namo.spring.application.external.api.record.dto.ActivityResponse;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.schedule.type.Location;

public class ActivityResponseConverter {

	public static ActivityResponse.ActivityInfoDto toActivityInfoDto(Activity activity) {
		return ActivityResponse.ActivityInfoDto.builder()
			.activityTitle(activity.getTitle())
			.activityParticipants(
				activity.getParticipants().stream()
					.map(ActivityResponseConverter::toActivityParticipantDto)
					.collect(Collectors.toList())
			)
			.activityStartDate(activity.getSchedule().getPeriod().getStartDate())
			.activityEndDate(activity.getSchedule().getPeriod().getEndDate())
			.activityLocation(toActivityLocationDto(activity.getSchedule().getLocation()))
			.totalAmount(activity.getTotalAmount())
			.tag(activity.getCategoryTag())
			.build();
	}

	public static ActivityResponse.ActivityParticipantDto toActivityParticipantDto(ActivityParticipant participant) {
		return ActivityResponse.ActivityParticipantDto.builder()
			.participantMemberId(participant.getParticipant().getMember().getId())
			.participantNickname(participant.getParticipant().getMember().getNickname())
			.build();
	}

	public static ActivityResponse.ActivityLocationDto toActivityLocationDto(Location location) {
		return ActivityResponse.ActivityLocationDto.builder()
			.kakaoLocationId(location.getKakaoLocationId())
			.locationName(location.getName())
			.build();
	}

}
