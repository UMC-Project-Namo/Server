package com.example.namo2.domain.individual.application.converter;

import com.example.namo2.domain.individual.domain.Image;
import com.example.namo2.domain.individual.domain.Schedule;

public class ImageConverter {

	public static Image toImage(String url, Schedule schedule) {
		return Image.builder()
			.imgUrl(url)
			.schedule(schedule)
			.build();
	}
}
