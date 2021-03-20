package com.ml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ml.model.gateways.MutantRepoGateway;
import com.ml.usecase.GetStatsUseCase;
import com.ml.usecase.MutantVerifyUseCase;

@Configuration
public class UseCaseConfig {

	@Bean
	public MutantVerifyUseCase getMutantVerifyUseCase(MutantRepoGateway mutantRepoGateway) {
		return new MutantVerifyUseCase(mutantRepoGateway);
	}

	@Bean
	public GetStatsUseCase getGetStatsUseCase(MutantRepoGateway mutantRepoGateway) {
		return new GetStatsUseCase(mutantRepoGateway);
	}

}
