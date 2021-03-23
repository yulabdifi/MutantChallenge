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

	/**
	 * Verify human DNA and check if it is mutant. Transform the array from string
	 * to matrix and validate them
	 * 
	 * @param dna is List<String>
	 * @return Mono<Boolean>
	 */
	public Mono<Boolean> verifyHumanIsMutant(List<String> dna) {
		return Utilities.listToMatrix(dna).map(matrix -> isMutant(matrix)).flatMap(b -> saveMutant(dna, b));
	}

	/**
	 * Reads human DNA as matrix to verify if human is mutant
	 * 
	 * @param matrix
	 * @return boolean
	 */
	private boolean isMutant(char[][] matrix) {
		boolean isMutant = Boolean.FALSE;

		// La busqueda se suspende una vez se detecte que es mutante
		for (int i = 0; !isMutant && i < matrix.length; i++) {
			for (int j = 0; !isMutant && j < matrix.length; j++) {
				int numRep = 0;
				// Se evaluaran las letras repetidas consecutivamente en 6 diferentes
				// coordenadas (arriba,
				// abajo, atras, adelante, diagonal arriba y diagonal abajo)
				int indDir = 0;
				while (indDir < Constants.NUM_COORDINATES && numRep < (Constants.NUM_REPEAT_LETTER_MUTANT - 1)) {
					// se validan las repeticiones en la misma coordenada,
					// arriba y abajo por ejemplo
					numRep = validateRepeatedByCoordinate(indDir++, i, j, matrix, 0);
					numRep = validateRepeatedByCoordinate(indDir++, i, j, matrix, numRep);
				}
				// si el numero de repeticiones es mayor o igual al numero de letras del ADN (4)
				// que indican que el humano es un mutante
				isMutant = numRep > (Constants.NUM_REPEAT_LETTER_MUTANT - 1);
			}
		}

		return isMutant;
	}

	/**
	 * Save mutant check up
	 * 
	 * @param dna
	 * @param isMutant
	 * @return Mono<Boolean>
	 */
	private Mono<Boolean> saveMutant(List<String> dna, boolean isMutant) {
		// La secuencia de adn se almacenará como string con cada palabra separada por
		// guión
		String strDna = dna.stream().map(Object::toString).collect(Collectors.joining(Constants.HYPHEN));
		// Finalmente retornará si el humano es mutante, mas no la respuesta del
		// almacenamiento
		return mutantRepoGateway.saveMutant(Mutant.builder().dna(strDna).isMutant(isMutant).build()).map(b -> isMutant);
	}

	/**
	 * Validate letters repeated consecutively by coordinates
	 * 
	 * @param indDir
	 * @param i
	 * @param j
	 * @param matrix
	 * @param numRepetitions
	 * @return numRepetitions is Integer
	 */
	private int validateRepeatedByCoordinate(int indDir, final int i, final int j, char[][] matrix,
			int numRepetitions) {
		// Avanzo a partir de la ubicacion actual
		int posX = i;
		int posY = j;
		boolean isRepeated = Boolean.FALSE;
		do {
			// avanzo en la direccion o coordenada indicada
			switch (indDir) {
			case 0:
				posX++;
				break;
			case 1:
				posX--;
				break;
			case 2:
				posY++;
				break;
			case 3:
				posY--;
				break;
			case 4:
				posX++;
				posY++;
				break;
			case 5:
				posX--;
				posY--;
				break;
			default:
				posX = matrix.length;
				posY = matrix.length;
				break;
			}
			// Se evalua si el valor actual es igual al valor de la posicion [posX][posY]
			if ((isRepeated = validateRepeated(matrix[i][j], posX, posY, matrix))) {
				numRepetitions++;
			}
			// Se repite mientras sigan apareciendo repetidos en la coordinada y no se
			// alcance el numero de repeticiones requerido
		} while (isRepeated && numRepetitions < (Constants.NUM_REPEAT_LETTER_MUTANT - 1));

		if (isRepeated) {
			numRepetitions++;
		}

		return numRepetitions;
	}

	/**
	 * Evaluates current value against the value of the indicated positions
	 * 
	 * @param value
	 * @param i
	 * @param j
	 * @param matrix
	 * @return boolean
	 */
	private boolean validateRepeated(char value, int i, int j, char[][] matrix) {
		boolean isRepeated = i > -1 && j > -1 && i < matrix.length && j < matrix.length;
		return isRepeated && value == matrix[i][j];
	}
}
