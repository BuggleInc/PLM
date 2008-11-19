package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class MethodsArgs extends ExerciseTemplated {

	public MethodsArgs(Lesson lesson) {
		super(lesson);
		name = "Méthodes avec des paramètres";
		tabName = "Program";

		BuggleWorld myWorld = new BuggleWorld("Buggles Party",7,7);
		new Buggle(myWorld, "Homer", 0, 6, Direction.NORTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Bart", 1, 2, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Lisa", 2, 4, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Maggie", 3, 1, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Marge", 4, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Itchy", 5, 3, Direction.NORTH, Color.black, Color.lightGray);
		new Buggle(myWorld, "Scratchy", 6, 5, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
