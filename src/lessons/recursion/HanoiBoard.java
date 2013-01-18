package lessons.recursion;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.hanoi.HanoiEntity;
import jlm.universe.hanoi.HanoiWorld;

public class HanoiBoard extends ExerciseTemplated {

	public HanoiBoard(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		myWorlds[0] = new HanoiWorld("solve(0,1)",  
				new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0],new Integer[0]);
		myWorlds[0].setParameter(new Object[] {0,1});		
		myWorlds[1] = new HanoiWorld("solve(0,2)", 
				new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0],new Integer[0]);
		myWorlds[1].setParameter(new Object[] {0,2});		
		myWorlds[2] = new HanoiWorld("solve(1,2)", 
				new Integer[0], new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0]);
		myWorlds[2].setParameter(new Object[] {1,2});		
		for (int i=0;i<myWorlds.length;i++) {
			new HanoiEntity("worker",myWorlds[i]);
		}
		
		setup(myWorlds);
	}
}
