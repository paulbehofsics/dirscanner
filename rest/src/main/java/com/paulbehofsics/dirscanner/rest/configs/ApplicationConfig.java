package com.paulbehofsics.dirscanner.rest.configs;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.paulbehofsics.dirscanner"})
@EntityScan(basePackages = {"com.paulbehofsics.dirscanner"})
public class ApplicationConfig {}
