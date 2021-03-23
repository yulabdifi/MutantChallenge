package com.ml.model.gateways;

import com.ml.model.Mutant;
import com.ml.model.MutantCount;

import reactor.core.publisher.Mono;

public interface MutantRepoGateway {

	public Mono<Boolean> saveMutant(Mutant mutant);

	public Mono<MutantCount> getMutantCheckupStat();
}
