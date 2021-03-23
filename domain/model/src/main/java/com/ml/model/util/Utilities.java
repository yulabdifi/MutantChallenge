package com.ml.model.util;

import java.util.List;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class Utilities {

	/**
	 * Transforma una lista de string en una matriz de chars
	 * 
	 * @param list is List<String>
	 * @return Mono<char[][]>
	 */
	public static Mono<char[][]> listToMatrix(List<String> list) {
		if (list != null && !list.isEmpty()) {
			char[][] matrix = new char[list.size()][list.get(0).length()];
			for (int i = 0; i < list.size(); i++) {
				char[] charArray = list.get(i).toCharArray();
				for (int j = 0; j < charArray.length; j++) {
					matrix[i][j] = charArray[j];
				}
			}
			return Mono.just(matrix);
		} else {
			return Mono.empty();
		}
	}

}
