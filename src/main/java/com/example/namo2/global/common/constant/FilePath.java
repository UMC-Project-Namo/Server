package com.example.namo2.global.common.constant;

import lombok.Getter;

@Getter
public enum FilePath {
	GROUP_ACTIVITY_IMG("group/activity/"),
	GROUP_PROFILE_IMG("group/profile/"),
	INVITATION_ACTIVITY_IMG("invitation/activity/");

	private final String path;

	FilePath(String path) {
		this.path = path;
	}

}
