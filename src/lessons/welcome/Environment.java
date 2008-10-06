package lessons.welcome;

import java.awt.Color;

import lessons.ExerciseTemplated;
import lessons.Lesson;
import bugglequest.core.Buggle;
import bugglequest.core.Direction;
import bugglequest.core.World;

public class Environment extends ExerciseTemplated {
	public Environment(Lesson lesson) {
		super(lesson);

		name = "Prise en main de l'environnement";
		tabName = "Program";
		
		World myWorld = new World("Training Camp",7,7);
		new Buggle(myWorld, "Noob", 3, 3, Direction.NORTH, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
