package lessons.sort.baseball;

import lessons.recursion.lego.dragoncurve.DragonCurve2Entity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.sort.baseball.universe.BaseballWorld;
import plm.universe.World;

import java.util.Arrays;

public class BubbleBaseball extends ExerciseTemplated {

	public BubbleBaseball(Lesson lesson) {
		super(lesson);

		World[] myWorlds = {
				new BaseballWorld("5 bases, 2 positions", 5, 2),
				new BaseballWorld("5 bases, 3 positions", 5, 3),
				new BaseballWorld("6 bases, 2 positions", 6, 2),
				new BaseballWorld("6 bases, 3 positions", 6, 3),
				new BaseballWorld("7 bases, 2 positions", 7, 2),
				new BaseballWorld("7 bases, 3 positions", 7, 3),
				new BaseballWorld("7 bases, 4 positions", 7, 4),
				new BaseballWorld("8 bases, 2 positions", 8, 2),
				new BaseballWorld("8 bases, 3 positions", 8, 3),
		};

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(BubbleBaseballEntity::new)).toList().toArray(new World[0]);
		setup(myWorlds);
	}

}
