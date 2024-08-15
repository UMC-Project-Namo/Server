package com.namo.spring.db.mysql.domains.user.repository;

import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.type.MemberStatus;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberByEmailAndSocialType(String email, SocialType socialType);

    Optional<Member> findMemberBySocialRefreshToken(String refreshToken);

    @Query("select m from Member m where m.id in :ids")
    List<Member> findMembersById(List<Long> ids);

    @Query("select m from Member m where m.status in :status and m.updatedAt < :localDateTime")
    List<Member> findMembersByStatusAndDate(MemberStatus status, LocalDateTime localDateTime);

}
