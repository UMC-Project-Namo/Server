package com.namo.spring.application.external.domain.group.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.namo.spring.application.external.domain.group.domain.constant.MoimStatus;

import com.namo.spring.application.external.domain.user.domain.User;

import com.namo.spring.application.external.global.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Moim extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "moim_id")
	private Long id;

	private String name;

	@Column(name = "img_url")
	private String imgUrl;

	private String code;

	@Column(name = "member_count")
	private Integer memberCount;

	@Enumerated(EnumType.STRING)
	@Column
	private MoimStatus status;

	@OneToMany(mappedBy = "moim", fetch = FetchType.LAZY)
	private List<MoimAndUser> moimAndUsers = new ArrayList<>();

	@Builder
	public Moim(Long id, String name, String imgUrl) {
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
		this.code = createCode();
		this.memberCount = 0;
		this.status = MoimStatus.ACTIVE;
	}

	private String createCode() {
		return UUID.randomUUID().toString();
	}

	public boolean containUser(User user) {
		for (MoimAndUser moimAndUser : moimAndUsers) {
			if (moimAndUser.getUser().getId() == user.getId()) {
				return true;
			}
		}
		return false;
	}

	public boolean isFull() {
		if (memberCount == 10) {
			return true;
		}
		return false;
	}

	public boolean isLastMember() {
		if (memberCount == 1) {
			return true;
		}
		return false;
	}

	public void addMember(MoimAndUser savedMoimAndUser) {
		moimAndUsers.add(savedMoimAndUser);
		this.memberCount += 1;
	}

	public void removeMember() {
		this.memberCount -= 1;
	}

	public void removeMoim() {
		status = MoimStatus.INACTIVE;
	}
}
