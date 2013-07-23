package lessons.maze.pledge;

import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

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
