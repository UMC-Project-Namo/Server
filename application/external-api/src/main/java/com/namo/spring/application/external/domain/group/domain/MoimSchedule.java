package com.namo.spring.application.external.domain.group.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.namo.spring.application.external.domain.group.domain.constant.MoimScheduleStatus;

import com.namo.spring.application.external.domain.individual.domain.constant.Location;
import com.namo.spring.application.external.domain.individual.domain.constant.Period;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim_schedule")
@Getter
@NoArgsConstructor
public class MoimSchedule extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "moim_schedule_id")
	private Long id;

	private String name;

	@Embedded
	Period period;

	@Embedded
	private Location location;

	@Enumerated(EnumType.STRING)
	private MoimScheduleStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moim_id")
	private Moim moim;

	@OneToMany(mappedBy = "moimSchedule", fetch = FetchType.LAZY)
	private List<MoimScheduleAndUser> moimScheduleAndUsers = new ArrayList<>();

	@OneToOne(mappedBy = "moimSchedule", fetch = FetchType.LAZY)
	private MoimMemo moimMemo;

	@Builder
	public MoimSchedule(Long id, String name, Period period, Location location, Moim moim) {
		this.id = id;
		this.name = name;
		this.period = period;
		this.location = location;
		this.moim = moim;
		status = MoimScheduleStatus.ACTIVE;
	}

	public void registerMemo(MoimMemo moimMemo) {
		this.moimMemo = moimMemo;
	}

	public void update(String name, Period period, Location location) {
		this.name = name;
		this.period = period;
		this.location = location;
	}

	public Boolean isLastScheduleMember() {
		return moimScheduleAndUsers.size() == 1;
	}

	public void deleteMoimSchedule() {
		this.status = MoimScheduleStatus.DELETED;
	}

}
