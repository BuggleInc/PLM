package lessons.welcome.methods.returning;

import java.awt.Color;
import java.util.Arrays;

import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class MethodsReturning extends ExerciseTemplated {

	public MethodsReturning(Lesson lesson) {
		super(lesson);
		tabName = "Program";

		BuggleWorld[] myWorlds = new BuggleWorld[3];
		for (int i=0; i<3;i++) {
			myWorlds[i] = new BuggleWorld("World "+(i+1),7,7);
			new SimpleBuggle(myWorlds[i], "Searcher", 0, 6, Direction.NORTH, Color.black, Color.lightGray);
		}

		try {
			myWorlds[0].addBaggle(3, 2);
			myWorlds[1].addBaggle(5, 1);
			myWorlds[2].addBaggle(2, 6);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(MethodsReturningEntity::new)).toList().toArray(new BuggleWorld[0]);
		setup(myWorlds);
	}
}
