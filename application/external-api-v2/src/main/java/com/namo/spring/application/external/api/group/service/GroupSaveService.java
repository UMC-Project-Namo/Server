package com.namo.spring.application.external.api.group.service;

import com.namo.spring.core.infra.common.aws.s3.FileUtils;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.core.infra.common.properties.ImageUrlProperties;
import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.group.service.GroupService;
import com.namo.spring.db.mysql.domains.group.service.GroupUserService;
import com.namo.spring.db.mysql.domains.group.type.GroupStatus;
import com.namo.spring.db.mysql.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.namo.spring.application.external.api.group.converter.GroupConverter.toGroup;
import static com.namo.spring.application.external.api.group.converter.GroupUserConverter.toGroupUser;
import static com.namo.spring.db.mysql.domains.category.type.ColorGroup.getPaletteColors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupSaveService {

    private final GroupService groupService;
    private final GroupUserService groupUserService;
    private final FileUtils fileUtils;
    private final ImageUrlProperties imageUrlProperties;

    @Transactional
    public Long createGroup(User user, String groupName, MultipartFile img) {
        String profileImg = imageUrlProperties.getGroup();
        if (img != null && !img.isEmpty()) {
            profileImg = fileUtils.uploadImage(img, FilePath.GROUP_PROFILE_IMG);
        }
        Group group = groupService.createGroup(toGroup(groupName, profileImg, GroupStatus.ACTIVE));
        GroupUser groupUser = toGroupUser(user, group, getPaletteColors()[0].getCode());
        groupUserService.createGroupUser(groupUser);
        return group.getId();
    }
}
