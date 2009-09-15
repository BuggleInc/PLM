package lessons.bat.bool;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class IcyHot extends BatExercise {
	
	public IcyHot(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[6];
		myWorlds[0] = new BatWorld(VISIBLE, 120,-1);
		myWorlds[1] = new BatWorld(VISIBLE, -1,120);
		myWorlds[2] = new BatWorld(VISIBLE, 2,120);
		
		myWorlds[3] = new BatWorld(INVISIBLE, -1,100);
		myWorlds[4] = new BatWorld(INVISIBLE, -2,-2);
		myWorlds[5] = new BatWorld(INVISIBLE, 120,120);

		setup(myWorlds,"icyHot");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = icyHot((Integer)w.getParameter(0),(Integer)w.getParameter(1));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean icyHot(int temp1, int temp2) {
	
		/* BEGIN SOLUTION */
	  return temp1<0&&temp2>100 || temp1>100&&temp2<0;
		/* END SOLUTION */
}
		/* END TEMPLATE */
}
