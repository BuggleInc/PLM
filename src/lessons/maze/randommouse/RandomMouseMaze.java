package lessons.maze.randommouse;

import java.io.IOException;
import java.util.Arrays;

import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class RandomMouseMaze extends ExerciseTemplated {

	public RandomMouseMaze(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "RandomMouseMaze";

		World[] myWorlds = {
				((BuggleWorld) BuggleWorld.newFromFile("lessons/maze/randommouse/RandomMouseMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile("lessons/maze/randommouse/RandomMouseMaze2")).ignoreDirectionDifference()
		};

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(RandomMouseMazeEntity::new)).toList().toArray(new World[0]);
		setup(myWorlds);
	}
}
