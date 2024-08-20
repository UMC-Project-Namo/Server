package com.namo.spring.db.mysql.domain.schedule.entity;

import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ScheduleParticipantsUpdateTest {
    private Schedule schedule;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        schedule = Schedule.builder()
                .title("Test Schedule")
                .period(Period.of(LocalDateTime.now(), LocalDateTime.now()))
                .location(null)
                .scheduleType(ScheduleType.MEETING.getValue())
                .build();
    }

    @Test
    void addActiveParticipant_ShouldAddParticipant() {
        schedule.addActiveParticipant("Owner");

        assertThat(schedule.getParticipantNicknames()).isEqualTo("Owner");
        assertThat(schedule.getParticipantCount()).isEqualTo(1);

        schedule.addActiveParticipant("Participant");

        assertThat(schedule.getParticipantNicknames()).isEqualTo("Owner, Participant");
        assertThat(schedule.getParticipantCount()).isEqualTo(2);
    }

    @Test
    void addActiveParticipant_ShouldThrowException_WhenNicknameIsEmpty() {
        assertThatThrownBy(() -> schedule.addActiveParticipant(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("nickname은 null이거나 빈 문자열일 수 없습니다.");
    }

    @Test
    void updateParticipant_ShouldUpdateNickname() {
        // Given
        schedule.addActiveParticipant("Owner");
        schedule.addActiveParticipant("Participant");

        // When
        schedule.updateParticipant("Owner", "updatedOwner");

        // Then
        assertThat(schedule.getParticipantNicknames()).isEqualTo("updatedOwner, Participant");
    }

    @Test
    void updateParticipant_ShouldThrowException_WhenOldNicknameIsEmpty() {
        assertThatThrownBy(() -> schedule.updateParticipant("", "updatedOwner"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("nickname은 null이거나 빈 문자열일 수 없습니다.");
    }

    @Test
    void updateParticipant_ShouldThrowException_WhenNewNicknameIsEmpty() {
        assertThatThrownBy(() -> schedule.updateParticipant("Owner", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("nickname은 null이거나 빈 문자열일 수 없습니다.");
    }

    @Test
    void removeParticipant_ShouldRemoveParticipant() {
        schedule.addActiveParticipant("Owner");
        schedule.addActiveParticipant("Participant");

        schedule.removeParticipant("Participant");

        assertThat(schedule.getParticipantNicknames()).isEqualTo("Owner");
        assertThat(schedule.getParticipantCount()).isEqualTo(1);
    }

    @Test
    void removeParticipant_ShouldThrowException_WhenNicknameIsEmpty() {
        assertThatThrownBy(() -> schedule.removeParticipant(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("nickname은 null이거나 빈 문자열일 수 없습니다.");
    }

}
