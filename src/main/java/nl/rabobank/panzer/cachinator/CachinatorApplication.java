/*
 * CachinatorApplication.java, 6 Jul 2018
 * Created by Joao Viegas (Joao.Nascimento@rabobank.nl)
 *
 * Copyright (c)2018, Rabobank Nederland & affiliates.
 */
package nl.rabobank.panzer.cachinator;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.panzer.cachinator.model.PukoFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@Slf4j
@EnableCaching
@SpringBootApplication
public class CachinatorApplication {
	
	@Bean
	public PukoFactory pukoFactory(Environment environment) {
		return PukoFactory.getInstance(environment.getActiveProfiles());
	}
	
	public static void main(String[] args) throws Exception {
//		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
//		System.setProperty("sun.net.spi.nameservice.nameservers", "10.253.64.15");

		SpringApplication.run(CachinatorApplication.class, args);
		log.debug("Serving cached services now");
	}
}
