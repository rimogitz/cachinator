/*
 * GemfirePuko.java, 28 Sep 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 28 Sep 2018
 *
 */
@Getter
@Setter
@AllArgsConstructor
@Region
public class GemfirePuko implements SerializablePuko {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5104375081931229077L;

	@Id
	private String key;
	
	private String value;
	
	private Date timestamp;

}
