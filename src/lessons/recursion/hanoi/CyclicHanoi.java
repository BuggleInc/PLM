package lessons.recursion.hanoi;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.recursion.hanoi.universe.HanoiWorld;

public class CyclicHanoi extends ExerciseTemplated {

	public CyclicHanoi(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[4];
		myWorlds[0] = new HanoiWorld("solve(0,2,1), 5 disks",  
				new Integer[] {5,4,3,2,1}, new Integer[0],new Integer[0]);
		myWorlds[0].setParameter(new Object[] {0,2,1});
		
		myWorlds[1] = new HanoiWorld("solve(0,2,1), 4 disks",  
				new Integer[] {4,3,2,1}, new Integer[0],new Integer[0]);
		myWorlds[1].setParameter(new Object[] {0,2,1});
		
		myWorlds[2] = new HanoiWorld("solve(0,2,1), 3 disks",  
				new Integer[] {3,2,1}, new Integer[0],new Integer[0]);
		myWorlds[2].setParameter(new Object[] {0,2,1});
		
		myWorlds[3] = new HanoiWorld("solve(0,2,1), 2 disks",  
				new Integer[] {2,1}, new Integer[0],new Integer[0]);
		myWorlds[3].setParameter(new Object[] {0,2,1});		
		
		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);		
		setup(myWorlds);
	}
}
