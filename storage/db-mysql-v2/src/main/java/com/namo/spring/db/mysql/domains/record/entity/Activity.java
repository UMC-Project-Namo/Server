package com.namo.spring.db.mysql.domains.record.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Location;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Activity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Schedule schedule;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 50)
    private String categoryTag;

    @Embedded
    private Location location;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityParticipant> activityParticipants;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityImage> activityImages;

    @Builder
    public Activity(Schedule schedule, String title, String categoryTag, Location location, LocalDateTime startDate, LocalDateTime endDate) {
        if (!StringUtils.hasText(title))
            throw new IllegalArgumentException("title은 null이거나 빈 문자열일 수 없습니다.");
        this.schedule = Objects.requireNonNull(schedule, "schedule은 null일 수 없습니다.");
        this.title = title;
        this.totalAmount = BigDecimal.ZERO;
        this.categoryTag = categoryTag;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setSettlementInfo(BigDecimal totalAmount){
        this.totalAmount = totalAmount;
    }

    public void updateInfo(String title, LocalDateTime activityStartDate, LocalDateTime activityEndDate) {
        this.title = title;
        this.startDate = activityStartDate;
        this.endDate = activityEndDate;
    }

    public void updateLocation(Location location){
        this.location = location;
    }

    public void updateTag(String tag) {
        this.categoryTag = tag;
    }
}
