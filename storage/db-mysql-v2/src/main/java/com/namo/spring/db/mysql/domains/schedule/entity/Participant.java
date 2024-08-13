package com.namo.spring.db.mysql.domains.schedule.entity;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Participant extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 0: 참여자, 1: 주최자
	@Column(nullable = false, columnDefinition = "TINYINT")
	private int isOwner;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "anonymous_id")
	private Anonymous anonymous;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "schedule_id", nullable = false)
	private Schedule schedule;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "palette_id", nullable = false)
	private Palette palette;

	@OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Diary> diaries;

	@OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ActivityParticipant> activityParticipants;

	@Builder
	public Participant(int isOwner, Schedule schedule, Category category, Palette palette, User user) {
		this.isOwner = Objects.requireNonNull(isOwner, "isOwner은 null일 수 없습니다.");
		this.member = user instanceof Member ? (Member)user : null;
		this.anonymous = user instanceof Anonymous ? (Anonymous)user : null;
		this.schedule = Objects.requireNonNull(schedule, "schedule은 null일 수 없습니다.");
		this.category = Objects.requireNonNull(category, "category은 null일 수 없습니다.");
		this.palette = Objects.requireNonNull(palette, "palette은 null일 수 없습니다.");
	}

	public Participant of(int isOwner, User user, Schedule schedule, Category category, Palette palette) {
		return Participant.builder()
			.isOwner(isOwner)
			.user(user)
			.schedule(schedule)
			.category(category)
			.palette(palette)
			.build();
	}
}
