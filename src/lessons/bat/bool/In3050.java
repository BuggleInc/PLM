package lessons.bat.bool;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class In3050 extends BatExercise {
	
	public In3050(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[12];
		myWorlds[0] = new BatWorld(VISIBLE,  30,31);
		myWorlds[1] = new BatWorld(VISIBLE,  30,41);
		myWorlds[2] = new BatWorld(VISIBLE,  40,50);
		
		myWorlds[3] = new BatWorld(INVISIBLE, 40,51);
		myWorlds[4] = new BatWorld(INVISIBLE, 39,50);
		myWorlds[5] = new BatWorld(INVISIBLE, 50,39);
		myWorlds[6] = new BatWorld(INVISIBLE, 40,39);
		myWorlds[7] = new BatWorld(INVISIBLE, 49,48);
		myWorlds[8] = new BatWorld(INVISIBLE, 50,40);
		myWorlds[9] = new BatWorld(INVISIBLE, 50,51);
		myWorlds[10]= new BatWorld(INVISIBLE, 35,36);
		myWorlds[11]= new BatWorld(INVISIBLE, 35,45);


		setup(myWorlds,"in3050");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = in3050((Integer)w.getParameter(0),(Integer)w.getParameter(1));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean in3050(int a, int b) {
	/* BEGIN SOLUTION */
		  return (a>29&&a<41 && b>29&&b<41) || (a>39&&a<51 && b>39&&b<51);
	/* END SOLUTION */
}
		/* END TEMPLATE */
}
