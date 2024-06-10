package com.example.namo2.domain.individual.application.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.individual.dao.repository.image.ImageRepository;
import com.example.namo2.domain.individual.domain.Image;
import com.example.namo2.domain.individual.domain.Schedule;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageRepository imageRepository;

	public List<Image> createImgs(List<Image> imgs) {
		return imageRepository.saveAll(imgs);
	}

	public List<Image> getImagesBySchedules(List<Schedule> schedules) {
		return schedules.stream().map(schedule ->
			imageRepository.findAllBySchedule(schedule).get()
		).flatMap(List::stream).collect(Collectors.toList());
	}

	public void removeImgsBySchedule(Schedule schedule) {
		imageRepository.deleteDiaryImages(schedule);
	}

	public void removeImgsBySchedules(List<Schedule> schedules) {
		schedules.forEach(schedule ->
			imageRepository.deleteAll(schedule.getImages())
		);
	}
}
