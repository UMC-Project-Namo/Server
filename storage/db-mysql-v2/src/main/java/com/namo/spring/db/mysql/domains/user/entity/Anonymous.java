package com.namo.spring.db.mysql.domains.user.entity;

import static com.namo.spring.db.mysql.domains.user.utils.UserValidationUtils.*;

import com.namo.spring.db.mysql.domains.category.entity.Palette;
import jakarta.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.user.type.Password;
import com.namo.spring.db.mysql.domains.user.utils.UserValidationUtils;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Anonymous extends BaseTimeEntity implements User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String name;

    private Boolean nameVisible;

    @Column(nullable = false, length = 4)
    private String tag;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 50)
    @Embedded
    private Password password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "palette_id")
    private Palette palette;

    private String inviteCode;

    @Builder
    public Anonymous(String name, Boolean nameVisible, String tag,
            String nickname, String password, String inviteCode, Palette palette) {
        this.name = name;
        this.nameVisible = nameVisible;
        this.tag = validateTag(tag);
        this.nickname = validateNickname(nickname);
        this.password = Password.encrypt(password);
        this.palette = palette;
        this.inviteCode = inviteCode;
    }

    public static Anonymous of(String name, Boolean nameVisible, String tag,
            String nickname, String password, String inviteCode, Palette palette) {
        return Anonymous.builder()
                .nickname(nickname)
                .name(name)
                .nameVisible(nameVisible)
                .tag(tag)
                .password(password)
                .palette(palette)
                .inviteCode(inviteCode)
                .build();
    }
}
