package com.ml.api.handleexception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ml.model.response.ErrorResponse;

import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class ErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(ErrorWebExceptionHandler.class);

	public ErrorWebExceptionHandler(DefaultErrorAttributes errorAttributes, ApplicationContext applicationContext,
			ServerCodecConfigurer serverCodecConfigurer) {
		super(errorAttributes, new Resources(), applicationContext);
		super.setMessageWriters(serverCodecConfigurer.getWriters());
		super.setMessageReaders(serverCodecConfigurer.getReaders());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::loadErrorResponse);
	}

	private Mono<ServerResponse> loadErrorResponse(final ServerRequest request) {
		return Mono.just(request).map(this::getError).flatMap(Mono::error).onErrorResume(this::mapErrorResponse)
				.flatMap(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(e),
						ErrorResponse.class));
	}

	private Mono<ErrorResponse> mapErrorResponse(Throwable t) {
		logger.error(t.getMessage(), t);
		return Mono.just(
				ErrorResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(t.getMessage()).build());
	}
}
