package com.namo.spring.application.external.api.record.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

import com.namo.spring.application.external.api.record.dto.ActivityResponse;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityImage;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.schedule.type.Location;

public class ActivityResponseConverter {

    public static ActivityResponse.ActivityInfoDto toActivityInfoDto(Activity activity) {
        return ActivityResponse.ActivityInfoDto.builder()
                .activityId(activity.getId())
                .activityTitle(activity.getTitle())
                .activityParticipants(
                        activity.getActivityParticipants().stream()
                                .map(ActivityResponseConverter::toActivityParticipantDto)
                                .collect(Collectors.toList())
                )
                .activityStartDate(activity.getStartDate())
                .activityEndDate(activity.getEndDate())
                .activityLocation(toActivityLocationDto(activity.getSchedule().getLocation()))
                .totalAmount(activity.getTotalAmount())
                .activityImages(activity.getActivityImages().stream()
                        .map(ActivityResponseConverter::toActivityImageDto)
                        .toList())
                .tag(activity.getCategoryTag())
                .build();
    }

    public static ActivityResponse.ActivityParticipantDto toActivityParticipantDto(ActivityParticipant participant) {
        return ActivityResponse.ActivityParticipantDto.builder()
                .participantId(participant.getParticipant().getId())
                .activityParticipantId(participant.getId())
                .participantNickname(participant.getParticipant().getMember().getNickname())
                .build();
    }

    public static ActivityResponse.ActivityLocationDto toActivityLocationDto(Location location) {
        return ActivityResponse.ActivityLocationDto.builder()
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .kakaoLocationId(location.getKakaoLocationId())
                .locationName(location.getName())
                .build();
    }

    public static ActivityResponse.ActivitySettlementInfoDto toActivitySettlementInfoDto(Activity activity) {
        long divisionCount = activity.getActivityParticipants().stream()
                .filter(ActivityParticipant::isIncludedInSettlement)
                .count();
        BigDecimal amountPerPerson = BigDecimal.ZERO;
        if (divisionCount > 0) {
            amountPerPerson = activity.getTotalAmount()
                    .divide(BigDecimal.valueOf(divisionCount), RoundingMode.HALF_UP);
        }

        return ActivityResponse.ActivitySettlementInfoDto.builder()
                .totalAmount(activity.getTotalAmount())
                .divisionCount((int)divisionCount)
                .amountPerPerson(amountPerPerson)
                .participants(activity.getActivityParticipants().stream()
                        .map(ActivityResponseConverter::toActivitySettlementParticipant)
                        .toList())
                .build();
    }

    public static ActivityResponse.ActivitySettlementParticipant toActivitySettlementParticipant(
            ActivityParticipant activityParticipant) {
        return ActivityResponse.ActivitySettlementParticipant.builder()
                .activityParticipantId(activityParticipant.getId())
                .participantNickname(activityParticipant.getParticipant().getMember().getNickname())
                .isIncludedInSettlement(activityParticipant.isIncludedInSettlement())
                .build();
    }

    public static ActivityResponse.ActivityImageDto toActivityImageDto(ActivityImage image){
        return ActivityResponse.ActivityImageDto.builder()
                .activityImageId(image.getId())
                .imageUrl(image.getImageUrl())
                .orderNumber(image.getImageOrder())
                .build();
    }

}
