package lessons.welcome;

import java.awt.Color;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;


import lessons.ExerciseTemplated;
import lessons.Lesson;

public class MethodsPicture extends ExerciseTemplated {

	public MethodsPicture(Lesson lesson) {
		super(lesson);
		name = "Dessiner avec méthode";
		BuggleWorld myWorld =  new BuggleWorld("World",5,5);
		new Buggle(myWorld, "Picasso", 0, 4, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
