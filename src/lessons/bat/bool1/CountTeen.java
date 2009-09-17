package lessons.bat.bool1;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class CountTeen extends BatExercise {
	
	public CountTeen(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[12];
		myWorlds[0] = new BatWorld(VISIBLE,  13,20,10,54);
		myWorlds[1] = new BatWorld(VISIBLE,  20,19,13,15);
		myWorlds[2] = new BatWorld(VISIBLE,  20,10,13,42);
		
		myWorlds[3] = new BatWorld(INVISIBLE, 1,20,12,54);
		myWorlds[4] = new BatWorld(INVISIBLE, 19,20,42,12);
		myWorlds[5] = new BatWorld(INVISIBLE, 12,16,20,19);
		myWorlds[6] = new BatWorld(INVISIBLE, 42,12,9,20);
		myWorlds[7] = new BatWorld(INVISIBLE, 12,18,19,14);
		myWorlds[8] = new BatWorld(INVISIBLE, 14,2,20,99);
		myWorlds[9] = new BatWorld(INVISIBLE, 4,11,2,20);
		myWorlds[10]= new BatWorld(INVISIBLE, 11,11,11,11);
		myWorlds[11]= new BatWorld(INVISIBLE, 15,15,15,15);

		setup(myWorlds,"countTeen");
	}


	/* BEGIN SKEL */
	public void run(World w) {
		BatWorld bw = (BatWorld) w;
		bw.result = countTeen((Integer)w.getParameter(0),(Integer)w.getParameter(1),(Integer)w.getParameter(2),(Integer)w.getParameter(3));		
	}
	/* END SKEL */
	
	/* BEGIN TEMPLATE */
int countTeen(int a, int b, int c,int d) {
	/* BEGIN SOLUTION */
	int ret=0;
	if (a>12&&a<20)
		ret+=1;
	if (b>12&&b<20)
		ret+=1;
	if (c>12&&c<20)
		ret+=1;
	if (d>12&&d<20)
		ret+=1;
	return ret; 
	/* END SOLUTION */
}
		/* END TEMPLATE */
}
