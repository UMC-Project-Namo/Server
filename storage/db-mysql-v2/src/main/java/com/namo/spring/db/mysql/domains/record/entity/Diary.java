package com.namo.spring.db.mysql.domains.record.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.schedule.entity.Participants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "diary")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Diary extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "participant_id", nullable = false)
	private Participants participants;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "memo", nullable = false, length = 250)
	private String memo;

	public void checkHaveOnlyOneSchedule(boolean meetingSchedule, boolean personalSchedule) {
		if (meetingSchedule && personalSchedule)
			throw new IllegalArgumentException("meetingSchedule과 personalSchedule 중 하나만 null 이어야합니다.");
	}
}
