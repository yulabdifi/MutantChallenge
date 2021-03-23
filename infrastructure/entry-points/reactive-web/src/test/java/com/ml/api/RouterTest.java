package com.ml.api;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ml.model.MutantCheckupStat;
import com.ml.model.request.MutantCheckRequest;
import com.ml.model.response.ErrorResponse;
import com.ml.model.response.GetStatsResponse;
import com.ml.model.util.Constants;
import com.ml.usecase.GetCheckUpStatUseCase;
import com.ml.usecase.MutantCheckUseCase;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouterTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private MutantCheckUseCase mutantCheckUseCase;
	@MockBean
	private GetCheckUpStatUseCase getCheckUpStatUseCase;

	@Test
	public void mutantShouldReturnHttpStatus200WhenVerifyHumanIsMutantReturnTrue() {
		List<String> dna = new ArrayList<>();
		dna.add("CCCCTA");
		MutantCheckRequest request = new MutantCheckRequest();
		request.setDna(dna);

		Mockito.when(mutantCheckUseCase.verifyHumanIsMutant(dna)).thenReturn(Mono.just(Boolean.TRUE));

		webTestClient.post().uri(Constants.ROUTE_VERIFY_IS_MUTANT).accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), MutantCheckRequest.class).exchange().expectStatus().isOk();
	}

	@Test
	public void mutantShouldReturnHttpStatus403WhenVerifyHumanIsMutantReturnFalse() {
		List<String> dna = new ArrayList<>();
		dna.add("TTATGT");
		MutantCheckRequest request = new MutantCheckRequest();
		request.setDna(dna);

		Mockito.when(mutantCheckUseCase.verifyHumanIsMutant(dna)).thenReturn(Mono.just(Boolean.FALSE));

		webTestClient.post().uri(Constants.ROUTE_VERIFY_IS_MUTANT).accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), MutantCheckRequest.class).exchange().expectStatus().isForbidden();
	}

	@Test
	public void statShouldReturnOkWhenGetCheckUpStatReturnRatio1() {
		MutantCheckupStat stat = MutantCheckupStat.builder().countHumanDna(1).countMutantDna(1).ratio(1).build();
		Mockito.when(getCheckUpStatUseCase.getCheckUpStat()).thenReturn(Mono.just(stat));

		webTestClient.get().uri(Constants.ROUTE_GET_CHECKUP_STAT).accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk().expectBody(GetStatsResponse.class);
	}

	@Test
	public void statShouldReturnErrorResponseWhenUseCaseThrowError() {
		Mockito.when(getCheckUpStatUseCase.getCheckUpStat()).thenReturn(Mono.error(new Exception("Error en consulta")));

		webTestClient.get().uri(Constants.ROUTE_GET_CHECKUP_STAT).accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().is5xxServerError().expectBody(ErrorResponse.class);
	}

	@Test
	public void healthShouldReturnOk() {
		webTestClient.get().uri(Constants.ROUTE_HEALTH).accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
				.isOk();
	}
}
