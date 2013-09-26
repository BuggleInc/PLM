package lessons.recursion.hanoi;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.recursion.hanoi.universe.HanoiWorld;

public class HanoiBoard extends ExerciseTemplated {

	public HanoiBoard(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		myWorlds[0] = new HanoiWorld("solve(0,1)",  
				new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0],new Integer[0]);
		myWorlds[0].setParameter(new Object[] {0,1,2});		
		myWorlds[1] = new HanoiWorld("solve(2,0)", 
				new Integer[0] , new Integer[0],new Integer[] {8,7,6,5,4,3,2,1});
		myWorlds[1].setParameter(new Object[] {2,0,1});		
		myWorlds[2] = new HanoiWorld("solve(1,2)", 
				new Integer[0], new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0]);
		myWorlds[2].setParameter(new Object[] {1,2,0});		
		for (int i=0;i<myWorlds.length;i++) {
			new HanoiEntity("worker",myWorlds[i]);
		}
		
		setup(myWorlds);
	}
}
