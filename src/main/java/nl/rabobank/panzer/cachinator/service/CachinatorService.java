/*
 * CachinatorService.java, 2 Jul 2018
 * Created by Joao Viegas (Joao.Nascimento@rabobank.nl)
 *
 * Copyright (c)2018, Rabobank Nederland & affiliates.
 */

package nl.rabobank.panzer.cachinator.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rabobank.panzer.cachinator.NotAuthorizedException;
import nl.rabobank.panzer.cachinator.model.*;
import nl.rabobank.panzer.cachinator.repository.CachinatorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 2 Jul 2018
 *
 */
@Service
@CacheConfig(cacheNames = "service")
@RequiredArgsConstructor
public class CachinatorService implements ApplicationListener<ContextRefreshedEvent> {

	private RestTemplate template = new RestTemplate();
	
	private CachinatorRepository<Puko> repository;

	@NonNull
	private PukoFactory pukoFactory;

	@Value("${RABOPCF_LOCALDOMAIN:apps.pcf-t01-we.rabobank.nl}")
	private String domain;
	
	@Cacheable("Cache")
	public CurrentTime getTime() {
		return getTime(false);
	}
	
	@Cacheable("Cache")
	public CurrentTime getTime(boolean authenticated) {
		return template.getForObject("http://wiremock." + domain + "/currentTime", CurrentTime.class);
	}
	
	public AuthenticationToken login(Authentication authentication) {
		// create a new login stub
		String username = authentication.getUsername();
		String password = authentication.getPassword();
		
		String tokenValue = UUID.nameUUIDFromBytes((username + password + System.currentTimeMillis()).getBytes()).toString();
		
		AuthenticationToken token = new AuthenticationToken(username,tokenValue); 
				
		String loginStub;
		try {
			loginStub = MessageFormat.format( new String(Files.readAllBytes(Paths.get(getClass().getResource("/loggedInStub.json").toURI())))
					                               , token.getUsername()
					                               , token.getToken());
		} catch (Exception e) {
			// Just throw it... it's not supposed to happen
			throw new RuntimeException(e);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(loginStub, headers);
		template.postForObject("http://wiremock." + domain + "/__admin/mappings", request, String.class);
				
		return token;
	}
	
	public boolean isLoggedIn(String username, String token) {
		try {
			AuthenticationToken authenticationToken = template.getForObject("http://wiremock." + domain + "/login/" + username, AuthenticationToken.class);
			return token.equals(authenticationToken.getToken());
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
			    throw new NotAuthorizedException();
			}
			throw e;
		}
	}

	/**
	 * @param token
	 * @return
	 */
	@Cacheable
	public CurrentTime getAuthenticatedTime(String username, String token) {
		if (isLoggedIn(username, token)) {
			return getTime(true);
		}
		throw new NotAuthorizedException();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public Puko store(String key, String value) {
		return repository.save(pukoFactory.newPuko(key, value, new Date()));
	}

	/**
	 * @param key
	 * @return
	 */
	public Puko retrieve(String key) {
		try {
			return repository.findById(key).get();
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public void delete(String key) {
		try {
			repository.deleteById(key);
		} catch (Exception e) {
		}
	}

	/**
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (repository == null) {//
		    repository = event.getApplicationContext().getBean(CachinatorRepository.class);
		}
	}

}
