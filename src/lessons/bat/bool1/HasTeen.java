package lessons.bat.bool1;

import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class HasTeen extends BatExercise {
	
	public HasTeen(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[] {
				new BatWorld(VISIBLE,  13,20,10),
				new BatWorld(VISIBLE,  20,19,10),
				new BatWorld(VISIBLE,  20,10,13),

				new BatWorld(INVISIBLE, 1,20,12),
				new BatWorld(INVISIBLE, 19,20,12),
				new BatWorld(INVISIBLE, 12,20,19),
				new BatWorld(INVISIBLE, 12,9,20),
				new BatWorld(INVISIBLE, 12,18,20),
				new BatWorld(INVISIBLE, 14,2,20),
				new BatWorld(INVISIBLE, 4,2,20),
				new BatWorld(INVISIBLE, 11,22,22)
		};


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
