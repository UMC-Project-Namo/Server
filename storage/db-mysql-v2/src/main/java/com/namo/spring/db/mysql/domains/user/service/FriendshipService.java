package com.namo.spring.db.mysql.domains.user.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;

    public List<Friendship> readFriendshipsByMember(Member member, List<Long> members) {
        return friendshipRepository.findFriendshipsByMember(member, members);
    }
}