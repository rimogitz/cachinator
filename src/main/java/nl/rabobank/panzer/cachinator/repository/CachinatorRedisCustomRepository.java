/*
 * CachinatorRedisCustomRepository.java, 16 Oct 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.repository;

import nl.rabobank.panzer.cachinator.model.PukoFactory;
import nl.rabobank.panzer.cachinator.model.RedisPuko;
import org.springframework.context.annotation.Profile;

/**
 * 
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 16 Oct 2018
 *
 */
@Profile(PukoFactory.REDIS_RAW)
public interface CachinatorRedisCustomRepository extends CachinatorRepository<RedisPuko>, CachinatorRedisCustomOperations<RedisPuko, String> {

}
