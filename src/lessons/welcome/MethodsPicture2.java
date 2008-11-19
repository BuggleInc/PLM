package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class MethodsPicture2 extends ExerciseTemplated {

	public MethodsPicture2(Lesson lesson) {
		super(lesson);
		name = "Dessiner avec méthode (2)";
		BuggleWorld myWorld =  new BuggleWorld("World",15,15);
		new Buggle(myWorld, "Picasso", 0, 14, Direction.EAST, Color.black, Color.lightGray);

		UIDelay = 20;

		setup(myWorld);
	}
}
