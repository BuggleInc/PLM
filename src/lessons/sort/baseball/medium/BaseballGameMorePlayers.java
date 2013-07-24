package lessons.sort.baseball.medium;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import lessons.sort.baseball.universe.BaseballEntity;
import lessons.sort.baseball.universe.BaseballWorld;

public class BaseballGameMorePlayers extends ExerciseTemplated {

	/**
	 * Constructor of the class BaseballGame
	 * @param Lesson : a lesson
	 */
	public BaseballGameMorePlayers(Lesson lesson) {
		super(lesson);
		
		BaseballWorld sets[] = new BaseballWorld[5];
		
		sets[0]= new BaseballWorld("12 bases with three players each",12,3);
		sets[1]= new BaseballWorld("10 bases with four players each",10,4);
		sets[2]= new BaseballWorld("7 bases with five players each",7,5);
		sets[3]= new BaseballWorld("5 bases with eight players each",5,8);
		sets[4]= new BaseballWorld("15 bases with two players each",15,2);
		
		for ( int i = 0 ; i<sets.length ; i++)
		{
			new BaseballEntity("Baseball Player",sets[i]);
		}

		setup(sets);
	}

}
