package com.namo.spring.db.mysql.domains.group.repository;

import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    Optional<GroupUser> findGroupUserByGroupAndUser(Group group, User user);

}
