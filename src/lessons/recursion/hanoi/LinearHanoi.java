package lessons.recursion.hanoi;

import java.util.Vector;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiEntity;
import plm.universe.hanoi.HanoiWorld;

public class LinearHanoi extends ExerciseTemplated {

	public LinearHanoi(Game game, Lesson lesson) {
		super(game, lesson);

		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[2];
		Vector<HanoiDisk> slot = HanoiDisk.generateHanoiDisks(new Integer[] {6,5,4,3,2,1});
		myWorlds[0] = new HanoiWorld(game, "solve(0,1,2)",  
				slot, new Vector<HanoiDisk>(),new Vector<HanoiDisk>());
		myWorlds[0].setParameter(new Object[] {0,1,2});		
		myWorlds[1] = new HanoiWorld(game, "solve(2,1,0)", 
				new Vector<HanoiDisk>(), new Vector<HanoiDisk>(), slot);
		myWorlds[1].setParameter(new Object[] {2,1,0});		

		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);
		
		setup(myWorlds);
	}
}
