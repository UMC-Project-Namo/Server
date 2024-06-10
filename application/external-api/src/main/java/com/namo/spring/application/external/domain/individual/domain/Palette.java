package com.example.namo2.domain.individual.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Palette {
	@Id
	@Column(name = "palette_id")
	private Long id;

	@Column(nullable = false, length = 10)
	private String belong;

	@Column(nullable = false)
	private Integer color;

	@Builder
	public Palette(Long id, String belong, Integer color) {
		this.id = id;
		this.belong = belong;
		this.color = color;
	}
}
