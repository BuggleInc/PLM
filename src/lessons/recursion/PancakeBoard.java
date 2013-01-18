package lessons.recursion;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.pancake.PancakeEntity;
import jlm.universe.pancake.PancakeWorld;

public class PancakeBoard extends ExerciseTemplated {

	public PancakeBoard(Lesson lesson) {
		super(lesson);
		PancakeWorld space[] = new PancakeWorld[4];
		space[0]= new PancakeWorld("5 pancakes",5,true);
		space[1]= new PancakeWorld("10 pancakes",10,true);
		space[2]= new PancakeWorld("15 pancakes",15,true);
		space[3]= new PancakeWorld("30 pancakes",30,true);
		
		for ( int i = 0 ; i<4;i++)
		{
			new PancakeEntity("worker",space[i]);
		}

		setup(space);
	}

}
