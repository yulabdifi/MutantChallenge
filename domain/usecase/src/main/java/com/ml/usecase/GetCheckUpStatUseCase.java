package com.ml.usecase;

import com.ml.model.MutantCheckupStat;
import com.ml.model.MutantCount;
import com.ml.model.gateways.MutantRepoGateway;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetCheckUpStatUseCase {

	private final MutantRepoGateway mutantRepoGateway;

	public Mono<MutantCheckupStat> getCheckUpStat() {
		return mutantRepoGateway.getMutantCheckupStat().map(this::mapCheckUpStat);
	}

	private MutantCheckupStat mapCheckUpStat(MutantCount counts) {
		int numHumans = 0;
		double ratio = 0;
		if (counts.getTotal() > 0) {
			numHumans = counts.getTotal() - counts.getMutantDna();
			ratio = (double) counts.getMutantDna() / numHumans;
		}
		return MutantCheckupStat.builder().countMutantDna(counts.getMutantDna()).countHumanDna(numHumans).ratio(ratio)
				.build();
	}
}
