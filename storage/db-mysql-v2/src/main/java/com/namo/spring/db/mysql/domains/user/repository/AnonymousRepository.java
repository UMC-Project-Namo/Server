package com.namo.spring.db.mysql.domains.user.repository;

import com.namo.spring.db.mysql.domains.user.dto.AnonymousInviteCodeQuery;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnonymousRepository extends JpaRepository<Anonymous, Long> {

    List<Anonymous> findAnonymousByNickname(String nickname);

    Optional<Anonymous> findAnonymousByInviteCode(String inviteCode);

    Optional<Anonymous> findAnonymousByTagAndNickname(String tag, String nickname);

    @Query("SELECT new com.namo.spring.db.mysql.domains.user.dto.AnonymousInviteCodeQuery(a.inviteCode) FROM Anonymous a")
    List<AnonymousInviteCodeQuery> findAllInviteCodes();
}
