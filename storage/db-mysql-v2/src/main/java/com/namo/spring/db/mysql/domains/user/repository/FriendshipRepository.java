package com.namo.spring.db.mysql.domains.user.repository;

import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT DISTINCT f FROM Friendship f JOIN FETCH f.member JOIN FETCH f.friend WHERE f.member = :member AND f.status = 'ACCEPTED' AND f.friend.id in :members")
    List<Friendship> findFriendshipsByMember(Member member, List<Long> members);
}
