package com.ml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ml.model.gateways.MutantRepoGateway;
import com.ml.usecase.GetCheckUpStatUseCase;
import com.ml.usecase.MutantCheckUseCase;

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
