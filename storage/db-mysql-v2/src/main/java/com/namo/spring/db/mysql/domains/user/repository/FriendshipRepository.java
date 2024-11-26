package com.namo.spring.db.mysql.domains.user.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.namo.spring.db.mysql.domains.user.model.query.FriendBirthdayQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.type.FriendshipStatus;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT DISTINCT f FROM Friendship f JOIN FETCH f.friend WHERE f.member.id = :memberId AND f.friend.id in :members AND f.status = 'ACCEPTED' AND f.member.status = '2'")
    List<Friendship> findAcceptedFriendshipsByMemberIdAndFriendIds(Long memberId, List<Long> members);

    boolean existsByMemberIdAndFriendId(Long memberId, Long friendId);

    Page<Friendship> findAllByFriendIdAndStatus(Long memberId, FriendshipStatus status, Pageable pageable);

    Optional<Friendship> findByIdAndStatus(Long friendshipId, FriendshipStatus status);

    /**
     * startDate부터 endDate까지의 날짜가
     * 생일인 친구의 정보와 생일을 조회합니다.
     * @param memberId
     * @param startDate
     * @param endDate
     * @return 친구 memberId, nickname, birthday
     */
    @Query("SELECT new com.namo.spring.db.mysql.domains.user.model.query.FriendBirthdayQuery(fr.id, fr.nickname, fr.birthday) " +
            "FROM Friendship f JOIN f.friend fr " +
            "WHERE f.member.id = :memberId " +
            "AND fr.birthdayVisible = true " +
            "AND f.status = 'ACCEPTED' " +
            "AND (" +
            "    (FUNCTION('DAYOFYEAR', fr.birthday) >= FUNCTION('DAYOFYEAR', :startDate) " +
            "     AND FUNCTION('DAYOFYEAR', fr.birthday) <= FUNCTION('DAYOFYEAR', :endDate)) " +
            "    OR " +
            "    (FUNCTION('DAYOFYEAR', :startDate) > FUNCTION('DAYOFYEAR', :endDate) " +
            "     AND (FUNCTION('DAYOFYEAR', fr.birthday) >= FUNCTION('DAYOFYEAR', :startDate) " +
            "          OR FUNCTION('DAYOFYEAR', fr.birthday) <= FUNCTION('DAYOFYEAR', :endDate))) " +
            ")")
    List<FriendBirthdayQuery> findBirthdayVisibleFriendIdsByPeriod(
            Long memberId,
            LocalDate startDate,
            LocalDate endDate);


    /**
     * 모든 친구들의 생일을 조회합니다.
     * @param memberId
     * @return 친구 memberId, nickname, birthday
     */
    @Query("SELECT new com.namo.spring.db.mysql.domains.user.model.query.FriendBirthdayQuery(fr.id, fr.nickname, fr.birthday) " +
            "FROM Friendship f JOIN f.friend fr " +
            "WHERE f.member.id = :memberId " +
            "AND fr.birthdayVisible = true " +
            "AND f.status = 'ACCEPTED'")
    List<FriendBirthdayQuery> findBirthdayVisibleFriends(Long memberId);

    Optional<Friendship> findByMemberIdAndFriendIdAndStatus(Long memberId, Long friendId, FriendshipStatus status);

    @Query("SELECT f FROM Friendship f "
            + "JOIN FETCH f.friend "
            + "WHERE f.member.id = :memberId AND f.status = :status")
    Page<Friendship> findAllByMemberIdAndStatusFetchJoin(@Param("memberId") Long memberId, @Param("status") FriendshipStatus status, Pageable pageable);

    @Query("SELECT f FROM Friendship f "
            + "JOIN FETCH f.friend "
            + "WHERE f.member.id = :memberId AND f.status = :status "
            + "AND (f.friend.name LIKE %:search% OR f.friend.nickname LIKE %:search%)")
    Page<Friendship> findAllByMemberIdAndStatusAndSearch(@Param("memberId") Long memberId, @Param("status") FriendshipStatus status,
            @Param("search") String search, Pageable pageable);
}
