/*
 * CachinatorRedisCustomSaveImpl.java, 16 Oct 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.repository.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rabobank.panzer.cachinator.model.PukoFactory;
import nl.rabobank.panzer.cachinator.model.RedisPuko;
import nl.rabobank.panzer.cachinator.repository.CachinatorRedisCustomOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 16 Oct 2018
 *
 */
@Component
@RequiredArgsConstructor
@Profile(PukoFactory.REDIS_RAW)
public class CachinatorRedisCustomOperationsImpl implements CachinatorRedisCustomOperations<RedisPuko, String> {

    @NonNull
	private RedisTemplate<String, RedisPuko> redisTemplate;

    @Value("${KEY_TTL}")
	private Long TTL;
	
	/**
	 * @see nl.rabobank.panzer.cachinator.repository.CachinatorRedisCustomOperations#save(java.lang.Object)
	 */
	@Override
	public <S extends RedisPuko> S save(S puko) {
//		System.out.println("Called save for id: " + puko.getKey());
		redisTemplate.opsForValue().set(puko.getKey(), puko); //, TTL, TimeUnit.MILLISECONDS);
		
		return puko;
	}

	/**
	 *
	 */
	@Override
	public Optional<RedisPuko> findById(String id) {
//		System.out.println("Called findById for id: " + id);
		return Optional.ofNullable(redisTemplate.opsForValue().get(id));
	}

}
