/*
 * CachinatorGemfireRepository.java, 28 Sep 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.repository.GemfireRepository;

import nl.rabobank.panzer.cachinator.model.GemfirePuko;
import nl.rabobank.panzer.cachinator.model.PukoFactory;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 28 Sep 2018
 *
 */
@Profile(PukoFactory.GEMFIRE)
public interface CachinatorGemfireRepository extends GemfireRepository<GemfirePuko, String>, CachinatorRepository<GemfirePuko> {

}
