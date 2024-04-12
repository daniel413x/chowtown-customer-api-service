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

    private String getQueryString(ServerRequest req) {
        String queryString = req.uri().getQuery();
        String addedQueryString = "";
        if (queryString != null) {
            addedQueryString = "?" + queryString;
        }
        return addedQueryString;
    }
}

