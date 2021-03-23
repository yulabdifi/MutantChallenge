package com.ml.mutant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDbBean
public class MutantCheckupDTO {

	private String dna;
	private Boolean isMutant;

	@DynamoDbPartitionKey
	public String getDna() {
		return dna;
	}

	public Boolean isMutant() {
		return isMutant;
	}

}
