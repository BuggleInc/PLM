package lessons.bat.bool;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class SumDouble extends BatExercise {
	
	public SumDouble(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[6];
		myWorlds[0] = new BatWorld(VISIBLE,  1,2);
		myWorlds[1] = new BatWorld(VISIBLE,  3,2);
		myWorlds[2] = new BatWorld(VISIBLE,  2,2);
		
		myWorlds[3] = new BatWorld(INVISIBLE, -1,0);
		myWorlds[4] = new BatWorld(INVISIBLE, 0,0);
		myWorlds[5] = new BatWorld(INVISIBLE, 0,1);


		setup(myWorlds,"sumDouble");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = sumDouble((Integer)w.getParameter(0),(Integer)w.getParameter(1));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
int sumDouble(int a, int b) {
	/* BEGIN SOLUTION */
	if (a==b)
		return (a+b)*2;
	return a+b;
			/* END SOLUTION */
}
		/* END TEMPLATE */
}
