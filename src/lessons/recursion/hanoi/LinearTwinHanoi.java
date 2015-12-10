package lessons.recursion.hanoi;

import java.awt.Color;
import java.util.Vector;

import lessons.recursion.hanoi.universe.HanoiDisk;
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
		Vector<HanoiDisk> slot00 = HanoiDisk.generateHanoiDisks(new Integer[] {6,5,4,3,2,1}, Color.black);
		Vector<HanoiDisk> slot02 = HanoiDisk.generateHanoiDisks(new Integer[] {6,5,4,3,2,1}, Color.white);
		HanoiWorld w = new HanoiWorld(game, "solve(0,1,2)",  
				slot00, new Vector<HanoiDisk>(), slot02);
		w.setParameter(new Object[] {0,1,2});
		myWorlds[0] = w;
		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);
		
		setup(myWorlds);
	}
}
