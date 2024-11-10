package io.games.play_book.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan("io.games.play_book")
@EnableJpaRepositories("io.games.play_book")
@EnableTransactionManagement
public class DomainConfig {}
