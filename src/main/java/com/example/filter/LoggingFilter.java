package com.example.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered { //GlobalFilter -applys to all route,Ordered -Controls execution priority among filters.

    private static final Logger log= LoggerFactory.getLogger(LoggingFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Long startTime=System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();
        log.info("Incoming Request => METHOD: {}, PATH: {}",request.getMethod(),request.getPath());
        return chain.filter(exchange)
                .doOnSuccess(aVoid ->{
                    long timeTaken=System.currentTimeMillis()-startTime;
                    log.info("Outgoing Response => STATUS: {}, TIME: {} ms",
                            exchange.getResponse().getStatusCode(),timeTaken);
                });
    }

    /* By default is zero */
    @Override
    public int getOrder() {
        return -1;
    }
}
