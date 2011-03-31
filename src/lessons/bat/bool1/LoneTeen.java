package lessons.bat.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class LoneTeen extends BatExercise {
	
	public LoneTeen(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[13];
		myWorlds[0] = new BatWorld(VISIBLE,  13,42);
		myWorlds[1] = new BatWorld(VISIBLE,  21,19);
		myWorlds[2] = new BatWorld(VISIBLE,  13,13);
		
		myWorlds[3] = new BatWorld(INVISIBLE, 14,20);
		myWorlds[4] = new BatWorld(INVISIBLE, 20,15);
		myWorlds[5] = new BatWorld(INVISIBLE, 16,17);
		myWorlds[6] = new BatWorld(INVISIBLE, 16,9);
		myWorlds[7] = new BatWorld(INVISIBLE, 16,18);
		myWorlds[8] = new BatWorld(INVISIBLE, 13,19);
		myWorlds[9] = new BatWorld(INVISIBLE, 13,20);
		myWorlds[10]= new BatWorld(INVISIBLE, 6,18);
		myWorlds[11]= new BatWorld(INVISIBLE, 42,13);
		myWorlds[12]= new BatWorld(INVISIBLE, 42,42);


		setup(myWorlds,"loneTeen");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = loneTeen((Integer)w.getParameter(0),(Integer)w.getParameter(1));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean loneTeen(int a, int b) {
	/* BEGIN SOLUTION */
	boolean teenA = a>12&&a<20;
	boolean teenB = b>12&&b<20;
	return  (teenA&&!teenB) || (teenB&&!teenA);
	/* END SOLUTION */
}
		/* END TEMPLATE */
}
