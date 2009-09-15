package lessons.bat.bool;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class NearHundred extends BatExercise {
	
	public NearHundred(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[10];
		myWorlds[0] = new BatWorld(VISIBLE, 93);
		myWorlds[1] = new BatWorld(VISIBLE, 90);
		myWorlds[2] = new BatWorld(VISIBLE, 89);
		
		myWorlds[3] = new BatWorld(INVISIBLE, 110);
		myWorlds[4] = new BatWorld(INVISIBLE, 191);
		myWorlds[5] = new BatWorld(INVISIBLE, 189);
		myWorlds[6] = new BatWorld(INVISIBLE, 200);
		myWorlds[7] = new BatWorld(INVISIBLE, 210);
		myWorlds[8] = new BatWorld(INVISIBLE, 211);
		myWorlds[9] = new BatWorld(INVISIBLE, -100);

		setup(myWorlds,"nearHundred");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = nearHundred((Integer)w.getParameter(0));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean nearHundred(int n) {
	
		/* BEGIN SOLUTION */
	  return (90<=n && n<=110)||(190<=n&&n<=210);
		/* END SOLUTION */
}
		/* END TEMPLATE */
}
