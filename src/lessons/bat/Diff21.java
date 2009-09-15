package lessons.bat;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class Diff21 extends BatExercise {
	
	public Diff21(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[10];
		myWorlds[0] = new BatWorld(VISIBLE,  2);
		myWorlds[1] = new BatWorld(VISIBLE,  11);
		myWorlds[2] = new BatWorld(VISIBLE,  0);
		
		myWorlds[3] = new BatWorld(INVISIBLE, 19);
		myWorlds[4] = new BatWorld(INVISIBLE, 10);
		myWorlds[5] = new BatWorld(INVISIBLE, 21);
		myWorlds[6] = new BatWorld(INVISIBLE, 22);
		myWorlds[7] = new BatWorld(INVISIBLE, 25);
		myWorlds[8] = new BatWorld(INVISIBLE, 30);
		myWorlds[9] = new BatWorld(INVISIBLE, -21);

		setup(myWorlds,"diff21");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = diff21((Integer)w.getParameter(0));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
int diff21(int n) {
	/* BEGIN SOLUTION */
	  if (n>21)
		  return 2*(n-21);
	  return 21-n;
			/* END SOLUTION */
}
		/* END TEMPLATE */
}
