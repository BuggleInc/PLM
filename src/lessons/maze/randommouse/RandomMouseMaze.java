package lessons.maze.randommouse;

import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

public class RandomMouseMaze extends ExerciseTemplated {

	public RandomMouseMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "RandomMouseMaze";
		
		setup( new World[] {
				BuggleWorld.newFromFile("lessons/maze/randommouse/RandomMouseMaze"),
				BuggleWorld.newFromFile("lessons/maze/randommouse/RandomMouseMaze2")
		});
	}
}
