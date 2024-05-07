package com.api.orders.router;

import com.api.orders.handlers.OrdersRoutesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class OrdersRouter {

    @Bean
    public RouterFunction<ServerResponse> ordersRouterRoutes(OrdersRoutesHandler ordersRoutesHandler) {
        return RouterFunctions
                .route()
                .nest(RequestPredicates.path("/api/orders"), builder -> {
                    builder.GET("/user", ordersRoutesHandler::getUserOrders);
                    builder.POST("/create-checkout-session", ordersRoutesHandler::createCheckoutSession);
                    builder.POST("/stripe-checkout-webhook", ordersRoutesHandler::stripeCheckoutWebhook);
                })
                .build();
    }
}
