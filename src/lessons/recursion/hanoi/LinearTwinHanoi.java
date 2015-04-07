package lessons.recursion.hanoi;

import java.awt.Color;

import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.recursion.hanoi.universe.HanoiWorld;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class LinearTwinHanoi extends ExerciseTemplated {

	public LinearTwinHanoi(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[1];
		HanoiWorld w = new HanoiWorld(game, "solve(0,1,2)",  
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
