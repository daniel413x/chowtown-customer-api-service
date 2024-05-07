package com.api.orders.handlers;

import com.api.utils.BaseHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class OrdersRoutesHandler extends BaseHandler {

    public OrdersRoutesHandler() {
        initializeWebClient(System.getenv("ORDERS_SVC_ADDRESS") + "/orders");
    }

    public Mono<ServerResponse> getUserOrders(ServerRequest req) {
        String addedQueryString = getQueryString(req);
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return this.webClient.get()
                .uri("/user" + addedQueryString)
                .header("Authorization", authorizationHeader)
                .exchangeToMono(response -> {
                    return response.bodyToMono(Object.class)
                            .flatMap(resBody -> ServerResponse
                                    .status(response.statusCode())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(resBody));
                })
                .onErrorResume(this::handleResponseError);
    }

    public Mono<ServerResponse> createCheckoutSession(ServerRequest req) {
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return req.bodyToMono(Object.class)
                .flatMap(reqBody -> this.webClient.post()
                        .uri("/create-checkout-session")
                        .header("Authorization", authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(reqBody)
                        .exchangeToMono(response -> {
                            return response.bodyToMono(Object.class)
                                    .flatMap(resBody -> ServerResponse
                                            .status(response.statusCode())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(resBody));
                        }))
                .onErrorResume(this::handleResponseError);
    }

    public Mono<ServerResponse> stripeCheckoutWebhook(ServerRequest req) {
        String stripeSignature = req.headers().firstHeader("stripe-signature");
        System.out.println(stripeSignature);
        return req.bodyToMono(String.class)
                .flatMap(reqBody -> this.webClient.post()
                        .uri("/stripe-checkout-webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(reqBody)
                        .header("stripe-signature", stripeSignature)
                        .exchangeToMono(response -> {
                            return response.bodyToMono(Object.class)
                                    .flatMap(resBody -> ServerResponse
                                            .status(response.statusCode())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(resBody));
                        }))
                .onErrorResume(this::handleResponseError);
    }
}
