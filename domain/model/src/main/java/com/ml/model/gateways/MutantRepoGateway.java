package com.ml.model.gateways;

import com.ml.model.Mutant;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MutantRepoGateway {

	public Mono<Boolean> saveMutant(Mutant mutant);

	public Flux<Mutant> getAllMutantVerifications();
}
