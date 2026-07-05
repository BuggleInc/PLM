package lessons.sort.pancake;

import lessons.sort.basic.bubble.AlgBubbleSort1Entity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.sort.pancake.universe.PancakeEntity;
import lessons.sort.pancake.universe.PancakeWorld;
import plm.universe.World;

import java.util.Arrays;

public class BurnedPancake extends ExerciseTemplated {

	public BurnedPancake(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new PancakeWorld[4];
		myWorlds[0]= new PancakeWorld("5 pancakes",5,true);
		myWorlds[1]= new PancakeWorld("10 pancakes",10,true);
		myWorlds[2]= new PancakeWorld("15 pancakes",15,true);
		myWorlds[3]= new PancakeWorld("30 pancakes",30,true);
		for ( int i = 0 ; i<4;i++)
			new PancakeEntity("Pancake Seller",myWorlds[i]);

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(BurnedPancakeEntity::new)).toList().toArray(new World[0]);
		setup(myWorlds);
	}

}
