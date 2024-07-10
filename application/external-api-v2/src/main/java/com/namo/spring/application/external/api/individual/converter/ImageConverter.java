package com.namo.spring.application.external.api.individual.converter;

import com.namo.spring.db.mysql.domains.individual.domain.Image;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;

public class ImageConverter {

	public static Image toImage(String url, Schedule schedule) {
		return Image.builder()
			.imgUrl(url)
			.schedule(schedule)
			.build();
	}
}
