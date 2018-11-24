/*
 * Puko.java, 6 Jul 2018
 * Created by Joao Viegas (Joao.Nascimento@rabobank.nl)
 *
 * Copyright (c)2018, Rabobank Nederland & affiliates.
 */

package nl.rabobank.panzer.cachinator.model;

import java.io.Serializable;
import java.util.Date;

/**
 * ## Prepare for Implementing multiple data sources ##
 * 
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 6 Jul 2018
 *
 */
public interface Puko /*extends Serializable*/ {

	/**
	 * Gets the value for key
	 * @return the key
	 */
	String getKey();

	/**
	 * Sets the value for key
	 * @param key the key to set
	 */
	void setKey(String key);

	/**
	 * Gets the value for value
	 * @return the value
	 */
	String getValue();

	/**
	 * Sets the value for value
	 * @param value the value to set
	 */
	void setValue(String value);

	/**
	 * Gets the value for timestamp
	 * @return the timestamp
	 */
	Date getTimestamp();

	/**
	 * Sets the value for timestamp
	 * @param timestamp the timestamp to set
	 */
	void setTimestamp(Date timestamp);
}
