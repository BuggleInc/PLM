package lessons.hanoi;

import universe.hanoi.HanoiEntity;
import universe.hanoi.HanoiWorld;
import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;

public class HanoiBoard extends ExerciseTemplated {

	public HanoiBoard(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		myWorlds[0] = new HanoiWorld("solve(0,1)", 8);
		myWorlds[0].setParameter(new Object[] {0,1});		
		myWorlds[1] = new HanoiWorld("solve(0,2)", 8);
		myWorlds[1].setParameter(new Object[] {0,2});		
		myWorlds[2] = new HanoiWorld("solve(1,2)", 
				new Integer[0], new Integer[] {0,1,2,3,4,5,6,7}, new Integer[0]);
		myWorlds[2].setParameter(new Object[] {1,2});		
		for (int i=0;i<myWorlds.length;i++) {
			new HanoiEntity("worker",myWorlds[i]);
		}
		
		setup(myWorlds);
	}
}
