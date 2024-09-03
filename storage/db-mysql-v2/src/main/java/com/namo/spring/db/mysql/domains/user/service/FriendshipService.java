package com.namo.spring.db.mysql.domains.user.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;

    public List<Friendship> readFriendshipsByMemberIdAndFriendIds(Long memberId, List<Long> members) {
        return friendshipRepository.findAcceptedFriendshipsByMemberIdAndFriendIds(memberId, members);
    }

    public boolean existsByMemberIdAndFriendId(Long memberId, Long friendId) {
        return friendshipRepository.existsByMemberIdAndFriendId(memberId, friendId);
    }
}
