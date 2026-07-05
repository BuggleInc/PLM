package lessons.sort.baseball;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.sort.baseball.universe.BaseballWorld;
import plm.universe.World;

import java.util.Arrays;

public class NaiveBaseball extends ExerciseTemplated {

	public NaiveBaseball(Lesson lesson) {
		super(lesson);

		World[] myWorlds = {
				new BaseballWorld("Field 1", 4, 2, BaseballWorld.MIX_NOBODY_HOME),
				new BaseballWorld("Field 2", 4, 2, BaseballWorld.MIX_NOBODY_HOME)
		};

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(NaiveBaseballEntity::new)).toList().toArray(new World[0]);
		setup(myWorlds);
	}

}
