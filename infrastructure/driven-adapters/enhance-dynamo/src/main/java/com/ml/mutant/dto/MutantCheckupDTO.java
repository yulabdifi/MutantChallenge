package com.ml.mutant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@DynamoDbBean
public class MutantCheckupDTO {

	private String dna;
	private boolean mutant;

	@DynamoDbPartitionKey
	public String getDna() {
		return dna;
	}

}
