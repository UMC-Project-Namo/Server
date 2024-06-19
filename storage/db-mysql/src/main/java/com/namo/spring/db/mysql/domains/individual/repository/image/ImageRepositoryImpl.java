package com.namo.spring.db.mysql.domains.individual.repository.image;

import jakarta.persistence.EntityManager;

import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.namo.spring.db.mysql.domains.individual.domain.QImage.image;

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
