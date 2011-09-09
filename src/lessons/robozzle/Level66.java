package lessons.robozzle;

import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.robozzle.Robozzle;
import jlm.universe.robozzle.RobozzleExercise;
import jlm.universe.robozzle.RobozzleWorld;
import jlm.universe.robozzle.RobozzleWorldCell;

public class Level66 extends RobozzleExercise {
	
	public Level66(Lesson lesson) {
		super(lesson);
		tabName = "Level66";
		
		RobozzleWorld myWorlds[] = new RobozzleWorld[1];
		myWorlds[0] = new RobozzleWorld("Level66", 16, 12); 
		parseLevel(myWorlds[0], 16, 12,"     rbR             RbbbR           RbR             RbbbbbbbR       RbbbbbR         RbR             BbB             BbbbbbB         BbbbbbbbB       BbB             BbbbB           BbB        ");
		
		new Robozzle(myWorlds[0], "Scotty", 5, 0, Direction.EAST);
		
		// max-functions : 5
		// max-length : 10
		
		setup(myWorlds);
	}

	private void parseLevel(RobozzleWorld myWorld, int width, int height, String data) {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
            	char conf = data.charAt(y * width + x);
            	myWorld.setCell(new RobozzleWorldCell(myWorld, x, y, conf, false, false, Character.isUpperCase(conf)), x, y);	
            }
        }
 
        // put the wall		
	}
	
}
