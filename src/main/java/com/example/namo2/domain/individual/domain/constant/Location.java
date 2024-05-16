package com.example.namo2.domain.individual.domain.constant;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {
	private Double x;
	private Double y;
	private String locationName;
	private String kakaoLocationId;

	@Builder
	public Location(Double x, Double y, String locationName, String kakaoLocationId) {
		this.x = x;
		this.y = y;
		this.locationName = locationName;
		this.kakaoLocationId = kakaoLocationId;
	}

	public static Location create(Double x, Double y, String locationName, String kakaoLocationId) {
		return new Location(x, y, locationName, kakaoLocationId);
	}
}
