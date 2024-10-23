package com.namo.spring.db.mysql.domains.user.entity;

import static com.namo.spring.db.mysql.domains.user.type.FriendshipStatus.*;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.user.type.FriendshipStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Friendship extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private Member friend;

    private boolean isFavorite;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    @Builder
    public Friendship(Member member, Member friend) {
        this.member = member;
        this.friend = friend;
        this.status = PENDING;
        this.isFavorite = false;
    }

    public void toggleFavorite() {
        this.isFavorite = !isFavorite;
    }

    public void accept() {
        status = ACCEPTED;
    }
    public void reject() {
        status = REJECTED;
    }
}
