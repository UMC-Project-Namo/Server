package com.namo.spring.db.redis.cache.refresh;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>{
}
