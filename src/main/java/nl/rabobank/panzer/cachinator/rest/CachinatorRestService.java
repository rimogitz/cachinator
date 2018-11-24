/*
 * CachinatorRestService.java, 2 Jul 2018
 * Created by Joao Viegas (Joao.Nascimento@rabobank.nl)
 *
 * Copyright (c)2018, Rabobank Nederland & affiliates.
 */

package nl.rabobank.panzer.cachinator.rest;

import nl.rabobank.panzer.cachinator.service.LoadTestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import nl.rabobank.panzer.cachinator.model.Authentication;
import nl.rabobank.panzer.cachinator.model.AuthenticationToken;
import nl.rabobank.panzer.cachinator.model.CurrentTime;
import nl.rabobank.panzer.cachinator.model.Puko;
import nl.rabobank.panzer.cachinator.service.CachinatorService;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 2 Jul 2018
 *
 */
@RestController
@AllArgsConstructor
public class CachinatorRestService {
	
	@NonNull
	private CachinatorService service;

	@RequestMapping("/time")
	public CurrentTime getTime() {
		return service.getTime();
	}
	
	@RequestMapping(method=RequestMethod.POST, path="/time")
	public CurrentTime getAuthenticatedTime(@RequestBody AuthenticationToken token) {
		return service.getAuthenticatedTime(token.getUsername(), token.getToken());
	}
	
	@RequestMapping(method=RequestMethod.POST, path="/login")
	public AuthenticationToken login(@RequestBody Authentication authentication) {
		return service.login(authentication);
	}
	
	@RequestMapping(method=RequestMethod.PUT, path="/store/{key}/{value}")
	public Puko store(@PathVariable("key") String key, @PathVariable("value") String value) {
		return service.store(key, value);
	}

	@RequestMapping(path="/store/{key}")
	public Puko get(@PathVariable("key") String key) {
		return service.retrieve(key);
	}

	@RequestMapping(method=RequestMethod.DELETE, path="/store/{key}")
	public String delete(@PathVariable("key") String key) {
		service.delete(key);
		return "Record deleted.";
	}

}
