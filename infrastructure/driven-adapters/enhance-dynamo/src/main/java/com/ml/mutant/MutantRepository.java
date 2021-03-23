package com.ml.mutant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ml.model.Mutant;
import com.ml.model.MutantCount;
import com.ml.model.gateways.MutantRepoGateway;
import com.ml.mutant.dto.MutantCheckupDTO;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ComparisonOperator;
import software.amazon.awssdk.services.dynamodb.model.Condition;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

@Component
public class MutantRepository implements MutantRepoGateway {

	private static final String FILTER_IS_MUTANT = "isMutant";

	private final DynamoDbAsyncTable<MutantCheckupDTO> mutantTable;
	private final DynamoDbAsyncClient asyncClient;
	private final String dynamoDbTableName;

	public MutantRepository(DynamoDbAsyncClient asyncClient, DynamoDbEnhancedAsyncClient enhancedAsyncClient,
			@Value("${dynamodb.tbl-name.mutant}") String dynamoDbTableName) {
		this.asyncClient = asyncClient;
		this.dynamoDbTableName = dynamoDbTableName;
		this.mutantTable = enhancedAsyncClient.table(dynamoDbTableName, TableSchema.fromBean(MutantCheckupDTO.class));
	}

	public Mono<Boolean> saveMutant(Mutant mutant) {
		return toEntity(mutant).flatMap(m -> Mono.fromFuture(mutantTable.putItem(m))).thenReturn(Boolean.TRUE)
				.doOnError(ex -> ex.printStackTrace()).onErrorReturn(Boolean.FALSE);
	}

	public Mono<MutantCount> getMutantCheckupStat() {
		ScanRequest scanRequest = ScanRequest.builder().tableName(dynamoDbTableName).scanFilter(loadFilterIsMutant())
				.build();

		return Mono.fromFuture(asyncClient.scan(scanRequest)).map(scanResponse -> MutantCount.builder()
				.mutantDna(scanResponse.count()).total(scanResponse.scannedCount()).build());
	}

	public Map<String, Condition> loadFilterIsMutant() {
		List<AttributeValue> list = new ArrayList<>();
		list.add(AttributeValue.builder().bool(Boolean.TRUE).build());
		Map<String, Condition> filter = new HashMap<>();
		filter.put(FILTER_IS_MUTANT, Condition.builder().comparisonOperator(ComparisonOperator.EQ.toString())
				.attributeValueList(list).build());
		return filter;
	}

	private Mono<MutantCheckupDTO> toEntity(Mutant mutant) {
		return Mono.justOrEmpty(mutant)
				.map(m -> MutantCheckupDTO.builder().dna(m.getDna()).isMutant(m.isMutant()).build());
	}

}
