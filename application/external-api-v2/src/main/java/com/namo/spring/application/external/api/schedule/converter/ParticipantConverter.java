package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantRole;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipateStatus;
import com.namo.spring.db.mysql.domains.user.entity.User;

public class ParticipantConverter {

    private ParticipantConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static Participant toOwnerParticipant(User user, Schedule schedule, Category category, Palette palette) {
        return Participant.builder()
                .isOwner(ParticipantRole.OWNER.getValue())
                .user(user)
                .schedule(schedule)
                .status(ParticipateStatus.ACTIVE)
                .category(category)
                .palette(palette)
                .build();
    }

    public static Participant toNonOwnerParticipant(User user, Schedule schedule) {
        return Participant.builder()
                .isOwner(ParticipantRole.NON_OWNER.getValue())
                .user(user)
                .schedule(schedule)
                .status(ParticipateStatus.INACTIVE)
                .category(null)
                .palette(null)
                .build();
    }
}
