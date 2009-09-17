package lessons.bat.bool1;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class Close10 extends BatExercise {
	
	public Close10(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[7];
		myWorlds[0] = new BatWorld(VISIBLE,  8,13);
		myWorlds[1] = new BatWorld(VISIBLE,  13,8);
		myWorlds[2] = new BatWorld(VISIBLE,  13,7);
		
		myWorlds[3] = new BatWorld(INVISIBLE, 7,13);
		myWorlds[4] = new BatWorld(INVISIBLE, 5,21);
		myWorlds[5] = new BatWorld(INVISIBLE, 0,20);
		myWorlds[6] = new BatWorld(INVISIBLE, 10,10);

		setup(myWorlds,"close10");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = close10((Integer)w.getParameter(0),(Integer)w.getParameter(1));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
int close10(int a, int b) {
	/* BEGIN SOLUTION */
	if (Math.abs(a-10)==Math.abs(b-10))
		return 0;
	if (Math.abs(a-10)<Math.abs(b-10))
		return a;
	return b;
	/* END SOLUTION */
}
		/* END TEMPLATE */
}
