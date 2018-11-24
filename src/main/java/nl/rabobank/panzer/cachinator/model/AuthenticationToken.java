/*
 * AuthenticationToken.java, 2 Jul 2018
 * Created by Joao Viegas (Joao.Nascimento@rabobank.nl)
 *
 * Copyright (c)2018, Rabobank Nederland & affiliates.
 */

package nl.rabobank.panzer.cachinator.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 2 Jul 2018
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5435888851159117709L;

	private String username;
	
	private String token;
}
