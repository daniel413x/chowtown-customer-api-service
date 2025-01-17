package com.api.restaurant.handlers;

import com.api.utils.BaseHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RestaurantRoutesHandler extends BaseHandler {

    public RestaurantRoutesHandler() {
        initializeWebClient(System.getenv("RESTAURANT_SVC_ADDRESS"));
    }

    public Mono<ServerResponse> getByRestaurantId(ServerRequest req) {
        String restaurantId = req.pathVariable("restaurantId");
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return this.webClient.get()
                .uri("/by-id" + "/" + restaurantId)
                .header("Authorization", authorizationHeader)
                .retrieve()
                .bodyToMono(Object.class)
                .flatMap(restaurant ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(restaurant))
                .onErrorResume(this::handleResponseError);
    }

    public Mono<ServerResponse> getByRestaurantSlug(ServerRequest req) {
        String restaurantSlug = req.pathVariable("restaurantSlug");
        return this.webClient.get()
                .uri("/" + restaurantSlug)
                .retrieve()
                .bodyToMono(Object.class)
                .flatMap(restaurant ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(restaurant))
                .onErrorResume(this::handleResponseError);
    }

    public Mono<ServerResponse> getBySearch(ServerRequest req) {
        String city = req.pathVariable("city");
        String addedQueryString = getQueryString(req);
        return this.webClient.get()
                        .uri("/search" + "/" + city + addedQueryString)
                        .retrieve()
                        .bodyToMono(Object.class)
                        .flatMap(restaurantDtoArr ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(restaurantDtoArr))
                        .onErrorResume(this::handleResponseError);
    }
}
