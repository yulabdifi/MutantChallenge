package com.ml.mutant;

import static org.mockito.ArgumentMatchers.any;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.ml.model.Mutant;
import com.ml.model.MutantCount;
import com.ml.mutant.dto.MutantCheckupDTO;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.Condition;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

@RunWith(SpringRunner.class)
public class MutantRepositoryTest {

	@Mock
	DynamoDbAsyncClient asyncClient;

	@Mock
	DynamoDbEnhancedAsyncClient enhancedAsyncClient;

	@Mock
	DynamoDbAsyncTable<MutantCheckupDTO> mutantTable;

	private MutantRepository repository;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		Mockito.when(enhancedAsyncClient.table(any(), any(BeanTableSchema.class))).thenReturn(mutantTable);
		repository = new MutantRepository(asyncClient, enhancedAsyncClient, MutantCheckupDTO.class.getSimpleName());
	}

	@Test
	public void saveMutantShouldReturnFalseWhenPutItemIsSuccess() {
		Mutant mutant = getMutantMock();
		MutantCheckupDTO dto = MutantCheckupDTO.builder().dna(mutant.getDna()).mutant(mutant.isMutant()).build();
		Mockito.when(mutantTable.putItem(dto)).thenReturn(CompletableFuture.allOf());

		Mono<Boolean> res = repository.saveMutant(mutant);
		StepVerifier.create(res).expectNextMatches(b -> b).verifyComplete();
	}

	@Test
	public void getMutantCheckupStat() {
		Map<String, Condition> filter = repository.loadFilterIsMutant();
		ScanRequest scanRequest = ScanRequest.builder().tableName(MutantCheckupDTO.class.getSimpleName())
				.scanFilter(filter).build();

		int totalRecords = 50;
		Mockito.when(asyncClient.scan(scanRequest)).thenReturn(
				CompletableFuture.completedFuture(ScanResponse.builder().count(10).scannedCount(totalRecords).build()));

		Mono<MutantCount> res = repository.getMutantCheckupStat();
		StepVerifier.create(res).expectNextMatches(r -> r.getTotal() == totalRecords).verifyComplete();
	}

	private Mutant getMutantMock() {
		return Mutant.builder().dna("ATGCGA-CAGTGC").isMutant(Boolean.FALSE).build();
	}

}
