package com.namo.spring.db.mysql.domains.diary.entity;

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
import com.namo.spring.db.mysql.domains.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "activity_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class ActivityUser extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "activity_id", nullable = false)
	private Activity activity;

	@Builder
	public ActivityUser(User user, Activity activity) {
		this.user = Objects.requireNonNull(user, "user은 null일 수 없습니다.");
		this.activity = Objects.requireNonNull(activity, "activity은 null일 수 없습니다.");
	}

	public ActivityUser of(User user, Activity activity) {
		return ActivityUser.builder()
				.user(user)
				.activity(activity)
				.build();
	}
}
