package lessons.editor.editor;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class Editor extends ExerciseTemplated {
	
	public Editor(Game game, Lesson lesson) {
		super(game, lesson);
		
		BuggleWorld myWorld = new BuggleWorld(game, "Editor",10,10);
		new SimpleBuggle(myWorld, "NDP", 7, 3, Direction.NORTH, Color.GREEN, Color.lightGray);

		setup(myWorld);
	}
}
