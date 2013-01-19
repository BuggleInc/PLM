package lessons.recursion;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.pancake.PancakeEntity;
import jlm.universe.pancake.PancakeWorld;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @version 1.2
 */
public class PancakeBoard extends ExerciseTemplated {

	
	/**
	 * Constructor of the class PancakeBoard
	 * @version 1.2
	 * @param Lesson : a lesson
	 */
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
