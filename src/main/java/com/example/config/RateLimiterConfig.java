package com.example.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        //Rate limit by IP address (simplest)
        //Client IP → Redis key → token bucket → allow / block
        return exchange ->
                Mono.just(
                        exchange.getRequest()
                                .getRemoteAddress()
                                .getAddress()
                                .getHostAddress()
                );
    }
}

