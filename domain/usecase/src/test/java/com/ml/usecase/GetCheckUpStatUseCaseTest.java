package com.ml.usecase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ml.model.MutantCheckupStat;
import com.ml.model.MutantCount;
import com.ml.model.gateways.MutantRepoGateway;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class GetCheckUpStatUseCaseTest {

	@Mock
	MutantRepoGateway mutantRepoGateway;

	@Test
	public void getCheckUpStatUseCaseShouldReturnRatio0_4WhenMutantIs40AndTotal140() {
		MutantCount count = MutantCount.builder().mutantDna(40).total(140).build();
		Mockito.when(mutantRepoGateway.getMutantCheckupStat()).thenReturn(Mono.just(count));

		GetCheckUpStatUseCase useCase = new GetCheckUpStatUseCase(mutantRepoGateway);
		Mono<MutantCheckupStat> checkup = useCase.getCheckUpStat();
		StepVerifier.create(checkup).expectNextMatches(r -> r.getRatio() == 0.4).verifyComplete();
	}

}
