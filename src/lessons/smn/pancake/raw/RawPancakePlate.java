package lessons.smn.pancake.raw;

import jlm.core.model.Game;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.smn.pancake.raw.PancakeEntity;
import jlm.universe.smn.pancake.raw.PancakeWorld;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class RawPancakePlate extends ExerciseTemplated {

	
	/**
	 * Constructor of the class BurnedPancakePlate
	 * @param Lesson : a lesson
	 */
	public RawPancakePlate(Lesson lesson) {
		super(lesson);
	
		addProgLanguage(Game.PYTHON);
		
		PancakeWorld plate[] = new PancakeWorld[4];
		plate[0]= new PancakeWorld("5 pancakes",5,true);
		plate[1]= new PancakeWorld("10 pancakes",10,true);
		plate[2]= new PancakeWorld("15 pancakes",15,true);
		plate[3]= new PancakeWorld("30 pancakes",30,true);
		for ( int i = 0 ; i<4;i++)
		{
			new PancakeEntity("Pancake Seller",plate[i]);
		}
		setup(plate);
	}

}
