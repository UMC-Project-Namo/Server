package com.namo.spring.db.mysql.domains.user.repository;

import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT DISTINCT f FROM Friendship f JOIN FETCH f.friend WHERE f.member.id = :memberId AND f.status = 'ACCEPTED' AND f.friend.id in :members AND f.member.status = '2'")
    List<Friendship> findAcceptedFriendshipsByMembeIdAndFriendIds(Long memberId, List<Long> members);
}
