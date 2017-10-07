package sort.pancake;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.pancake.PancakeEntity;
import plm.universe.pancake.PancakeWorld;

public class CohenPancake extends ExerciseTemplated {
	
	public CohenPancake() {
		super("CohenPancake", "CohenPancake");

		PancakeWorld[] plate = new PancakeWorld[] {
				new PancakeWorld("5 pancakes",new int[] {2,4,5,3,1},true),         // A+ H D-
				new PancakeWorld("upside down",new int[] {-1,-2,-3,-4,-5,-6,-7,-8,-9,-10},true),
				new PancakeWorld("7 pancakes",new int[] {3,6, 1,2,5,4,7},true),  // C  E+ Cbis E+ Cbis
				new PancakeWorld("8 pancakes",new int[] {5,2,7,4,1,6,8,3,},true),  // A+ A+ A+ E+ A- G H
				new PancakeWorld("9 pancakes",new int[] {4,2,3,7,9,1,5,6,8},true), // B+ Cbis A- D+ E+ H
				new PancakeWorld("15 pancackes",new int[] {7, 2, 3, 14, 9, 5, 1, 8, 10, 11, 6, 12, 15, 4, 13, },true), // A+ Cbis A+ D+ E+ G A- B+ E+ F- H
				new PancakeWorld("random 15 pancakes", 15, true),
				new PancakeWorld("30 pancakes",new int[] 
						{1, 5, 17, 8, 24, 27, 14, 4, 11, 10, 28, 2, 29, 25, 15, 20, 3, 18, 19, 7, 21, 12, 23, 22, 16, 26, 6, 9, 13, 30, },
						true),
						// A+ A+ D+ A+ A+ A- E- A+ C E- A+ A+ A+ D+ D+ E+ D+ F+ F- C B+ F- E+ D+ F+ H E- F+ E+ => miss Cbis and G
		};
		for ( int i = 0 ; i<plate.length;i++)
			new PancakeEntity("Bill",plate[i]);

		setup(plate);
	}

}
