package lessons.sort.pancake;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.pancake.PancakeEntity;
import plm.universe.pancake.PancakeWorld;

public class GatesPancake extends ExerciseTemplated {
	
	public GatesPancake(Game game, Lesson lesson) {
		super(game, lesson);
	
		PancakeWorld[] plate = new PancakeWorld[] {
				new PancakeWorld(game, "5 pancakes",new int[] {2,4,5,3,1},false),         // A+ H D-
				new PancakeWorld(game, "7 pancakes",new int[] {3,6, 1,2,8,5,4,7},false),  // C  E+ Cbis E+ Cbis
				new PancakeWorld(game, "8 pancakes",new int[] {5,2,7,4,1,6,8,3,},false),  // A+ A+ A+ E+ A- G H
				new PancakeWorld(game, "9 pancakes",new int[] {4,2,3,7,9,1,5,6,8},false), // B+ Cbis A- D+ E+ H
				new PancakeWorld(game, "15 pancackes",new int[] {7, 2, 3, 14, 9, 5, 1, 8, 10, 11, 6, 12, 15, 4, 13, },false), // A+ Cbis A+ D+ E+ G A- B+ E+ F- H
				new PancakeWorld(game, "random 15 pancakes", 15, false),
				new PancakeWorld(game, "30 pancakes",new int[] 
						{1, 5, 17, 8, 24, 27, 14, 4, 11, 10, 28, 2, 29, 25, 15, 20, 3, 18, 19, 7, 21, 12, 23, 22, 16, 26, 6, 9, 13, 30, },
						false),
						// A+ A+ D+ A+ A+ A- E- A+ C E- A+ A+ A+ D+ D+ E+ D+ F+ F- C B+ F- E+ D+ F+ H E- F+ E+ => miss Cbis and G
		};
		for ( int i = 0 ; i<plate.length;i++)
			new PancakeEntity("Bill",plate[i]);

		setup(plate);
	}

}
