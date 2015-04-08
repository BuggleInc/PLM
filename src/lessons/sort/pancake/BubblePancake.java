package lessons.sort.pancake;

import lessons.sort.pancake.universe.PancakeEntity;
import lessons.sort.pancake.universe.PancakeWorld;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class BubblePancake extends ExerciseTemplated {

	public BubblePancake(Game game, Lesson lesson) {
		super(game, lesson);
	
		PancakeWorld plate[] = new PancakeWorld[4];
		plate[0]= new PancakeWorld(game, "5 pancakes",5,false);
		plate[1]= new PancakeWorld(game, "10 pancakes",10,false);
		plate[2]= new PancakeWorld(game, "15 pancakes",15,false);
		plate[3]= new PancakeWorld(game, "30 pancakes",30,false);
		for ( int i = 0 ; i<4;i++)
			new PancakeEntity("Pancake Seller",plate[i]);

		setup(plate);
	}

}
