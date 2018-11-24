/*
 * CachinatorConfiguration.java, 25 Jun 2018
 * Created by Joao Viegas (Joao.Nascimento@rabobank.nl)
 *
 * Copyright (c)2018, Rabobank Nederland & affiliates.
 */

package nl.rabobank.panzer.cachinator;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.panzer.cachinator.model.PukoFactory;
import nl.rabobank.panzer.cachinator.model.RedisPuko;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Initial Cache configuration with redis
 * 
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 25 Jun 2018
 *
 */
@Configuration
@Profile({PukoFactory.REDIS, PukoFactory.REDIS_RAW})
@EnableRedisRepositories
@Slf4j
public class CachinatorRedisConfiguration {

//	@Value("${REDIS_NODE:redis-11111.rabo-we.redisdemo.com}")
	@Value("${REDIS_NODE}")
	private String redisNode;

//	@Value("${REDIS_PORT:11111}")
	@Value("${REDIS_PORT}")
	private Integer redisPort;

	@Value("${REDIS_PASSWORD:pass}")
	private String password;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		
		
		RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(redisNode, redisPort);
		standaloneConfiguration.setPassword(RedisPassword.of(password));
		
		LettuceClientConfiguration clientConfiguration = 
				LettuceClientConfiguration.builder()
				                          // Add other configuration if required
				                          .build();
				                                 
				                                       

		return new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);
	}

	@Bean
	RedisTemplate<String, RedisPuko> redisTemplate(RedisConnectionFactory rcf) {
		RedisTemplate<String, RedisPuko> redisTemplate = new RedisTemplate<>();
		
		redisTemplate.setConnectionFactory(rcf);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		
		return redisTemplate;
	}
	
	@Bean
	@Primary
	RedisCacheConfiguration redisCacheConfiguration() {
		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
		configuration.serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()));
		configuration.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
		configuration.computePrefixWith(CacheKeyPrefix.simple());
		return configuration;
	}

	@Bean
	@RefreshScope
	RedisCacheManager cacheManager(RedisConnectionFactory rcf, RedisCacheConfiguration defaultCacheConfiguration) {
		RedisCacheManager cacheManager = RedisCacheManager.builder(rcf)
				                                          .cacheDefaults(defaultCacheConfiguration)
				                                          .build();
		log.info("Redis cache manager initialized");
		return cacheManager;
	}

}
