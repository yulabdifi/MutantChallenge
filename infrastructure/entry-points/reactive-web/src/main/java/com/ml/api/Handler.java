package com.ml.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ml.model.request.MutantCheckRequest;
import com.ml.model.response.GetStatsResponse;
import com.ml.usecase.GetCheckUpStatUseCase;
import com.ml.usecase.MutantCheckUseCase;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

	private final MutantCheckUseCase useCase;
	private final GetCheckUpStatUseCase getCheckUpStatUseCase;

	public Mono<ServerResponse> verifyHumanIsMutant(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(MutantCheckRequest.class).map(req -> req.getDna())
				.flatMap(dna -> useCase.verifyHumanIsMutant(dna)).filter(b -> b)
				.flatMap(b -> ServerResponse.ok().build())
				.switchIfEmpty(ServerResponse.status(HttpStatus.FORBIDDEN).build());
	}

	public Mono<ServerResponse> getCheckUpStat(ServerRequest serverRequest) {
		return getCheckUpStatUseCase.getCheckUpStat().flatMap(r -> ServerResponse.ok()
				.body(Mono.just(GetStatsResponse.builder().data(r).build()), GetStatsResponse.class));
	}
}
