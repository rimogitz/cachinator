/*
 * CachinatorHazelcastConfiguration.java, 24 Sep 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.hazelcast.repository.config.EnableHazelcastRepositories;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.panzer.cachinator.model.PukoFactory;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 24 Sep 2018
 *
 */
@Configuration
@Profile(PukoFactory.HAZELCAST)
@EnableHazelcastRepositories
@Slf4j
public class CachinatorHazelcastConfiguration extends CachingConfigurerSupport {

	@Value("${CLUSTER_MEMBERS}")
	private String clusterMembers;
	
	@Value("${GROUP_NAME}")
	private String groupName;
	
	@Value("${GROUP_PASSWORD}")
	private String groupPassword;
	
	@Bean
	public ClientConfig clientConfig() {
		log.info("Hazelcast Configuration {} [{}:{}]", clusterMembers, groupName, groupPassword);
		
		ClientConfig config = new ClientConfig();
		
		if (StringUtils.isNotEmpty(clusterMembers)) {
			
			List<String> clusterMembersList = Arrays.asList(clusterMembers.split(","));
			ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
			clientNetworkConfig.setAddresses(clusterMembersList);
			clientNetworkConfig.setSmartRouting(true);
			config.setNetworkConfig(clientNetworkConfig);

			log.info("Connecting to cluster members : {}", clusterMembersList);
		} else {
			log.warn("NO Cluster Members Defined !!!");
		}

		if (StringUtils.isNotEmpty(groupName) && StringUtils.isNotEmpty(groupPassword)) {
			GroupConfig groupConfig = new GroupConfig();
			
			groupConfig.setName(groupName);
			groupConfig.setPassword(groupPassword);
			config.setGroupConfig(groupConfig);
			
			log.info("Connecting to group {}", groupName);
		} else {
			log.warn("NO Credentials provided for group");
		}
		
		return config;
	}
	
	@Bean
	public HazelcastInstance hazelcastInstance(ClientConfig config) {
		HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(config);
		
		return hazelcastInstance;
	}
	
	@Bean
    public CacheManager cacheManager(HazelcastInstance client) {
        HazelcastCacheManager hazelcastCacheManager = new HazelcastCacheManager(client);
        
        log.info("Hazelcast cache manager initialized");
        
        return hazelcastCacheManager;
    }

}
