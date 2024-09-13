package com.namo.spring.db.mysql.domains.schedule.entity;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Participant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 0: 참여자, 1: 주최자
    @Column(nullable = false, columnDefinition = "TINYINT")
    private int isOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anonymous_id")
    private Anonymous anonymous;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "palette_id", nullable = true)
    private Palette palette;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 50)
    private String customTitle;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String customImage;

    private boolean hasDiary;

    @OneToOne(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Diary diary;

    @Builder
    public Participant(int isOwner, User user, Schedule schedule, ParticipantStatus status, Category category,
            Palette palette, String customTitle, String customImage) {
        this.isOwner = Objects.requireNonNull(isOwner, "isOwner은 null일 수 없습니다.");
        this.member = user instanceof Member ? (Member)user : null;
        this.anonymous = user instanceof Anonymous ? (Anonymous)user : null;
        this.schedule = Objects.requireNonNull(schedule, "schedule은 null일 수 없습니다.");
        this.status = Objects.requireNonNull(status, "status는 null일 수 없습니다.");
        this.category = category;
        this.palette = palette;
        this.hasDiary = false;
        this.customTitle = customTitle;
        this.customImage =customImage;
    }

    public static Participant of(int isOwner, User user, Schedule schedule, ParticipantStatus status, Category category,
            Palette palette, String customTitle, String customImage) {
        return Participant.builder()
                .isOwner(isOwner)
                .user(user)
                .schedule(schedule)
                .status(status)
                .category(category)
                .palette(palette)
                .customTitle(customTitle)
                .customImage(customImage)
                .build();
    }

    public User getUser() {
        if (this.member != null) {
            return this.member;
        } else if (this.anonymous != null) {
            return this.anonymous;
        }
        return null;
    }

    public void activateStatus(Category category, Palette palette) {
        this.status = ParticipantStatus.ACTIVE;
        this.category = category;
        this.palette = palette;
        this.customTitle = schedule.getTitle();
        this.customImage = schedule.getImageUrl();
    }

    public void updateCustomScheduleInfo(String title, String imageUrl) {
        this.customTitle = title;
        this.customImage = imageUrl;
    }

    public void inactiveStatus() {
        this.status = ParticipantStatus.INACTIVE;
    }

    public void diaryCreated() {
        this.hasDiary = true;
    }

    public void diaryDeleted() {
        this.hasDiary = false;
    }

}
