/*
 * CachinatorRepository.java, 20 Sep 2018
 * Created by Joao Viegas (joao.viegas@mindprogeny.com)
 *
 * Copyright (c)2018 Mind Progeny.
 */

package nl.rabobank.panzer.cachinator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import nl.rabobank.panzer.cachinator.model.Puko;

/**
 * @author Jo&atilde;o Viegas (joao.viegas@mindprogeny.com)
 * @since 20 Sep 2018
 *
 */
@NoRepositoryBean
public interface CachinatorRepository<T extends Puko> extends CrudRepository<T, String>{

}
