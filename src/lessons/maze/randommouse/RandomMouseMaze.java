package lessons.maze.randommouse;

import java.io.IOException;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

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
