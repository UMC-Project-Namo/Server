package com.namo.spring.db.mysql.domains.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.hibernate.annotations.DynamicInsert;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.shop.enums.ThemeType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Theme extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 테마 이름

    private String description; // 테마 설명

    @Column(nullable = false)
    private Integer price; // 테마 가격

    private String previewImageUrl; // 미리보기 이미지 URL

    @Column(nullable = false)
    private String detailImageUrl; // 상세 이미지 URL

    @Enumerated(EnumType.STRING)
    private ThemeType type; // 테마 유형 (배경 테마, 프로필 테마 등)

    @Builder
    public Theme(String name, String description, Integer price, String previewImageUrl, String detailImageUrl, ThemeType type) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.previewImageUrl = previewImageUrl;
        this.detailImageUrl = detailImageUrl;
        this.type = type;
    }


}
