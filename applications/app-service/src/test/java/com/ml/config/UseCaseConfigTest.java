package com.ml.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mock;

import com.ml.model.gateways.MutantRepoGateway;

public class UseCaseConfigTest {

	@Mock
	private MutantRepoGateway mutantRepoGateway;

	@Test
	public void getMutantVerifyUseCaseShouldReturnNotNull() {
		UseCaseConfig useCaseConfig = new UseCaseConfig();
		assertNotNull(useCaseConfig.getMutantVerifyUseCase(mutantRepoGateway));
	}

	@Test
	public void getGetStatsUseCaseShouldReturnNotNull() {
		UseCaseConfig useCaseConfig = new UseCaseConfig();
		assertNotNull(useCaseConfig.getGetStatsUseCase(mutantRepoGateway));
	}

}
