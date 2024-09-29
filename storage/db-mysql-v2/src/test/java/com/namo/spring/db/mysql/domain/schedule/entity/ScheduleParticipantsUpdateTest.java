package com.namo.spring.db.mysql.domain.schedule.entity;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;

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
    void addActiveParticipant_ShouldAddParticipantNicknames() {
        schedule.setMemberParticipantsInfo(List.of("Owner", "Participant1", "Participant2"));
        assertThat(schedule.getParticipantNicknames()).isEqualTo("Owner, Participant1, Participant2");
        assertThat(schedule.getParticipantCount()).isEqualTo(3);
    }

    @Test
    void setParticipant_Nicknames_ShouldThrowException_WhenNicknameIsEmpty() {
        assertThatThrownBy(() -> schedule.setMemberParticipantsInfo(new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("nickname은 null이거나 빈 list일 수 없습니다.");
    }

    @Test
    void updateParticipant_ShouldUpdateNickname() {
        // Given
        schedule.setMemberParticipantsInfo(List.of("Owner", "Participant1", "Participant2"));

        // When
        schedule.updateParticipant("Participant1", "updatedParticipant");

        // Then
        assertThat(schedule.getParticipantNicknames()).isEqualTo("Owner, updatedParticipant, Participant2");
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
        schedule.setMemberParticipantsInfo(List.of("Owner", "Participant1", "Participant2"));

        schedule.removeParticipants(List.of("Participant1", "Participant2"));

        assertThat(schedule.getParticipantNicknames()).isEqualTo("Owner");
        assertThat(schedule.getParticipantCount()).isEqualTo(1);
    }

    @Test
    void removeParticipant_ShouldRemoveParticipant_DuplicateNicknameRequest() {
        schedule.setMemberParticipantsInfo(List.of("Owner", "Participant", "Participant"));

        schedule.removeParticipants(List.of("Participant", "Participant"));

        assertThat(schedule.getParticipantNicknames()).isEqualTo("Owner");
        assertThat(schedule.getParticipantCount()).isEqualTo(1);
    }

    @Test
    void removeParticipant_ShouldThrowException_WhenNicknameIsNull() {
        assertThatThrownBy(() -> schedule.removeParticipants(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제할 닉네임 목록이 비어있거나 null일 수 없습니다.");
    }

    @Test
    void removeParticipant_ShouldThrowException_WhenNicknameIsEmpty() {
        assertThatThrownBy(() -> schedule.removeParticipants(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제할 닉네임 목록이 비어있거나 null일 수 없습니다.");
    }

}
