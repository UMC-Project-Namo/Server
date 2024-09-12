package com.namo.spring.db.mysql.domains.schedule.type;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Column(name = "longitude")
    private Double longitude; // 카카오맵 좌표계 상의 x 좌표

    @Column(name = "latitude")
    private Double latitude; // 카카오맵 좌표계 상의 y 좌표

    @Column(name = "location_name")
    private String name;

    @Column(name = "kakao_location_id")
    private String kakaoLocationId;

    @Builder
    public Location(Double longitude, Double latitude, String name, String kakaoLocationId) {
        checkAllHaveOrAllNull(
                longitude != null,
                latitude != null,
                name != null,
                kakaoLocationId != null
        );

        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.kakaoLocationId = kakaoLocationId;
    }

    private void checkAllHaveOrAllNull(boolean longitude, boolean latitude, boolean name, boolean kakaoLocationId) {
        if (!checkAllHave(longitude, latitude, name, kakaoLocationId) &&
                !checkAllNull(longitude, latitude, name, kakaoLocationId)) {
            throw new IllegalArgumentException("Location의 필드는 모두 null이거나 모두 null이 아니어야 합니다.");
        }
    }

    private boolean checkAllHave(boolean longitude, boolean latitude, boolean name, boolean kakaoLocationId) {
        return longitude && latitude && name && kakaoLocationId;
    }

    private boolean checkAllNull(boolean longitude, boolean latitude, boolean name, boolean kakaoLocationId) {
        return !longitude && !latitude && !name && !kakaoLocationId;
    }

    public static Location of(Double longitude, Double latitude, String name, String kakaoLocationId) {
        return new Location(longitude, latitude, name, kakaoLocationId);
    }
}
