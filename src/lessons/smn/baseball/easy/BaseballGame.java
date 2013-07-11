package lessons.smn.baseball.easy;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.smn.baseball.BaseballEntity;
import jlm.universe.smn.baseball.BaseballWorld;

public class BaseballGame extends ExerciseTemplated {

	public BaseballGame(Lesson lesson) {
		super(lesson);
		
		BaseballWorld sets[] = new BaseballWorld[3];
		
		sets[0]= new BaseballWorld("4 bases",4,2);
		sets[1]= new BaseballWorld("10 bases",10,2);
		sets[2]= new BaseballWorld("15 bases",15,2);
		
		for ( int i = 0 ; i<3;i++)
		{
			new BaseballEntity("Baseball Player",sets[i]);
		}
		
		setup(sets);
	}

}
