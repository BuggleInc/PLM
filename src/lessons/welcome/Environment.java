package lessons.welcome;

import java.awt.Color;

import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;

import lessons.ExerciseTemplated;
import lessons.Lesson;

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
