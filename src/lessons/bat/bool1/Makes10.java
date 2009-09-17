package lessons.bat.bool1;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class Makes10 extends BatExercise {
	
	public Makes10(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[9];
		myWorlds[0] = new BatWorld(VISIBLE,  9,10);
		myWorlds[1] = new BatWorld(VISIBLE,  9,9);
		myWorlds[2] = new BatWorld(VISIBLE,  1,9);
		
		myWorlds[3] = new BatWorld(INVISIBLE, 10,1);
		myWorlds[4] = new BatWorld(INVISIBLE, 10,10);
		myWorlds[5] = new BatWorld(INVISIBLE, 8,2);
		myWorlds[6] = new BatWorld(INVISIBLE, 8,3);
		myWorlds[7] = new BatWorld(INVISIBLE, 10,42);
		myWorlds[8] = new BatWorld(INVISIBLE, 12,-2);


		setup(myWorlds,"makes10");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = makes10((Integer)w.getParameter(0),(Integer)w.getParameter(1));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean makes10(int a, int b) {
	/* BEGIN SOLUTION */
		  return a==10||b==10||(a+b)==10;
			/* END SOLUTION */
}
		/* END TEMPLATE */
}
