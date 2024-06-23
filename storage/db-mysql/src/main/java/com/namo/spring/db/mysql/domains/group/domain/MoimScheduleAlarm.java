package com.namo.spring.db.mysql.domains.group.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim_schedule_alarm")
@Getter
@NoArgsConstructor
public class MoimScheduleAlarm {
	@Id
	@Column(name = "moim_schedule_alarm_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "alarm_date")
	private Integer alarmDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moim_schedule_user_id")
	private MoimScheduleAndUser moimScheduleAndUser;

	@Builder
	public MoimScheduleAlarm(Long id, Integer alarmDate, MoimScheduleAndUser moimScheduleAndUser) {
		this.id = id;
		this.alarmDate = alarmDate;
		this.moimScheduleAndUser = moimScheduleAndUser;
	}
}
