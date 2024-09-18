package com.namo.spring.db.mysql.domains.user.repository;

import java.time.LocalDate;
import java.util.List;

import com.namo.spring.db.mysql.domains.user.dto.FriendBirthdayQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.namo.spring.db.mysql.domains.user.entity.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT DISTINCT f FROM Friendship f JOIN FETCH f.friend WHERE f.member.id = :memberId AND f.friend.id in :members AND f.status = 'ACCEPTED' AND f.member.status = '2'")
    List<Friendship> findAcceptedFriendshipsByMemberIdAndFriendIds(Long memberId, List<Long> members);

    boolean existsByMemberIdAndFriendId(Long memberId, Long friendId);

    /**
     * startDate부터 endDate까지의 날짜가
     * 생일인 친구의 정보와 생일을 조회합니다.
     * @param memberId
     * @param startDate
     * @param endDate
     * @return 친구 memberId, nickname, birthday
     */
    @Query("SELECT new com.namo.spring.db.mysql.domains.user.dto.FriendBirthdayQuery(fr.id, fr.nickname, fr.birthday) " +
            "FROM Friendship f JOIN f.friend fr " +
            "WHERE f.member.id = :memberId " +
            "AND fr.birthdayVisible = true " +
            "AND f.status = 'ACCEPTED' " +
            "AND (" +
            "    (FUNCTION('MONTH', fr.birthday) > FUNCTION('MONTH', :startDate) " +
            "     OR (FUNCTION('MONTH', fr.birthday) = FUNCTION('MONTH', :startDate) " +
            "         AND FUNCTION('DAY', fr.birthday) >= FUNCTION('DAY', :startDate))) " +
            "    AND " +
            "    (FUNCTION('MONTH', fr.birthday) < FUNCTION('MONTH', :endDate) " +
            "     OR (FUNCTION('MONTH', fr.birthday) = FUNCTION('MONTH', :endDate) " +
            "         AND FUNCTION('DAY', fr.birthday) <= FUNCTION('DAY', :endDate))) " +
            ")")
    List<FriendBirthdayQuery> findBirthdayVisibleFriendIdsByPeriod(
            Long memberId,
            LocalDate startDate,
            LocalDate endDate);

}
