package com.namo.spring.db.mysql.domains.individual.type;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Period {
	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	@Column(name = "day_interval")
	private Integer dayInterval;

	@Builder
	public Period(Long startDate, Long endDate, Integer dayInterval) {
		this.startDate = convertLongToLocalDateTime(startDate);
		this.endDate = convertLongToLocalDateTime(endDate);
		this.dayInterval = dayInterval;
	}

	public LocalDateTime convertLongToLocalDateTime(Long timeStamp) {
		if (timeStamp == 0 || timeStamp == null) {
			return null;
		}
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.systemDefault());
	}
}
