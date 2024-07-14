package com.namo.spring.db.mysql.domains.schedule.type;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.namo.spring.core.common.utils.DateUtil;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {

	@JdbcTypeCode(SqlTypes.TIMESTAMP)
	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@JdbcTypeCode(SqlTypes.TIMESTAMP)
	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	@Builder
	public Period(LocalDateTime startDate, LocalDateTime endDate) {
		this.startDate = Objects.requireNonNull(startDate, "startDate은 null일 수 없습니다.");
		this.endDate = Objects.requireNonNull(endDate, "endDate은 null일 수 없습니다.");
	}

	public static Period of(LocalDateTime startDate, LocalDateTime endDate) {
		return new Period(startDate, endDate);
	}

	public static Period of(Long startDate, Long endDate) {
		return new Period(
			DateUtil.toLocalDateTime(startDate),
			DateUtil.toLocalDateTime(endDate)
		);
	}
}
