package com.ml.mutant.dto;

import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Builder(toBuilder = true)
@Data
@DynamoDbBean
public class MutantDTO {

	private String dna;
	private boolean isMutant;

	@DynamoDbPartitionKey
	public String getDna() {
		return dna;
	}

}
