package com.example.namo2.domain.group.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.example.namo2.domain.user.domain.User;

import com.example.namo2.global.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim_memo_location_and_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MoimMemoLocationAndUser extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "moim_memo_location_user_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moim_memo_location_id")
	private MoimMemoLocation moimMemoLocation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public MoimMemoLocationAndUser(Long id, MoimMemoLocation moimMemoLocation, User user) {
		this.id = id;
		this.moimMemoLocation = moimMemoLocation;
		this.user = user;
		this.moimMemoLocation.getMoimMemoLocationAndUsers().add(this);
	}
}
