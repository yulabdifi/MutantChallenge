package com.ml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ml.model.gateways.MutantRepoGateway;
import com.ml.usecase.GetCheckUpStatUseCase;
import com.ml.usecase.MutantCheckUseCase;

/**
 * Clase que expone los casos de uso como bean para ser usados por el handler
 * 
 * @author ybarraza
 *
 */
@Configuration
public class UseCaseConfig {

	@Bean
	public MutantCheckUseCase getMutantVerifyUseCase(MutantRepoGateway mutantRepoGateway) {
		return new MutantCheckUseCase(mutantRepoGateway);
	}

	@Bean
	public GetCheckUpStatUseCase getGetStatsUseCase(MutantRepoGateway mutantRepoGateway) {
		return new GetCheckUpStatUseCase(mutantRepoGateway);
	}

}
