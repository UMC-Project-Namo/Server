package com.namo.spring.db.mysql.domains.schedule.entity;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    // 0: 개인, 1: 그룹
    @Column(nullable = false, columnDefinition = "TINYINT")
    private int scheduleType;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String imageUrl;

    private Integer participantCount;

    private String participantNicknames;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<Participant> participantList = new ArrayList<>();

    @Builder
    public Schedule(String title, Period period, Location location, int scheduleType, String imageUrl,
                    Integer participantCount, String participantNicknames) {
        if (!StringUtils.hasText(title))
            throw new IllegalArgumentException("title은 null이거나 빈 문자열일 수 없습니다.");
        this.title = Objects.requireNonNull(title, "title은 null일 수 없습니다.");
        ;
        this.period = Objects.requireNonNull(period, "period는 null일 수 없습니다.");
        ;
        this.location = location;
        this.scheduleType = Objects.requireNonNull(scheduleType, "scheduleType은 null일 수 없습니다.");
        ;
        this.imageUrl = Objects.requireNonNull(imageUrl, "imageUrl은 null일 수 없습니다.");
        ;
        this.participantCount = participantCount;
        this.participantNicknames = participantNicknames;
    }

    public static Schedule of(String title, Period period, Location location, int scheduleType, String imageUrl) {
        return Schedule.builder()
                .title(title)
                .period(period)
                .location(location)
                .scheduleType(scheduleType)
                .imageUrl(imageUrl)
                .build();
    }

    public void updateContent(String title, Period period, Location location) {
        this.title = title;
        this.period = period;
        if (location != null) {
            this.location = location;
        }
    }

    public void addActiveParticipant(String nickname) {
        if (!StringUtils.hasText(nickname))
            throw new IllegalArgumentException("nickname은 null이거나 빈 문자열일 수 없습니다.");
        if (this.participantNicknames == null || this.participantNicknames == "") {
            this.participantNicknames = nickname;
        } else {
            this.participantNicknames += ", " + nickname;
        }
        if (this.participantCount == null) {
            this.participantCount = 1;
        } else {
            this.participantCount++;
        }
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

    // 단일 참가자 제거
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
            currentNicknames.remove(nicknameToRemove); // 첫 번째 일치하는 닉네임만 제거
        }

        updateParticipants(currentNicknames);
    }

    private void updateParticipants(List<String> updatedNicknames) {
        this.participantNicknames = String.join(", ", updatedNicknames);
        this.participantCount = updatedNicknames.size();
    }

}
