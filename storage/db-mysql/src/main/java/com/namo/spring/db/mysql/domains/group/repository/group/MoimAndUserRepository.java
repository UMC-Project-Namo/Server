package com.namo.spring.db.mysql.domains.group.repository.group;

import com.namo.spring.db.mysql.domains.group.domain.Moim;
import com.namo.spring.db.mysql.domains.group.domain.MoimAndUser;
import com.namo.spring.db.mysql.domains.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MoimAndUserRepository extends JpaRepository<MoimAndUser, Long> {

    @Query(value = "select gu from MoimAndUser gu join fetch gu.moim where gu.user= :user order by gu.createdDate")
    List<MoimAndUser> findMoimAndUserByUser(User user);

    @Query(value = "select gu from MoimAndUser gu join fetch gu.user where gu.moim= :moim")
    List<MoimAndUser> findMoimAndUserByMoim(Moim moim);

    @Query(value = "select gu from MoimAndUser gu join fetch gu.user where gu.moim in :moims")
    List<MoimAndUser> findMoimAndUserByMoim(List<Moim> moims);

    Optional<MoimAndUser> findMoimAndUserByUserAndMoim(User user, Moim moim);

    boolean existsByUserAndMoim(User user, Moim moim);

    void deleteAllByUser(User user);
}
