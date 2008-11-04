package lessons.welcome;

import java.awt.Color;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;


import lessons.ExerciseTemplated;
import lessons.Lesson;

public class Environment extends ExerciseTemplated {
	public Environment(Lesson lesson) {
		super(lesson);

		name = "Prise en main de l'environnement";
		tabName = "Program";
		
		BuggleWorld myWorld = new BuggleWorld("Training Camp",7,7);
		new Buggle(myWorld, "Noob", 3, 3, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
