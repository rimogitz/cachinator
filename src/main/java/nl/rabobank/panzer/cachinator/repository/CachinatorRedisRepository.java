/*
 * CachinatorRedisRepository.java, 11 Jul 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.repository;

import org.springframework.context.annotation.Profile;

import nl.rabobank.panzer.cachinator.model.PukoFactory;
import nl.rabobank.panzer.cachinator.model.RedisPuko;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 11 Jul 2018
 *
 */
@Profile({PukoFactory.REDIS,"default"})
public interface CachinatorRedisRepository extends CachinatorRepository<RedisPuko> {

}
