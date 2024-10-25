package com.bytmasoft.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class.getName());


      @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        HttpHeaders headers = exchange.getRequest().getHeaders();
        logger.info("Incoming request to path: {}", path);
          logger.info("Request: Method = {}, Path = {}, Headers = {}",
                  exchange.getRequest().getMethod(), exchange.getRequest().getURI().getPath(),
                  exchange.getRequest().getHeaders());
        headers.forEach((key, value ) -> {logger.info("Headers '{}' = {}", key, value);});

        // Proceed with the chain and log response details
         return chain.filter(exchange).then(Mono.fromRunnable(() -> {
             logger.info("Response status code: {}", exchange.getResponse().getStatusCode());

         }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
