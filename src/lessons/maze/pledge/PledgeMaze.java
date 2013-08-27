package lessons.maze.pledge;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class PledgeMaze extends ExerciseTemplated {

	public PledgeMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Escaper";
				
		setup( new World[] {
				BuggleWorld.newFromFile("lessons/maze/pledge/PledgeMaze"),	
				BuggleWorld.newFromFile("lessons/maze/pledge/PledgeMaze2")
		});
	}
}
