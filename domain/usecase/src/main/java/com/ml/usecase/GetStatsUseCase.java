package com.ml.usecase;

import com.ml.model.Mutant;
import com.ml.model.MutantVerificationStat;
import com.ml.model.gateways.MutantRepoGateway;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetStatsUseCase {

	private final MutantRepoGateway mutantRepoGateway;

	public Mono<MutantVerificationStat> getStats() {
		Flux<Mutant> mutantV = mutantRepoGateway.getAllMutantVerifications();
		return Mono.zip(mutantV.filter(m -> m.isMutant()).count(), mutantV.filter(m -> !m.isMutant()).count(),
				mutantV.count()).map(z -> getStatVerification(z.getT1(), z.getT2(), z.getT3()));
	}

	private MutantVerificationStat getStatVerification(long numMutants, long numHumans, long totalItems) {
		long ratio = numMutants / numHumans;
		return MutantVerificationStat.builder().countMutantDna(numMutants).countHumanDna(numHumans).ratio(ratio)
				.build();
	}
}
