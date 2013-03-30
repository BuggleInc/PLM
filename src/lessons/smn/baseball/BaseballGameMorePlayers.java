package lessons.smn.baseball;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.smn.baseball.BaseballEntity;
import jlm.universe.smn.baseball.BaseballWorld;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class BaseballGameMorePlayers extends ExerciseTemplated {

	/**
	 * Constructor of the class BaseballGame
	 * @param Lesson : a lesson
	 */
	public BaseballGameMorePlayers(Lesson lesson) {
		super(lesson);
		
		BaseballWorld sets[] = new BaseballWorld[4];
		
		sets[0]= new BaseballWorld("10 bases with three players each",10,3);
		sets[1]= new BaseballWorld("7 bases with four players each",7,4);
		sets[2]= new BaseballWorld("5 bases with five players each",5,5);
		sets[3]= new BaseballWorld("15 bases with two players each",15,2);
		
		for ( int i = 0 ; i<sets.length ; i++)
		{
			new BaseballEntity("Baseball Player",sets[i]);
		}
		
		setup(sets);
	}

}
