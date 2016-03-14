package lessons.sort.pancake;

import lessons.sort.pancake.universe.PancakeEntity;
import lessons.sort.pancake.universe.PancakeWorld;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class BasicPancake extends ExerciseTemplated {
	
	public BasicPancake(Game game, Lesson lesson) {
		super(game, lesson);
	
		PancakeWorld[] plate = new PancakeWorld[] {
				new PancakeWorld(game, "5 pancakes",new int[] {5,1,3,4,2},false),
				new PancakeWorld(game, "10 pancakes",new int[] {1,9,2,7,10,5,4,6,8,3},false),
				new PancakeWorld(game, "15 pancakes",new int[] {14,3,13,10,11,12,2,8,7,6,1,15,5,9,4},false),
				new PancakeWorld(game, "30 pancakes",new int[] 
						{13,30,28,11,26,14,24,3,23,21,20,29,18,22,15,16,25,19,4,27,10,5,1,7,6,9,12,2,17,8},
						false),
		};
		for ( int i = 0 ; i<plate.length;i++)
			new PancakeEntity("Pancake Seller",plate[i]);

		setup(plate);
	}

}
