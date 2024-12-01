package com.namo.spring.db.mysql.domains.schedule.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.namo.spring.db.mysql.domains.notification.entity.Notification;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.Period;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Schedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 50)
    private String title;

    @Embedded
    private Period period;

    @Embedded
    private Location location;

    // 0: 개인, 1: 그룹, 2: 생일
    @Column(nullable = false, columnDefinition = "TINYINT")
    private int scheduleType;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String imageUrl;

    @Column(nullable = false)
    private Integer participantCount;

    @Column(nullable = false)
    private String participantNicknames;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activityList = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participantList = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @Builder
    public Schedule(String title, Period period, Location location, int scheduleType, String imageUrl,
            Integer participantCount, String participantNicknames) {
        if (!StringUtils.hasText(title))
            throw new IllegalArgumentException("title은 null이거나 빈 문자열일 수 없습니다.");
        this.title = title;
        this.period = period;
        this.location = location;
        this.scheduleType = scheduleType;
        this.imageUrl = imageUrl;
        this.participantCount = participantCount;
        this.participantNicknames = participantNicknames;
    }

    public static Schedule of(String title, Period period, Location location, int scheduleType, String imageUrl,
            Integer participantCount, String participantNicknames) {
        return Schedule.builder()
                .title(title)
                .period(period)
                .location(location)
                .scheduleType(scheduleType)
                .imageUrl(imageUrl)
                .participantCount(participantCount)
                .participantNicknames(participantNicknames)
                .build();
    }

    public void updateContent(String title, Period period, Location location, String imageUrl) {
        this.title = title;
        this.period = period;
        if (location != null) {
            this.location = location;
        }
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }

    public void setMemberParticipantsInfo(List<String> nicknames) {
        if (nicknames.isEmpty())
            throw new IllegalArgumentException("nickname은 null이거나 빈 list일 수 없습니다.");

        if (this.participantNicknames != null && !this.participantNicknames.isEmpty()) {
            List<String> allNicknames = new ArrayList<>(Arrays.asList(this.participantNicknames.split(", ")));
            allNicknames.addAll(nicknames);
            this.participantNicknames = String.join(", ", allNicknames);
        } else {
            this.participantNicknames = String.join(", ", nicknames);
        }
        this.participantCount += nicknames.size();
    }

    public void setGuestParticipantsInfo(String nickname) {
        this.participantNicknames += ", " + nickname;
        this.participantCount += 1;
    }

    public void updateParticipant(String oldNickname, String newNickname) {
        if (!StringUtils.hasText(oldNickname) || !StringUtils.hasText(newNickname))
            throw new IllegalArgumentException("nickname은 null이거나 빈 문자열일 수 없습니다.");
        if (this.participantNicknames != null) {
            List<String> nicknames = new ArrayList<>(Arrays.asList(this.participantNicknames.split(", ")));
            int index = nicknames.indexOf(oldNickname);
            if (index != -1) {
                nicknames.set(index, newNickname);
                this.participantNicknames = String.join(", ", nicknames);
            }
        }
    }

    public void removeParticipant(String nicknameToRemove) {
        if (nicknameToRemove == null || nicknameToRemove.isEmpty()) {
            throw new IllegalArgumentException("삭제할 닉네임이 비어있거나 null일 수 없습니다.");
        }

        List<String> currentNicknames = new ArrayList<>(Arrays.asList(this.participantNicknames.split(", ")));
        currentNicknames.remove(nicknameToRemove);

        updateParticipants(currentNicknames);
    }

    public void removeParticipants(List<String> nicknamesToRemove) {
        if (nicknamesToRemove == null || nicknamesToRemove.isEmpty()) {
            throw new IllegalArgumentException("삭제할 닉네임 목록이 비어있거나 null일 수 없습니다.");
        }

        List<String> currentNicknames = new ArrayList<>(Arrays.asList(this.participantNicknames.split(", ")));

        for (String nicknameToRemove : nicknamesToRemove) {
            currentNicknames.remove(nicknameToRemove);
        }
        updateParticipants(currentNicknames);
    }

    private void updateParticipants(List<String> updatedNicknames) {
        this.participantNicknames = String.join(", ", updatedNicknames);
        this.participantCount = updatedNicknames.size();
    }

    public boolean getIsMeetingSchedule() {
        return this.scheduleType == ScheduleType.MEETING.getValue();
    }

    public boolean getIsPersonalSchedule(){
        return this.scheduleType == ScheduleType.PERSONAL.getValue();
    }

    public boolean getIsBirthdaySchedule(){
        return this.scheduleType == ScheduleType.BIRTHDAY.getValue();
    }

    public int getStartDayOfMonth() {
        return this.getPeriod().getStartDate().getDayOfMonth();
    }
}
