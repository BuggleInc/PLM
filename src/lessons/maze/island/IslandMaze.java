package lessons.maze.island;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

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
