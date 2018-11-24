/*
 * CachinatorExceptionHandler.java, 11 Jul 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.rest;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import nl.rabobank.panzer.cachinator.NotAuthorizedException;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 11 Jul 2018
 *
 */
@ControllerAdvice
public class CachinatorExceptionHandler {

	@ExceptionHandler( value = NoSuchElementException.class )
	public ResponseEntity<Object> handleRecordNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record Not Found");
    }

	@ExceptionHandler( value = NotAuthorizedException.class )
	public ResponseEntity<Object> handleNotAuthorized(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not Authenticated");
    }
}
