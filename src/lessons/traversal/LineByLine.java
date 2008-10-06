package lessons.traversal;

import java.awt.Color;

import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;

import lessons.ExerciseTemplated;
import lessons.Lesson;

public class LineByLine extends ExerciseTemplated {

	public LineByLine(Lesson lesson) {
		super(lesson);
		name = "Parcours ligne par ligne";
		tabName = "LineByLine";

		World myWorld = new World("Grid",7,7);
		for (int i=0; i<7;i++) {
			myWorld.getCell(i, 0).putTopWall();
			myWorld.getCell(0, i).putLeftWall();
		}
		
		new Buggle(myWorld, "Walker", 0, 0, Direction.NORTH, Color.black, Color.red);

		setup(myWorld);
	}
}
