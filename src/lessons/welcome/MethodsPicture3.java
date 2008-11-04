package lessons.welcome;

import java.awt.Color;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;


import lessons.ExerciseTemplated;
import lessons.Lesson;

public class MethodsPicture3 extends ExerciseTemplated {

	public MethodsPicture3(Lesson lesson) {
		super(lesson);
		name = "Dessiner avec méthode (3)";
		BuggleWorld myWorld =  new BuggleWorld("World",45,45);
		new Buggle(myWorld, "Picasso", 0, 44, Direction.EAST, Color.black, Color.lightGray);
		
		UIDelay = 5;
		
		setup(myWorld);
	}
}
