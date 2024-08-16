package com.namo.spring.application.external.api.individual.service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.PersonalException;
import com.namo.spring.db.mysql.domains.individual.domain.Image;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.individual.repository.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public Image getImage(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new PersonalException(ErrorStatus.NOT_FOUND_IMAGE));
    }

    public Image getImage(String url) {
        return imageRepository.findByImgUrl(url)
                .orElseThrow(() -> new PersonalException(ErrorStatus.NOT_FOUND_IMAGE));
    }

    public List<Image> createImages(List<Image> imgs) {
        return imageRepository.saveAll(imgs);
    }

    public List<Image> getImagesBySchedules(List<Schedule> schedules) {
        return schedules.stream().map(schedule ->
                imageRepository.findAllBySchedule(schedule).get()
        ).flatMap(List::stream).collect(Collectors.toList());
    }

    public void removeImagesBySchedule(Schedule schedule) {
        imageRepository.deleteDiaryImages(schedule);
    }

    public void removeImagesBySchedules(List<Schedule> schedules) {
        schedules.forEach(schedule ->
                imageRepository.deleteAll(schedule.getImages())
        );
    }

    public void removeImage(Long scheduleId, Image img) {
        if (!img.getSchedule().getId().equals(scheduleId)) {
            throw new PersonalException(ErrorStatus.NOT_IMAGE_IN_DIARY);
        }
        imageRepository.delete(img);
    }
}
