/*
 * PukoFactory.java, 11 Jul 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

/**
 * ## Prepare for multiple datasources ##
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 11 Jul 2018
 *
 */
@Slf4j
public class PukoFactory {
	
	public static final String REDIS = "redis";
	
	public static final String REDIS_RAW = "redis-raw";
	
	public static final String HAZELCAST = "hazelcast";
	
	public static final String GEMFIRE = "gemfire"; 

	private Constructor<? extends Puko> constructor;
	
	/**
	 * @param redis2
	 */
	private PukoFactory(Class<? extends Puko> pukoClazz) {
		try {
			Constructor<? extends Puko> constructor = pukoClazz.getConstructor(String.class, String.class, Date.class);
			this.constructor = constructor;
		} catch (Exception e) {
			log.error("Error setting up Puko Factory",e);
			throw new RuntimeException(e);
		}
	}

	public Puko newPuko(String key, String value) {
		return newPuko(key, value, null);
	}
	
	public Puko newPuko(String key, String value, Date timestamp) {
		try {
			return constructor.newInstance(key, value, timestamp);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			log.error("Configuration Error: SerializablePuko Class not Available", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param activeProfiles
	 * @return
	 */
	public static PukoFactory getInstance(String[] activeProfiles) {

		if (Arrays.binarySearch(activeProfiles, REDIS) >= 0) {
			return new PukoFactory(RedisPuko.class);
		}

		if (Arrays.binarySearch(activeProfiles, REDIS_RAW) >= 0) {
			return new PukoFactory(RedisPuko.class);
		}

		if (Arrays.binarySearch(activeProfiles, HAZELCAST) >= 0) {
			return new PukoFactory(HazelcastPuko.class);
		}

		if (Arrays.binarySearch(activeProfiles, GEMFIRE) >= 0) {
			return new PukoFactory(GemfirePuko.class);
		}
		
		log.info("No supported profile is active");
		return new PukoFactory(H2Puko.class);
    }
}
