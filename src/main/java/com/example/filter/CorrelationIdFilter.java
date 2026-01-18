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
import java.util.UUID;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);
    private static final String CORRELATION_ID = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request=exchange.getRequest();
        String correlationId= request.getHeaders().getFirst(CORRELATION_ID);

        if(correlationId==null){
            correlationId = UUID.randomUUID().toString();
        }
        log.info("[CID={}] Incoming request {}", correlationId, request.getPath());

        /*
        * Gateway requests are immutable.Mutation creates a new request.
        * Injects correlation ID into request
        * Forwarded to all downstream services
        * */
        ServerHttpRequest modifiedRequest = request.mutate()
                .header(CORRELATION_ID, correlationId)
                .build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        //Runs before logging filter
        return -2;
    }
}
