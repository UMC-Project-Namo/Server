package com.namo.spring.application.external.api.group.service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GroupException;
import com.namo.spring.db.mysql.domains.group.domain.Moim;
import com.namo.spring.db.mysql.domains.group.domain.MoimAndUser;
import com.namo.spring.db.mysql.domains.group.repository.group.MoimAndUserRepository;
import com.namo.spring.db.mysql.domains.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupAndUserService {
    private final MoimAndUserRepository groupAndUserRepository;

    public MoimAndUser createGroupAndUser(MoimAndUser groupAndUser, Moim group) {
        validateExistsGroupAndUser(group, groupAndUser);
        validateGroupIsFull(group);
        MoimAndUser savedGroupAndUser = groupAndUserRepository.save(groupAndUser);
        group.addMember(savedGroupAndUser);
        return savedGroupAndUser;
    }

    private void validateExistsGroupAndUser(Moim group, MoimAndUser groupAndUser) {
        if (group.containUser(groupAndUser.getUser())) {
            throw new GroupException(ErrorStatus.DUPLICATE_PARTICIPATE_FAILURE);
        }
    }

    private void validateGroupIsFull(Moim group) {
        if (group.isFull()) {
            throw new GroupException(ErrorStatus.INVALID_MEETING_PARTICIPANT_COUNT);
        }
    }

    public List<MoimAndUser> getGroupAndUsers(User user) {
        return groupAndUserRepository.findMoimAndUserByUser(user);
    }

    public List<MoimAndUser> getGroupAndUsers(Moim group) {
        return groupAndUserRepository.findMoimAndUserByMoim(group);
    }

    public List<MoimAndUser> getGroupAndUsers(List<Moim> groups) {
        return groupAndUserRepository.findMoimAndUserByMoim(groups);
    }

    public MoimAndUser getGroupAndUser(Moim group, User user) {
        return groupAndUserRepository.findMoimAndUserByUserAndMoim(user, group)
                .orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_GROUP_AND_USER_FAILURE));
    }

    public void existsGroupAndUser(Moim group, User user) {
        if (!groupAndUserRepository.existsByUserAndMoim(user, group))
            throw new GroupException(ErrorStatus.NOT_FOUND_GROUP_AND_USER_FAILURE);
    }

    public void removeGroupAndUser(MoimAndUser groupAndUser, Moim group) {
        validateNotExistsGroupAndUser(group, groupAndUser.getUser());
        if (group.isLastMember()) {
            group.removeMoim();
        }
        group.removeMember();
        groupAndUserRepository.delete(groupAndUser);
    }

    private void validateNotExistsGroupAndUser(Moim group, User user) {
        if (!group.containUser(user)) {
            throw new GroupException(ErrorStatus.NOT_INCLUDE_GROUP_USER);
        }
    }

    public void removeGroupAndUsersByUser(User user) {
        groupAndUserRepository.deleteAllByUser(user);
    }
}
