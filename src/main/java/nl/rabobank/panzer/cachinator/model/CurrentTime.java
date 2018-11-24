/*
 * CurrentTime.java, 2 Jul 2018
 * Created by Joao Viegas (Joao.Nascimento@rabobank.nl)
 *
 * Copyright (c)2018, Rabobank Nederland & affiliates.
 */

package nl.rabobank.panzer.cachinator.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 2 Jul 2018
 *
 */
@Getter
@Setter
public class CurrentTime implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5749426938455239880L;

	private Date timestamp;
}
