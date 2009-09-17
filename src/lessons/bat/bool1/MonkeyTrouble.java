package lessons.bat.bool1;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class MonkeyTrouble extends BatExercise {
	
	public MonkeyTrouble(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[4];
		myWorlds[0] = new BatWorld(VISIBLE, true, true);
		myWorlds[1] = new BatWorld(VISIBLE, false, false);
		myWorlds[2] = new BatWorld(VISIBLE, true, false);
		myWorlds[3] = new BatWorld(INVISIBLE, false, true);
		
		setup(myWorlds,"monkeyTrouble");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = monkeyTrouble((Boolean)w.getParameter(0),(Boolean)w.getParameter(1));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
public boolean monkeyTrouble(boolean aSmile, boolean bSmile) {
	/* BEGIN SOLUTION */
	  if (aSmile && bSmile) {
		    return true;
		  }
		  if (!aSmile && !bSmile) {
		    return true;
		  }
		  return false;
    // This all can be shortened to just:
	// return ((aSmile && bSmile) || (!aSmile && !bSmile));
	/* END SOLUTION */		  
}	
	/* END TEMPLATE */
}
