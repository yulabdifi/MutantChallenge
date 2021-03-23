package com.ml.usecase;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ml.model.Mutant;
import com.ml.model.gateways.MutantRepoGateway;
import com.ml.model.util.Constants;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class MutantCheckUseCaseTest {

	@Mock
	MutantRepoGateway mutantRepoGateway;

	@Test
	public void verifyHumanIsMutantShouldReturnTrueWhenContains4LetterConsecutive() {
		String[] secuencias = { "ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG" };
		boolean isHumanMock = Boolean.TRUE;

		String strDna = Stream.of(secuencias).map(Object::toString).collect(Collectors.joining(Constants.HYPHEN));
		Mockito.when(mutantRepoGateway.saveMutant(Mutant.builder().dna(strDna).isMutant(isHumanMock).build()))
				.thenReturn(Mono.just(isHumanMock));

		MutantCheckUseCase useCase = new MutantCheckUseCase(mutantRepoGateway);
		Mono<Boolean> isHuman = useCase.verifyHumanIsMutant(Stream.of(secuencias).collect(Collectors.toList()));
		StepVerifier.create(isHuman).expectNextMatches(b -> b).verifyComplete();
	}

}
