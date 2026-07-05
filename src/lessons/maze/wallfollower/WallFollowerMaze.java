package lessons.maze.wallfollower;

import java.io.IOException;
import java.util.Arrays;

import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class WallFollowerMaze extends ExerciseTemplated {

	public WallFollowerMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Escaper";

		World[] myWorlds = {
				((BuggleWorld) BuggleWorld.newFromFile("lessons/maze/wallfollower/WallFollowerMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile("lessons/maze/wallfollower/WallFollowerMaze2")).ignoreDirectionDifference()
		};

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(WallFollowerMazeEntity::new)).toList().toArray(new World[0]);
		setup(myWorlds);
	}
}
