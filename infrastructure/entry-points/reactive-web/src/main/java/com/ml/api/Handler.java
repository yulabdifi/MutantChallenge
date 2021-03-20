package com.ml.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ml.model.request.MutantVerifyRequest;
import com.ml.model.response.GetStatsResponse;
import com.ml.usecase.GetStatsUseCase;
import com.ml.usecase.MutantVerifyUseCase;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

	private final MutantVerifyUseCase useCase;
	private final GetStatsUseCase getStatsUseCase;

	public Mono<ServerResponse> verifyHumanIsMutant(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(MutantVerifyRequest.class).map(req -> req.getDna())
				.flatMap(dna -> useCase.verifyHumanIsMutant(dna)).filter(b -> b)
				.flatMap(b -> ServerResponse.ok().build())
				.switchIfEmpty(ServerResponse.status(HttpStatus.FORBIDDEN).build());
	}

	public Mono<ServerResponse> getStats(ServerRequest serverRequest) {
		return getStatsUseCase.getStats().flatMap(
				r -> ServerResponse.ok().body(GetStatsResponse.builder().data(r).build(), GetStatsResponse.class));
	}
}
