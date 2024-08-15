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
import java.util.List;

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

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<Participant> participantList = new ArrayList<>();

    @Builder
    public Schedule(String title, Period period, Location location, int scheduleType, String imageUrl) {
        if (!StringUtils.hasText(title))
            throw new IllegalArgumentException("title은 null이거나 빈 문자열일 수 없습니다.");
        this.title = title;
        this.period = period;
        this.location = location;
        this.scheduleType = scheduleType;
        this.imageUrl = imageUrl;
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
}
