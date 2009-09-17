package lessons.bat.bool1;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class In1020 extends BatExercise {
	
	public In1020(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[9];
		myWorlds[0] = new BatWorld(VISIBLE,  12,99);
		myWorlds[1] = new BatWorld(VISIBLE,  21,12);
		myWorlds[2] = new BatWorld(VISIBLE,  8,99);
		
		myWorlds[3] = new BatWorld(INVISIBLE, 99,10);
		myWorlds[4] = new BatWorld(INVISIBLE, 20,20);
		myWorlds[5] = new BatWorld(INVISIBLE, 21,21);
		myWorlds[6] = new BatWorld(INVISIBLE, 9,9);
		myWorlds[7] = new BatWorld(INVISIBLE, 10,42);
		myWorlds[8] = new BatWorld(INVISIBLE, 12,-2);


		setup(myWorlds,"in1020");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = in1020((Integer)w.getParameter(0),(Integer)w.getParameter(1));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean in1020(int a, int b) {
	/* BEGIN SOLUTION */
		  return a>9&&a<21 || b>9&&b<21;
	/* END SOLUTION */
}
		/* END TEMPLATE */
}
