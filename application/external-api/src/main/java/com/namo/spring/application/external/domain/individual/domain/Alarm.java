package com.namo.spring.application.external.domain.individual.domain;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.namo.spring.application.external.global.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "alarm")
public class Alarm extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alarm_id")
	private Long id;

	@Column(name = "alarm_date")
	private Integer alarmDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;

	@Builder
	public Alarm(Long id, Integer alarmDate, Schedule schedule) {
		this.id = id;
		this.alarmDate = alarmDate;
		this.schedule = schedule;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Alarm alarm = (Alarm)obj;
		return Objects.equals(getAlarmDate(), alarm.getAlarmDate());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAlarmDate());
	}
}
