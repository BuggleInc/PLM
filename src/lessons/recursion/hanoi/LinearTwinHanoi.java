package lessons.recursion.hanoi;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.recursion.hanoi.universe.HanoiWorld;

public class LinearTwinHanoi extends ExerciseTemplated {

	public LinearTwinHanoi(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[1];
		HanoiWorld w = new HanoiWorld("solve(0,1,2)",  
				new Integer[] {6,5,4,3,2,1}, new Integer[0],new Integer[] {6,5,4,3,2,1});
		w.setParameter(new Object[] {0,1,2});		
		for (int i=0; i<w.getSlotSize(0);i++) {
			w.setColor(0, i, Color.black);
			w.setColor(2, i, Color.white);
		}
		myWorlds[0] = w;
		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);
		
		setup(myWorlds);
	}
}
