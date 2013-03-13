package lessons.smn.baseball;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.smn.baseball.BaseballEntity;
import jlm.universe.smn.baseball.BaseballWorld;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class BaseballGame extends ExerciseTemplated {

	/**
	 * Constructor of the class BaseballGame
	 * @param Lesson : a lesson
	 */
	public BaseballGame(Lesson lesson) {
		super(lesson);
	
	//	this.addProgLanguage(Game.SCRATCH);
		
		BaseballWorld sets[] = new BaseballWorld[3];
		
		sets[0]= new BaseballWorld("4 bases",4);
		sets[1]= new BaseballWorld("10 bases",10);
		sets[2]= new BaseballWorld("15 bases",15);
		
		for ( int i = 0 ; i<3;i++)
		{
			new BaseballEntity("Baseball Player",sets[i]);
		}
		
		setup(sets);
	}

}
