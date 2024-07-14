package com.namo.spring.db.mysql.domains.diary.entity;

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
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;
import com.namo.spring.db.mysql.domains.schedule.entity.PersonalSchedule;

import lombok.AccessLevel;
import lombok.Builder;
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
	@JoinColumn(name = "m_schedule_id", nullable = true)
	private MeetingSchedule meetingSchedule;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "p_schedule_id", nullable = true)
	private PersonalSchedule personalSchedule;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "memo", nullable = false, length = 250)
	private String memo;

	@Builder
	public Diary(MeetingSchedule meetingSchedule, PersonalSchedule personalSchedule, String memo) {
		if (!StringUtils.hasText(memo))
			throw new IllegalArgumentException("memo는 null이거나 빈 문자열일 수 없습니다.");

		checkHaveOnlyOneSchedule(meetingSchedule != null, personalSchedule != null);

		this.meetingSchedule = meetingSchedule;
		this.personalSchedule = personalSchedule;
		this.memo = memo;
	}

	public Diary of(MeetingSchedule meetingSchedule,String memo) {
		return Diary.builder()
				.meetingSchedule(meetingSchedule)
				.memo(memo)
				.build();
	}

	public Diary of(PersonalSchedule personalSchedule,String memo) {
		return Diary.builder()
				.personalSchedule(personalSchedule)
				.memo(memo)
				.build();
	}

	public boolean isMeetingDairy() {
		return this.meetingSchedule != null;
	}

	public boolean isPersonalDiary() {
		return this.personalSchedule != null;
	}

	public void checkHaveOnlyOneSchedule(boolean meetingSchedule, boolean personalSchedule) {
		if (meetingSchedule && personalSchedule)
			throw new IllegalArgumentException("meetingSchedule과 personalSchedule 중 하나만 null 이어야합니다.");
	}
}
