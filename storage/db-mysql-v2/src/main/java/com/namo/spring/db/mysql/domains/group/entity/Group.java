package com.namo.spring.db.mysql.domains.group.entity;

import com.namo.spring.db.mysql.common.converter.GroupStatusConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.group.type.GroupStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@Table(name = "group_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Group extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "profile_img")
    private String profileImg;

    @Convert(converter = GroupStatusConverter.class)
    @Column(name = "status", nullable = false, length = 50)
    private GroupStatus status;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<GroupUser> groupUsers = new ArrayList<>();

    @Builder
    public Group(String name, String profileImg, GroupStatus status) {
        if (!StringUtils.hasText(name))
            throw new IllegalArgumentException("name은 null이거나 빈 문자열일 수 없습니다.");
        else if (!StringUtils.hasText(code))
            throw new IllegalArgumentException("code는 null이거나 빈 문자열일 수 없습니다.");
        this.name = name;
        this.code = createCode();
        this.profileImg = profileImg;
        this.status = Objects.requireNonNull(status, "status은 null일 수 없습니다.");
    }

    private String createCode() {
        return UUID.randomUUID().toString();
    }
}
