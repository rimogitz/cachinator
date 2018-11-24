/*
 * H2Puko.java, 26 Sep 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 26 Sep 2018
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class H2Puko implements Puko {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4378426519450520459L;

	@Id
	private String key;
	
	private String value;
	
	private Date timestamp;

}
