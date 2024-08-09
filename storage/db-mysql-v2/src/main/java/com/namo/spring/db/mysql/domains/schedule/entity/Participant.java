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
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "participants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Participant extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 0: 참여자, 1: 주최자
	@Column(name = "is_owner", nullable = false, columnDefinition = "TINYINT")
	private int isOwner;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "schedule_id", nullable = false)
	private Schedule schedule;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "palette_id", nullable = false)
	private Palette palette;

	@Builder
	public Participant(int isOwner, Member member, Schedule schedule, Category category, Palette palette) {
		this.isOwner = Objects.requireNonNull(isOwner, "isOwner은 null일 수 없습니다.");
		this.member = member;
		this.schedule = Objects.requireNonNull(schedule, "schedule은 null일 수 없습니다.");
		this.category = Objects.requireNonNull(category, "category은 null일 수 없습니다.");
		this.palette = Objects.requireNonNull(palette, "palette은 null일 수 없습니다.");
	}

	public Participant of(int isOwner, Member member, Schedule schedule, Category category, Palette palette) {
		return Participant.builder()
			.isOwner(isOwner)
			.member(member)
			.schedule(schedule)
			.category(category)
			.palette(palette)
			.build();
	}
}
