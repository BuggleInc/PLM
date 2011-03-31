package lessons.bat.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class SleepIn extends BatExercise {
	
	public SleepIn(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[4];
		myWorlds[0] = new BatWorld(VISIBLE,  false,false);
		myWorlds[1] = new BatWorld(VISIBLE,  true,false);
		myWorlds[2] = new BatWorld(INVISIBLE, false,true);
		myWorlds[3] = new BatWorld(INVISIBLE, true,true);

		setup(myWorlds,"sleepIn");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = sleepIn((Boolean)w.getParameter(0),(Boolean)w.getParameter(1));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
boolean sleepIn(boolean weekday, boolean vacation) {
		/* BEGIN SOLUTION */
		 if (!weekday || vacation) {
			 return true;
		 } else {
			 return false;
		 }
		 // This can be shortened to: return(!weekday || vacation);
		/* END SOLUTION */
}
		/* END TEMPLATE */
}
