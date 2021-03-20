package com.ml.model;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class Mutant {

	private String dna;
	private boolean isMutant;

	public String getDna() {
		return dna;
	}

}
