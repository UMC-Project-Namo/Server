package com.namo.spring.db.mysql.domains.record.entity;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 250)
    private String content;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryImage> images;

    private double enjoyRating;

    @Builder
    public Diary(Participant participant, String memo, double enjoyRating) {
        this.participant = Objects.requireNonNull(participant, "participant은 null일 수 없습니다.");
        if (!StringUtils.hasText(memo))
            throw new IllegalArgumentException("memo은 null이거나 빈 문자열일 수 없습니다.");
        this.content = memo;
        this.enjoyRating = enjoyRating;
    }

    public static Diary of(Participant participant, String memo, double enjoyRating) {
        return Diary.builder()
                .participant(participant)
                .memo(memo)
                .enjoyRating(enjoyRating)
                .build();
    }

    @PostPersist
    public void postPersist() {
        participant.diaryCreated();
    }

    @PostRemove
    public void PostRemove() {
        participant.diaryDeleted();
    }

    public void update(String content, double enjoyRating) {
        this.content = content;
        this.enjoyRating = enjoyRating;
    }
}
