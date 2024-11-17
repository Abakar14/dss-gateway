package com.bytmasoft.logging;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import java.io.ByteArrayInputStream;

import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
public static final int CONTENT_LENGTH_BYTES = 10;
public static final int CHUNK_SIZE_BYTES = 3;

      @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
          // Log request details
          logRequest(exchange);

       // String path = exchange.getRequest().getURI().getPath();
       // HttpHeaders headers = exchange.getRequest().getHeaders();
       // logger.info("Incoming request to path: {}", path);
        //  logger.info("Request: Method = {}, Path = {}, Headers = {}",
         //         exchange.getRequest().getMethod(), exchange.getRequest().getURI().getPath(),
          //        exchange.getRequest().getHeaders());
        //headers.forEach((key, value ) -> {logger.info("Headers '{}' = {}", key, value);});

        // Proceed with the chain and log response details
       //  return chain.filter(exchange).then(Mono.fromRunnable(() -> {
         //    logger.info("Response status code: {}", exchange.getResponse().getStatusCode());

         //}));


          ServerHttpResponse originalResponse = exchange.getResponse();
          ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
              @Override
              public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                  if (originalResponse.getStatusCode() != null && (originalResponse.getStatusCode().is4xxClientError() || originalResponse.getStatusCode().is5xxServerError())) {
                      // Capture and log the response body
                      return super.writeWith(Flux.from(body).doOnNext(dataBuffer -> logResponseBody(dataBuffer)));
                  }
                  return super.writeWith(body);
              }
          };

          // Replace the original response with the decorated response
          return chain.filter(exchange.mutate().response(decoratedResponse).build());


      }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


private void logRequest(ServerWebExchange exchange) {
    String path = exchange.getRequest().getURI().getPath();
    logger.info("Incoming request: Method = {}, Path = {}, Headers = {}",
            exchange.getRequest().getMethod(), path, exchange.getRequest().getHeaders());
}

private void logResponseBody(DataBuffer dataBuffer) {
    byte[] bytes = new byte[dataBuffer.readableByteCount()];
    dataBuffer.read(bytes);
    String responseBody = new String(bytes, StandardCharsets.UTF_8);
    logger.error("Response Body: {}", responseBody);
}



}
