package com.namo.spring.db.mysql.domains.schedule.entity;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipateStatus;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;
import java.util.Objects;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anonymous_id")
    private Anonymous anonymous;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipateStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "palette_id", nullable = true)
    private Palette palette;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityParticipant> activityParticipants;

    @Builder
    public Participant(int isOwner, User user, Schedule schedule, ParticipateStatus status, Category category, Palette palette) {
        this.isOwner = Objects.requireNonNull(isOwner, "isOwner은 null일 수 없습니다.");
        this.member = user instanceof Member ? (Member) user : null;
        this.anonymous = user instanceof Anonymous ? (Anonymous) user : null;
        this.schedule = Objects.requireNonNull(schedule, "schedule은 null일 수 없습니다.");
        this.status = Objects.requireNonNull(status, "status는 null일 수 없습니다.");
        this.category = category;
        this.palette = palette;
    }

    public static Participant of(int isOwner, User user, Schedule schedule, ParticipateStatus status, Category category, Palette palette) {
        return Participant.builder()
                .isOwner(isOwner)
                .user(user)
                .schedule(schedule)
                .status(status)
                .category(category)
                .palette(palette)
                .build();
    }
}
