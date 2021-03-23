package com.ml.mutant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final String FILTER_IS_MUTANT = "mutant";

	private final DynamoDbAsyncTable<MutantCheckupDTO> mutantTable;
	private final DynamoDbAsyncClient asyncClient;
	private final String dynamoDbTableName;

	private final Logger logger = LoggerFactory.getLogger(MutantRepository.class);

	public MutantRepository(DynamoDbAsyncClient asyncClient, DynamoDbEnhancedAsyncClient enhancedAsyncClient,
			@Value("${dynamodb.tbl-name.mutant}") String dynamoDbTableName) {
		this.asyncClient = asyncClient;
		this.dynamoDbTableName = dynamoDbTableName;
		this.mutantTable = enhancedAsyncClient.table(dynamoDbTableName, TableSchema.fromBean(MutantCheckupDTO.class));
	}

	/**
	 * Saves mutant check up
	 * 
	 * @param Mutant
	 * @return Mono<Boolean>
	 */
	public Mono<Boolean> saveMutant(Mutant mutant) {
		// Se mapea a la entidad y se adiciona a la tabla de manera asíncrona
		// En caso de error se imprime dicho error y se retonar false
		return toEntity(mutant).flatMap(m -> Mono.fromFuture(mutantTable.putItem(m))).thenReturn(Boolean.TRUE)
				.doOnError(t -> logger.error(t.getMessage(), t)).onErrorReturn(Boolean.FALSE);
	}

	/**
	 * Gets mutant checkup Stat (number of mutant and total of records)
	 * 
	 * @return Mono<MutantCount>
	 */
	public Mono<MutantCount> getMutantCheckupStat() {
		// Se crea un scan request a la tabla con el filtro sobre el campo mutant
		ScanRequest scanRequest = ScanRequest.builder().tableName(dynamoDbTableName).scanFilter(loadFilterIsMutant())
				.build();
		// Se obtienen los items que cumplieron con el filtro y el total de la busqueda
		// que sera igual al total de registros
		return Mono.fromFuture(asyncClient.scan(scanRequest)).map(scanResponse -> {
			return MutantCount.builder().mutantDna(scanResponse.count()).total(scanResponse.scannedCount()).build();
		});
	}

	/**
	 * Load filter to validate isMutant field equals true
	 * 
	 * @return Map<String, Condition>
	 */
	public Map<String, Condition> loadFilterIsMutant() {
		List<AttributeValue> list = new ArrayList<>();
		list.add(AttributeValue.builder().bool(Boolean.TRUE).build());
		Map<String, Condition> filter = new HashMap<>();
		filter.put(FILTER_IS_MUTANT, Condition.builder().comparisonOperator(ComparisonOperator.EQ.toString())
				.attributeValueList(list).build());
		return filter;
	}

	/**
	 * Map mutant to entity MutantCheckupDTO
	 * 
	 * @param mutant
	 * @return Mono<MutantCheckupDTO>
	 */
	private Mono<MutantCheckupDTO> toEntity(Mutant mutant) {
		return Mono.justOrEmpty(mutant)
				.map(m -> MutantCheckupDTO.builder().dna(m.getDna()).mutant(m.isMutant()).build());
	}

}
