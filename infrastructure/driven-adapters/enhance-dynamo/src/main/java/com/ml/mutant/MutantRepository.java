package com.ml.mutant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ml.model.Mutant;
import com.ml.model.gateways.MutantRepoGateway;
import com.ml.mutant.dto.MutantDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Component
public class MutantRepository implements MutantRepoGateway {

	private final DynamoDbAsyncTable<MutantDTO> mutantTable;

	public MutantRepository(DynamoDbEnhancedAsyncClient enhancedAsyncClient,
			@Value("${dynamodb.tbl-name.mutant}") String dynamoDbTableName) {
		this.mutantTable = enhancedAsyncClient.table(dynamoDbTableName, TableSchema.fromBean(MutantDTO.class));
	}

	public Mono<Boolean> saveMutant(Mutant mutant) {
		return mapMutantDTO(mutant).flatMap(m -> Mono.fromFuture(mutantTable.putItem(m)))
				.subscribeOn(Schedulers.boundedElastic()).thenReturn(Boolean.TRUE).doOnError(ex -> ex.printStackTrace())
				.onErrorReturn(Boolean.FALSE);
	}

	public Flux<Mutant> getAllMutantVerifications() {
		return Flux.from(mutantTable.scan().items())
				.map(m -> Mutant.builder().dna(m.getDna()).isMutant(m.isMutant()).build());
	}

	private Mono<MutantDTO> mapMutantDTO(Mutant mutant) {
		return Mono.just(mutant).map(m -> MutantDTO.builder().dna(m.getDna()).isMutant(m.isMutant()).build());
	}
}
