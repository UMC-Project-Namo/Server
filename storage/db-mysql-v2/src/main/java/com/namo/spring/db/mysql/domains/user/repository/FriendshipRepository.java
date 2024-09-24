package com.namo.spring.db.mysql.domains.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.type.FriendshipStatus;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT DISTINCT f FROM Friendship f JOIN FETCH f.friend WHERE f.member.id = :memberId AND f.friend.id in :members AND f.status = 'ACCEPTED' AND f.member.status = '2'")
    List<Friendship> findAcceptedFriendshipsByMemberIdAndFriendIds(Long memberId, List<Long> members);

    boolean existsByMemberIdAndFriendId(Long memberId, Long friendId);

    List<Friendship> findAllByFriendIdAndStatus(Long memberId, FriendshipStatus status, Pageable pageable);

    Optional<Friendship> findByIdAndStatus(Long friendshipId, FriendshipStatus status);
}
