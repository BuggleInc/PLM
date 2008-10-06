package lessons.welcome;

import java.awt.Color;

import lessons.ExerciseTemplated;
import lessons.Lesson;
import bugglequest.core.Buggle;
import bugglequest.core.Direction;
import bugglequest.core.World;

public class MethodsPicture4 extends ExerciseTemplated {

	public MethodsPicture4(Lesson lesson) {
		super(lesson);
		name = "Encore des motifs à dessiner";
		World myWorld =  new World("World",8,8);
		
		for (int i=0;i<8;i++) {
			myWorld.getCell(i, 0).putTopWall();
			myWorld.getCell(0, i).putLeftWall();
		}
		
		new Buggle(myWorld, "Picasso", 0, 7, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
