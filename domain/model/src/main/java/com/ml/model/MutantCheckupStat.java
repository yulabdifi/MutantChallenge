package com.ml.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MutantCheckupStat {

	@JsonProperty("count_mutant_dna")
	private int countMutantDna;
	@JsonProperty("count_human_dna")
	private int countHumanDna;
	private double ratio;
}
