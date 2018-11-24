/*
 * CachinatorRedisCustomOperations.java, 16 Oct 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.repository;

import java.util.Optional;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 16 Oct 2018
 *
 */
public interface CachinatorRedisCustomOperations<T, ID> {
	
	<S extends T> S save(S puko);

	Optional<T> findById(ID id);
}
