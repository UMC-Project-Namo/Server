package com.namo.spring.db.mysql.domains.user.entity;

import com.namo.spring.db.mysql.common.converter.MemberStatusConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.user.type.MemberRole;
import com.namo.spring.db.mysql.domains.user.type.MemberStatus;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW(), status = 'INACVTIVE' WHERE id = ?")
public class Member extends BaseTimeEntity implements User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true)
    private String authId;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 50)
    private String email;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 50)
    private String name;

    @Column(nullable = false)
    private boolean nameVisible;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 50)
    private String nickname;

    @Column(length = 4)
    private String tag;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 10)
    private String birthday;  // "MM-DD"

    @Column(nullable = false)
    private boolean birthdayVisible;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false)
    private String socialRefreshToken;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Convert(converter = MemberStatusConverter.class)
    @Column(nullable = false, length = 50)
    private MemberStatus status;

    @ColumnDefault("NULL")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "palette_id", nullable = false)
    private Palette palette;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Friendship> friendships = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> devices;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;

    @Builder
    public Member(String name, String tag, String birthday, String authId, String email,
                  SocialType socialType, String socialRefreshToken) {
        this.name = name;
        this.nameVisible = true;
        this.tag = tag;
        this.email = email;
        this.authId = authId;
        this.birthday = birthday;
        this.birthdayVisible = true;
        this.memberRole = MemberRole.USER;
        this.status = MemberStatus.PENDING;
        this.socialType = socialType;
        this.socialRefreshToken = socialRefreshToken;
    }

    @Override
    public String toString() {
        return "Member ["
                + "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", userRole='" + memberRole + '\'' +
                ", palette_id='" + palette.getId() + '\'' +
                ", status='" + status + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                ']';
    }

    public void changeToActive() {
        this.status = MemberStatus.ACTIVE;
    }

    public void changeToInactive() {
        this.status = MemberStatus.INACTIVE;
    }

    public void updateSocialRefreshToken(String socialRefreshToken) {
        this.socialRefreshToken = socialRefreshToken;
    }

    public boolean isSignUpComplete() {
        return !status.equals(MemberStatus.PENDING);
    }

    public void signUpComplete(String name, String nickname, String birthday, String bio, String tag, Palette palette) {
        this.name = name;
        this.nickname = nickname;
        this.birthday = birthday;
        this.bio = bio;
        this.tag = tag;
        this.palette = palette;
        this.status = MemberStatus.ACTIVE;
    }
}
