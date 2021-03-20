package com.ml;

import java.util.Arrays;
import java.util.Scanner;

import com.ml.model.util.Utilities;
import com.ml.usecase.MutantVerifyUseCase;

public class ChallengeApp {

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		String[] dna = new String[6];

		System.out.println("Por favor ingrese la secuencia de adn");
		Scanner in = new Scanner(System.in);
		for (int i = 0; i < dna.length; i++) {
			System.out.println(i + ":");
			dna[i] = in.nextLine();
		}

		MutantVerifyUseCase useCase = new MutantVerifyUseCase(null);
		Utilities.listToMatrix(Arrays.asList(dna)).map(m -> useCase.isMutant(m)).map(b -> b ? "verdad" : "falso")
				.subscribe(
						s -> System.out.println("De acuerdo a la secuencia es " + s + " que el humano sea mutante."));
	}

}
