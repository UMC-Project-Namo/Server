package com.namo.spring.application.external.domain.individual.repository.image;

import static com.namo.spring.application.external.domain.individual.domain.QImage.*;

import jakarta.persistence.EntityManager;

import com.namo.spring.application.external.domain.individual.domain.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ImageRepositoryImpl implements ImageRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private final EntityManager em;

	public ImageRepositoryImpl(EntityManager em) {
		queryFactory = new JPAQueryFactory(em);
		this.em = em;
	}

	@Override
	public void deleteDiaryImages(Schedule schedule) {
		queryFactory
			.delete(image)
			.where(image.schedule.eq(schedule))
			.execute();
		em.flush();
		em.clear();
	}
}
