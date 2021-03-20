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
public class MutantVerifyUseCase {

	private final MutantRepoGateway mutantRepoGateway;

	public Mono<Boolean> verifyHumanIsMutant(List<String> dna) {
		return Utilities.listToMatrix(dna).map(matrix -> isMutant(matrix)).doOnNext(b -> saveMutant(dna, b));
	}

	private Mono<Boolean> saveMutant(List<String> dna, boolean isMutant) {
		String strDna = dna.stream().map(Object::toString).collect(Collectors.joining(Constants.HYPHEN));

		return mutantRepoGateway.saveMutant(Mutant.builder().dna(strDna).isMutant(isMutant).build());
	}

	public boolean isMutant(char[][] matrix) {
		boolean isMutant = Boolean.FALSE;

		// Nos ubicamos hacia el centro para iniciar la búsqueda
		int puntoInicio = (matrix[0].length % 2);
		for (int i = puntoInicio; !isMutant && i < matrix.length; i++) {
			for (int j = puntoInicio; !isMutant && j < matrix.length; j++) {
				int repeticiones = 0;
				int indDir = 0;
				while (indDir < Constants.NUM_DIRECCIONES && repeticiones < (Constants.NUM_REPEAT_LETTER_MUTANT - 1)) {
					// se validan las repeticiones en la misma coordenada,
					// arriba y abajo por ejemplo
					repeticiones = validarRepetidosEnDireccion(indDir, matrix[i][j], i, j, matrix);
					repeticiones += validarRepetidosEnDireccion(indDir, matrix[i][j], i, j, matrix);
				}

				isMutant = repeticiones > (Constants.NUM_REPEAT_LETTER_MUTANT - 1);
				if (isMutant) {
					System.out
							.println("La letra " + matrix[i][j] + " se encuentra repetida " + repeticiones + " veces");
				}
			}
		}
		return isMutant;
	}

	private int validarRepetidosEnDireccion(int indDir, char value, int posX, int posY, char[][] matrix) {

		int nroRepeticiones = 0;
		boolean isRepetido = Boolean.FALSE;
		do {
			// avanzo en la dirección indicada
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

			if ((isRepetido = validarRepetido(value, posX, posY, matrix))) {
				nroRepeticiones++;
			}
		} while (isRepetido && nroRepeticiones < (Constants.NUM_REPEAT_LETTER_MUTANT - 1));

		if (isRepetido) {
			nroRepeticiones++;
		}

		return nroRepeticiones;
	}

	private boolean validarRepetido(char value, int i, int j, char[][] matrix) {
		boolean isRepetido = i > -1 && j > -1 && i < matrix.length && j < matrix.length;
		return isRepetido && value == matrix[i][j];
	}
}
