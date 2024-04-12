package com.api.user.handlers;

import com.api.user.dto.UserDto;
import com.api.user.dto.UserPATCHReq;
import com.api.user.dto.UserPOSTReq;
import com.api.utils.BaseHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserRoutesHandler extends BaseHandler {

    public UserRoutesHandler() {
        initializeWebClient(System.getenv("USER_SVC_ADDRESS") + "/user");
    }

    public Mono<ServerResponse> getById(ServerRequest req) {
        String id = req.pathVariable("id");
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return this.webClient.get()
                        .uri("/" + id)
                        .header("Authorization", authorizationHeader)
                        .retrieve()
                        .bodyToMono(UserDto.class)
                        .flatMap(userDto ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(userDto))
                        .onErrorResume(this::handleResponseError);
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return req.bodyToMono(UserPOSTReq.class)
                .flatMap(body -> this.webClient.post()
                        .uri("")
                        .header("Authorization", authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .exchangeToMono(response -> {
                            return response.bodyToMono(UserDto.class)
                                    .flatMap(userDto -> ServerResponse
                                            .status(response.statusCode())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(userDto));
                        }))
                        .onErrorResume(this::handleResponseError);
    }

    public Mono<ServerResponse> patch(ServerRequest req) {
        String id = req.pathVariable("id");
        String authorizationHeader = req.headers().firstHeader("Authorization");
        return req.bodyToMono(UserPATCHReq.class)
                .flatMap(body -> this.webClient.patch()
                        .uri("/" + id)
                        .header("Authorization", authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(UserDto.class)
                        .flatMap(userDto ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(userDto))
                        .onErrorResume(this::handleResponseError));
    }
}
