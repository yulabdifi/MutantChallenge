package com.ml.usecase;

import com.ml.model.MutantCheckupStat;
import com.ml.model.MutantCount;
import com.ml.model.gateways.MutantRepoGateway;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetCheckUpStatUseCase {

	private final MutantRepoGateway mutantRepoGateway;

	/**
	 * Gets mutant check-up stat
	 * 
	 * @return Mono<MutantCheckupStat>
	 */
	public Mono<MutantCheckupStat> getCheckUpStat() {
		// Se consultan los valores totales de la DB y se mapean al objeto de respuesta
		return mutantRepoGateway.getMutantCheckupStat().map(this::mapCheckUpStat);
	}

	/**
	 * Gets the radius from the total mutants and total records and maps it to the
	 * data of the response object
	 * 
	 * @param counts
	 * @return MutantCheckupStat
	 */
	private MutantCheckupStat mapCheckUpStat(MutantCount counts) {
		// Los registros no retornados como mutantes dieron negativo en la verificacion,
		// es decir, son humanos
		int numHumans = counts.getTotal() - counts.getMutantDna();
		double ratio = 0;
		if (numHumans > 0) {
			// El radio se calcula como numero de mutantes sobre numero de humanos
			ratio = (double) counts.getMutantDna() / numHumans;
		}
		// Se retorna los datos estadisticos dentro de un objeto que se mostrara como
		// data en el response
		return MutantCheckupStat.builder().countMutantDna(counts.getMutantDna()).countHumanDna(numHumans).ratio(ratio)
				.build();
	}
}
