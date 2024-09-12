package com.namo.spring.db.mysql.domains.category.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.converter.CategoryStatusConverter;
import com.namo.spring.db.mysql.common.converter.CategoryTypeConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.category.type.CategoryStatus;
import com.namo.spring.db.mysql.domains.category.type.CategoryType;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "palette_id", nullable = false)
    private Palette palette;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 50)
    private String name;

    @Convert(converter = CategoryTypeConverter.class)
    @Column(nullable = false, length = 50)
    private CategoryType type;

    @Column(nullable = false)
    private Integer orderNumber;

    @Column(nullable = false)
    private boolean isShared;

    @Convert(converter = CategoryStatusConverter.class)
    @Column(nullable = false, length = 50)
    private CategoryStatus status;

    @Builder
    public Category(Member member, Palette palette, String name, CategoryStatus status, CategoryType type,
            boolean isShared,
            Integer orderNumber) {
        if (!StringUtils.hasText(name))
            throw new IllegalArgumentException("name는 null이거나 빈 문자열일 수 없습니다.");

        this.member = Objects.requireNonNull(member, "user은 null일 수 없습니다.");
        this.palette = Objects.requireNonNull(palette, "palette은 null일 수 없습니다.");
        this.name = name;
        this.status = Objects.requireNonNull(status, "status은 null일 수 없습니다.");
        this.type = Objects.requireNonNull(type, "type은 null일 수 없습니다.");
        this.orderNumber = Objects.requireNonNull(orderNumber, "orderNumber는 null일 수 없습니다.");
        this.isShared = isShared;
    }
}
