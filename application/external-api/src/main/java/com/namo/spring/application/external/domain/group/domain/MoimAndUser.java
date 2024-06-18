package com.namo.spring.application.external.domain.group.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.namo.spring.application.external.domain.user.domain.User;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim_and_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimAndUser extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "moim_and_user_id")
	private Long id;

	@Column(name = "moim_custom_name")
	private String moimCustomName;

	private Integer color;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moim_id")
	private Moim moim;

	@Builder
	public MoimAndUser(String moimCustomName, Integer color, User user, Moim moim) {
		this.moimCustomName = moimCustomName;
		this.color = color;
		this.user = user;
		this.moim = moim;
	}

	public void updateCustomName(String name) {
		this.moimCustomName = name;
	}
}
