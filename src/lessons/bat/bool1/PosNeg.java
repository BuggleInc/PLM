package lessons.bat.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class PosNeg extends BatExercise {
	
	public PosNeg(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[13];
		myWorlds[0] = new BatWorld(VISIBLE, -1,1,false);
		myWorlds[1] = new BatWorld(VISIBLE, 1,-1,false);
		myWorlds[2] = new BatWorld(VISIBLE, 1,1,false);
		
		myWorlds[3] = new BatWorld(INVISIBLE, -1,-1,false);
		myWorlds[4] = new BatWorld(INVISIBLE, 1,-1,true);
		myWorlds[5] = new BatWorld(INVISIBLE, -1,1,true);
		myWorlds[6] = new BatWorld(INVISIBLE, 1,1,true);
		myWorlds[7] = new BatWorld(INVISIBLE, -1,-1,true);
		myWorlds[8] = new BatWorld(INVISIBLE, 5,-5,true);
		myWorlds[9] = new BatWorld(INVISIBLE, -6,6,false);
		myWorlds[10]= new BatWorld(INVISIBLE, -5,-5,false);
		myWorlds[11]= new BatWorld(INVISIBLE, -5,5,true);
		myWorlds[12]= new BatWorld(INVISIBLE, -5,-5,true);

		setup(myWorlds,"posNeg");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = posNeg((Integer)w.getParameter(0),(Integer)w.getParameter(1),(Boolean)w.getParameter(2));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean posNeg(int a,int b,boolean negative) {
	
		/* BEGIN SOLUTION */
	  if (negative)
		  return a<0&&b<0;
	  return (a<0&&b>0) || (a>0&&b<0);
		  /* END SOLUTION */
}
		/* END TEMPLATE */
}
