package com.example.namo2.domain.group.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.example.namo2.global.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim_memo_location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MoimMemoLocation extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "moim_memo_location_id")
	private Long id;

	private String name;

	@Column(name = "total_amount")
	private Integer totalAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moim_memo_id")
	private MoimMemo moimMemo;

	@OneToMany(mappedBy = "moimMemoLocation", fetch = FetchType.LAZY)
	private List<MoimMemoLocationAndUser> moimMemoLocationAndUsers = new ArrayList<>();

	@OneToMany(mappedBy = "moimMemoLocation", fetch = FetchType.LAZY)
	private List<MoimMemoLocationImg> moimMemoLocationImgs = new ArrayList<>();

	@Builder
	public MoimMemoLocation(Long id, String name, Integer totalAmount, MoimMemo moimMemo) {
		this.id = id;
		this.name = name;
		this.totalAmount = totalAmount;
		this.moimMemo = moimMemo;
	}

	public void update(String name, Integer totalAmount) {
		this.name = name;
		this.totalAmount = totalAmount;
	}
}
