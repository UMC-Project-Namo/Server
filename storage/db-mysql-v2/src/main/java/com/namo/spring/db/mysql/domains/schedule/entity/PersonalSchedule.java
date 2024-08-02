package com.namo.spring.db.mysql.domains.schedule.entity;

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

import org.hibernate.annotations.DynamicInsert;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "personal_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class PersonalSchedule extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private Member user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "schedule_id", nullable = false)
	private Schedule schedule;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Builder
	public PersonalSchedule(Member user, Schedule schedule, Category category) {
		this.user = Objects.requireNonNull(user, "user은 null일 수 없습니다.");
		this.schedule = Objects.requireNonNull(schedule, "schedule은 null일 수 없습니다.");
		this.category = Objects.requireNonNull(category, "category은 null일 수 없습니다.");
	}
}
