package com.namo.spring.application.external.domain.individual.application.converter;

import com.namo.spring.application.external.domain.individual.domain.Image;
import com.namo.spring.application.external.domain.individual.domain.Schedule;

public class ImageConverter {

	public static Image toImage(String url, Schedule schedule) {
		return Image.builder()
			.imgUrl(url)
			.schedule(schedule)
			.build();
	}
}
