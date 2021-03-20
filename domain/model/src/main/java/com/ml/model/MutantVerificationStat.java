package com.ml.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class MutantVerificationStat {

	@JsonProperty("count_mutant_dna")
	private long countMutantDna;
	@JsonProperty("count_human_dna")
	private long countHumanDna;
	private long ratio;
}
