package lessons.maze.island;

import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

public class IslandMaze extends ExerciseTemplated {

	public IslandMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Escaper";
				
		/* Create initial situation */
		setup( new World[] {
				BuggleWorld.newFromFile("lessons/maze/island/IslandMaze"),
				BuggleWorld.newFromFile("lessons/maze/island/IslandMaze2")
		});
	}
}
