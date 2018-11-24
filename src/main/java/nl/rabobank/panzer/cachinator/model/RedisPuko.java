/*
 * RedisPuko.java, 11 Jul 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 11 Jul 2018
 *
 */
@Getter
@Setter
@RedisHash("puko")
@AllArgsConstructor
public class RedisPuko implements SerializablePuko {

	/**
	 * 
	 */
	private static final long serialVersionUID = 177799797604709200L;

	public RedisPuko () {

	}

	@Id
	private String key;
	
	private String value;
	
	private Date timestamp;

}
