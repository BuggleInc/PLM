package lessons.sort.pancake;

import lessons.sort.pancake.universe.PancakeEntity;
import lessons.sort.pancake.universe.PancakeWorld;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class BubblePancake extends ExerciseTemplated {

	public BubblePancake(Game game, Lesson lesson) {
		super(game, lesson);
	
		PancakeWorld[] plate = new PancakeWorld[] {
				new PancakeWorld(game, "5 pancakes",new int[] {5,2,3,4,1},false),
				new PancakeWorld(game, "10 pancakes",new int[] {10,3,8,1,9,5,4,7,6,2},false),
				new PancakeWorld(game, "15 pancakes",new int[] {8,12,13,14,11,3,2,7,6,15,5,4,9,10,1},false),
				new PancakeWorld(game, "30 pancakes",new int[] 
						{5,29,8,9,27,25,4,11,3,28,7,19,18,10,16,12,26,22,15,23,13,30,2,24,14,20,6,1,17,21},
						false),
		};
		for ( int i = 0 ; i<plate.length;i++)
			new PancakeEntity("Pancake Seller",plate[i]);

		setup(plate);
	}

}
