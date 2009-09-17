package lessons.bat.bool1;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class HasTeen extends BatExercise {
	
	public HasTeen(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[11];
		myWorlds[0] = new BatWorld(VISIBLE,  13,20,10);
		myWorlds[1] = new BatWorld(VISIBLE,  20,19,10);
		myWorlds[2] = new BatWorld(VISIBLE,  20,10,13);
		
		myWorlds[3] = new BatWorld(INVISIBLE, 1,20,12);
		myWorlds[4] = new BatWorld(INVISIBLE, 19,20,12);
		myWorlds[5] = new BatWorld(INVISIBLE, 12,20,19);
		myWorlds[6] = new BatWorld(INVISIBLE, 12,9,20);
		myWorlds[7] = new BatWorld(INVISIBLE, 12,18,20);
		myWorlds[8] = new BatWorld(INVISIBLE, 14,2,20);
		myWorlds[9] = new BatWorld(INVISIBLE, 4,2,20);
		myWorlds[10]= new BatWorld(INVISIBLE, 11,22,22);


		setup(myWorlds,"hasTeen");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = hasTeen((Integer)w.getParameter(0),(Integer)w.getParameter(1),(Integer)w.getParameter(2));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean hasTeen(int a, int b, int c) {
	/* BEGIN SOLUTION */
		  return a>12&&a<20 || b>12&&b<20 || c>12&&c<20;
	/* END SOLUTION */
}
		/* END TEMPLATE */
}
