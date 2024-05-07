package com.api.restaurant.router;

import com.api.restaurant.handlers.RestaurantRoutesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class RestaurantRouter {

    @Bean
    public RouterFunction<ServerResponse> restaurantRoutes(RestaurantRoutesHandler restaurantRoutesHandler) {
        return RouterFunctions
                .route()
                .nest(RequestPredicates.path("/api/restaurant"), builder -> {
                    builder.GET("/search/{city}", restaurantRoutesHandler::getBySearch);
                    builder.GET("/{restaurantSlug}", restaurantRoutesHandler::getByRestaurantSlug);
                    builder.GET("/by-id/{restaurantId}", restaurantRoutesHandler::getByRestaurantId);
                })
                .build();
    }
}
