package com.ml.model;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class MutantCount {

	private int mutantDna;
	private int total;
}
