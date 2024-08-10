package com.namo.spring.db.mysql.domains.record.entity;

public interface Image {
	void updateImageOrder(Integer imageOrder);

	void updateImageUrl(String imageUrl);

	void getImageUrl();
}
