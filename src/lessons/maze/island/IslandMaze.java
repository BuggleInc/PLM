package lessons.maze.island;

import java.io.IOException;
import java.util.Arrays;

import lessons.turmites.helloturmite.HelloTurmiteEntity;
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
		World[] myWorlds = {
				((BuggleWorld) BuggleWorld.newFromFile("lessons/maze/island/IslandMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile("lessons/maze/island/IslandMaze2")).ignoreDirectionDifference()
		};

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(IslandMazeEntity::new)).toList().toArray(new World[0]);
		setup(myWorlds);
	}
}
