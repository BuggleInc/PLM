package lessons.maze.pledge;

import java.io.IOException;
import java.util.Arrays;

import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class PledgeMaze extends ExerciseTemplated {

	public PledgeMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Escaper";

		World[] myWorlds = {
				((BuggleWorld) BuggleWorld.newFromFile("lessons/maze/pledge/PledgeMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile("lessons/maze/pledge/PledgeMaze2")).ignoreDirectionDifference()
		};

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(PledgeMazeEntity::new)).toList().toArray(new World[0]);
		setup(myWorlds);
	}
}
