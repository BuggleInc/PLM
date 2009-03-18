package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.Direction;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;

public class Basics3Pas extends ExerciseTemplated {

	public Basics3Pas(Lesson lesson) {
		super(lesson);
		tabName="SourceCode";
		
		BuggleWorld myWorld = new BuggleWorld("Training World", 7,7);
		new Buggle(myWorld, "Rookie", 3, 3, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
