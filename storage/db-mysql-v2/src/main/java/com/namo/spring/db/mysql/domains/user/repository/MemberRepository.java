package com.namo.spring.db.mysql.domains.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.namo.spring.db.mysql.domains.user.model.query.MemberWithFriendshipStatusQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.type.MemberStatus;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberByAuthId(String authId);

    Optional<Member> findMemberByEmailAndSocialType(String email, SocialType socialType);

    Optional<Member> findMemberBySocialRefreshToken(String refreshToken);

    List<Member> findMembersByIdInAndStatusIs(List<Long> id, MemberStatus status);

    @Query("select u from Member u where u.status in :status and u.updatedAt < :localDateTime")
    List<Member> findMembersByStatusAndDate(MemberStatus status, LocalDateTime localDateTime);

    List<Member> findMembersByNickname(String nickname);

    boolean existsByEmailAndSocialType(String email, SocialType socialType);

    Optional<Member> findByIdAndStatus(Long memberId, MemberStatus status);

    Optional<Member> findByNicknameAndTagAndStatus(String nickname, String tag, MemberStatus status);

    @Query("SELECT new com.namo.spring.db.mysql.domains.user.model.query.MemberWithFriendshipStatusQuery(" +
            "m, CASE WHEN f IS NOT NULL THEN f.status ELSE null END) " +
            "FROM Member m " +
            "LEFT JOIN Friendship f ON f.member.id = :memberId AND f.friend.id = :targetId " +
            "WHERE m.id = :targetId")
    MemberWithFriendshipStatusQuery findByMemberIdWithFriendStatus(@Param("memberId") Long memberId, @Param("targetId") Long targetId);

}
