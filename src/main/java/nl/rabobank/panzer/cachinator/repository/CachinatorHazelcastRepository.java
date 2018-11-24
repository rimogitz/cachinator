/*
 * CachinatorHazelcastRepository.java, 25 Sep 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.hazelcast.repository.HazelcastRepository;

import nl.rabobank.panzer.cachinator.model.HazelcastPuko;
import nl.rabobank.panzer.cachinator.model.PukoFactory;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 25 Sep 2018
 *
 */
@Profile(PukoFactory.HAZELCAST)
public interface CachinatorHazelcastRepository extends HazelcastRepository<HazelcastPuko, String>, CachinatorRepository<HazelcastPuko> {

}
