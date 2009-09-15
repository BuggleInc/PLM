package lessons.bat.bool;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class ParotTrouble extends BatExercise {
	
	public ParotTrouble(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[8];
		myWorlds[0] = new BatWorld(VISIBLE,  true,6);
		myWorlds[1] = new BatWorld(VISIBLE,  true,7);
		myWorlds[2] = new BatWorld(VISIBLE,  false,6);
		
		myWorlds[3] = new BatWorld(INVISIBLE, true,21);
		myWorlds[4] = new BatWorld(INVISIBLE, false,21);
		myWorlds[5] = new BatWorld(INVISIBLE, true,23);
		myWorlds[6] = new BatWorld(INVISIBLE, false,23);
		myWorlds[7] = new BatWorld(INVISIBLE, true,20);


		setup(myWorlds,"parotTrouble");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = parotTrouble((Boolean)w.getParameter(0),(Integer)w.getParameter(1));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean parotTrouble(boolean talking, int hour) {
		/* BEGIN SOLUTION */
	  return (talking && (hour<7||hour>20));	
		/* END SOLUTION */
}
		/* END TEMPLATE */
}
