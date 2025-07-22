package com.neoage.product;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@RequiredArgsConstructor
@Configuration
public class ProductRouter {

  private final ProductHandler productHandler;

  @Bean
  public RouterFunction<ServerResponse> productRoutes() {
    return RouterFunctions.route(GET("/products"), productHandler::findAll)
        .andRoute(GET("/products/{id}"), productHandler::findById)
        .andRoute(POST("/products"), productHandler::create)
        .andRoute(PUT("/products/{id}"), productHandler::update)
        .andRoute(DELETE("/products/{id}"), productHandler::delete)
        .andRoute(DELETE("/products"), productHandler::deleteAll);
  }
}
