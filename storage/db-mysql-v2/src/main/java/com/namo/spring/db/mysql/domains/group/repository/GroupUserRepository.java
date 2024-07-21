package com.namo.spring.db.mysql.domains.group.repository;

import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    Optional<GroupUser> findGroupUserByGroupAndUser(Group group, User user);

    @Query("SELECT gu FROM GroupUser gu JOIN FETCH gu.group WHERE gu.user = :user AND gu.group.status = '1' ORDER BY gu.createdAt")
    List<GroupUser> findGroupUsersWithGroupByUser(@Param("user") User user);

    @Query(value = "SELECT gu FROM GroupUser gu JOIN FETCH gu.user JOIN FETCH gu.group WHERE gu.group in :groups AND gu.group.status = '1'")
    List<GroupUser> findGroupUsersByGroups(@Param("groups") List<Group> groups);

    @Query("SELECT gu FROM GroupUser gu JOIN FETCH gu.group WHERE gu.group = :group AND gu.group.status = '1'")
    List<GroupUser> findGroupUsersByGroup(@Param("group") Group group);

    boolean existsGroupUserByGroupAndUser(Group group, User user);

}
