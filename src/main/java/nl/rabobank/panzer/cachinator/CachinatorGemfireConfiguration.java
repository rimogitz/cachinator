/*
 * CachinatorGemfireConfiguration.java, 28 Sep 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.hazelcast.repository.config.EnableHazelcastRepositories;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.panzer.cachinator.model.GemfirePuko;
import nl.rabobank.panzer.cachinator.model.PukoFactory;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 28 Sep 2018
 *
 */
@Configuration
@Profile(PukoFactory.GEMFIRE)
@EnableGemfireCaching
@EnableGemfireRepositories
@Slf4j
public class CachinatorGemfireConfiguration extends CachingConfigurerSupport {

	@Value("${GEMFIRE_LOCATORS}")
	private String locators;
		
	@Value("${GEMFIRE_PASSWORD}")
	private String password;

	@Bean
	public Properties gemfireProperties() {
		Properties gemfireProperties = new Properties();
		gemfireProperties.setProperty("mcast-port", "0");
		return gemfireProperties;
	}

	@Bean
	public ClientCache clientCache(Properties gemfireProperties) {
		ClientCacheFactory clientCacheFactory = new ClientCacheFactory(gemfireProperties)
                .setPdxSerializer(new ReflectionBasedAutoSerializer(".*"))
                .setPdxReadSerialized(false);

		if (StringUtils.isNotEmpty(locators)) {
			Arrays.asList(locators.split(","))
			      .forEach( locator -> { String[] locatorAddress = locator.split(":");
			                             clientCacheFactory.addPoolLocator(locatorAddress[0], Integer.parseInt(locatorAddress[1]));
			                           });
		}
		
		return clientCacheFactory.create();
	}
	
	@Bean("GemfirePuko")
	public ClientRegionFactoryBean<String, GemfirePuko> gemfirePukoRegion(ClientCache clientCache) {
		ClientRegionFactoryBean<String, GemfirePuko> regionFactory = new ClientRegionFactoryBean<>();
		regionFactory.setCache(clientCache);
		regionFactory.setPool(clientCache.getDefaultPool());
		regionFactory.setName("GemfirePuko");
		regionFactory.setShortcut(ClientRegionShortcut.PROXY);
		return regionFactory;
	}
	
	@Bean("Cache")
	public ClientRegionFactoryBean<String, GemfirePuko> getRegion(ClientCache clientCache) {
		ClientRegionFactoryBean<String, GemfirePuko> regionFactory = new ClientRegionFactoryBean<>();
		regionFactory.setCache(clientCache);
		regionFactory.setPool(clientCache.getDefaultPool());
		regionFactory.setName("Cache");
		regionFactory.setShortcut(ClientRegionShortcut.PROXY);
		return regionFactory;
	}
	
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			
			@Override
			public Object generate(Object target, Method method, Object... params) {
				List<Object> key = new ArrayList<>();
			    key.add(method.getDeclaringClass().getSimpleName());
			    key.add(method.getName());
			    
			    for (final Object o : params) {
			        key.add(o);
			    }
			    return key;
			}
		};
	}
}
