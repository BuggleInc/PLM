package lessons.sort.pancake;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.sort.pancake.universe.PancakeEntity;
import lessons.sort.pancake.universe.PancakeWorld;

public class BurnedPancake extends ExerciseTemplated {

	public BurnedPancake(Game game, Lesson lesson) {
		super(game, lesson);
		
		PancakeWorld[] plate = new PancakeWorld[] {
				new PancakeWorld(game, "5 pancakes",new int[] {2,3,1,5,4},true),
				new PancakeWorld(game, "10 pancakes",new int[] {3,9,8,7,2,5,1,4,6,10},true),
				new PancakeWorld(game, "15 pancakes",new int[] {13,14,15,3,9,10,4,8,12,6,7,1,5,11,2},true),
				new PancakeWorld(game, "30 pancakes",new int[] 
						{2,29,11,27,16,25,18,15,14,21,30,28,3,7,26,22,23,13,4,19,10,17,5,9,6,8,12,24,20,1},
						true),
		};
		for ( int i = 0 ; i<plate.length;i++)
			new PancakeEntity("Pancake Seller",plate[i]);

		setup(plate);
	}

}
