package com.namo.spring.db.mysql.domains.schedule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
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

	// 0: 개인, 1: 그룹
	@Column(nullable = false, columnDefinition = "TINYINT")
	private int scheduleType;

	@Builder
	public Schedule(String title, Period period, Location location, int scheduleType) {
		if (!StringUtils.hasText(title))
			throw new IllegalArgumentException("title은 null이거나 빈 문자열일 수 없습니다.");
		this.title = title;
		this.period = period;
		this.location = location;
		this.scheduleType = scheduleType;
	}

	public Schedule of(String title, Period period, Location location, int scheduleType) {
		return Schedule.builder()
			.title(title)
			.period(period)
			.location(location)
			.scheduleType(scheduleType)
			.build();
	}
}
