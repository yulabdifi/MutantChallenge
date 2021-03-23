package com.ml.usecase;

import java.util.List;
import java.util.stream.Collectors;

import com.ml.model.Mutant;
import com.ml.model.gateways.MutantRepoGateway;
import com.ml.model.util.Constants;
import com.ml.model.util.Utilities;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MutantCheckUseCase {

	private final MutantRepoGateway mutantRepoGateway;

	public Mono<Boolean> verifyHumanIsMutant(List<String> dna) {
		return Utilities.listToMatrix(dna).map(matrix -> isMutant(matrix)).doOnSuccess(b -> saveMutant(dna, b));
	}

	private Mono<Boolean> saveMutant(List<String> dna, boolean isMutant) {
		String strDna = dna.stream().map(Object::toString).collect(Collectors.joining(Constants.HYPHEN));

		return mutantRepoGateway.saveMutant(Mutant.builder().dna(strDna).isMutant(isMutant).build());
	}

	private boolean isMutant(char[][] matrix) {
		boolean isMutant = Boolean.FALSE;

		// Nos ubicamos hacia el centro para iniciar la búsqueda
		int init = Constants.NUM_REPEAT_LETTER_MUTANT - 1;
		for (int i = init; !isMutant && i < matrix.length; i++) {
			for (int j = init; !isMutant && j < matrix.length; j++) {
				int numRep = 0;
				int indDir = 0;
				while (indDir < Constants.NUM_COORDINATES && numRep < (Constants.NUM_REPEAT_LETTER_MUTANT - 1)) {
					numRep = 0;
					// se validan las repeticiones en la misma coordenada,
					// arriba y abajo por ejemplo
					numRep = validateRepeatedByCoordinate(indDir++, i, j, matrix, numRep);
					numRep += validateRepeatedByCoordinate(indDir++, i, j, matrix, numRep);
				}

				isMutant = numRep > (Constants.NUM_REPEAT_LETTER_MUTANT - 1);
			}
		}
		return isMutant;
	}

	private int validateRepeatedByCoordinate(int indDir, final int i, final int j, char[][] matrix,
			int numRepetitions) {
		int posX = i;
		int posY = j;
		boolean isRepeated = Boolean.FALSE;
		do {
			// avanzo en la dirección o coordenada indicada
			if (indDir == 0) {
				posX++;
			} else if (indDir == 1) {
				posX--;
			} else if (indDir == 2) {
				posY++;
			} else if (indDir == 3) {
				posY--;
			} else if (indDir == 4) {
				posX++;
				posY++;
			} else if (indDir == 5) {
				posX--;
				posY--;
			}

			if ((isRepeated = validateRepeated(matrix[i][j], posX, posY, matrix))) {
				numRepetitions++;
			}
		} while (isRepeated && numRepetitions < (Constants.NUM_REPEAT_LETTER_MUTANT - 1));

		if (isRepeated) {
			numRepetitions++;
		}
		
		return numRepetitions;
	}

	private boolean validateRepeated(char value, int i, int j, char[][] matrix) {
		boolean isRepeated = i > -1 && j > -1 && i < matrix.length && j < matrix.length;
		return isRepeated && value == matrix[i][j];
	}
}
