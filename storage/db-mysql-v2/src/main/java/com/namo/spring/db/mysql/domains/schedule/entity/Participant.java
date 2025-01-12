package com.namo.spring.db.mysql.domains.schedule.entity;

import java.util.Objects;

import com.namo.spring.db.mysql.domains.schedule.type.ParticipantRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 50)
    private String customTitle;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String customImage;

    private boolean hasDiary;

    @OneToOne(mappedBy = "participant", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Diary diary;

    @Builder
    public Participant(int isOwner, User user, Schedule schedule, Category category, String customTitle, String customImage) {
        this.isOwner = Objects.requireNonNull(isOwner, "isOwner은 null일 수 없습니다.");
        this.member = user instanceof Member ? (Member)user : null;
        this.anonymous = user instanceof Anonymous ? (Anonymous)user : null;
        this.schedule = Objects.requireNonNull(schedule, "schedule은 null일 수 없습니다.");
        this.category = category;
        this.hasDiary = false;
        this.customTitle = customTitle;
        this.customImage =customImage;
    }

    public static Participant of(int isOwner, User user, Schedule schedule, Category category, String customTitle, String customImage) {
        return Participant.builder()
                .isOwner(isOwner)
                .user(user)
                .schedule(schedule)
                .category(category)
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

    public Palette getPalette() {
        if(this.getUser() instanceof Member){
            return this.member.getPalette();
        }
        else {
            return this.anonymous.getPalette();
        }
    }

    public void setIsOwner(ParticipantRole role) {
        this.isOwner = role.getValue();
    }

    public void updateCustomScheduleInfo(String title, String imageUrl) {
        this.customTitle = title;
        this.customImage = imageUrl;
    }

    public void diaryCreated() {
        this.hasDiary = true;
    }

    public void diaryDeleted() {
        this.hasDiary = false;
    }

    public String getScheduleTitle(){
        return schedule.getIsMeetingSchedule() ? this.getCustomTitle() : schedule.getTitle();
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

}
