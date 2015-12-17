package lessons.recursion.hanoi;

import java.util.Vector;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiEntity;
import plm.universe.hanoi.HanoiWorld;

public class HanoiBoard extends ExerciseTemplated {

	public HanoiBoard(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		
		Vector<HanoiDisk> slot = HanoiDisk.generateHanoiDisks(new Integer[] {8,7,6,5,4,3,2,1});
		
		myWorlds[0] = new HanoiWorld(game, "solve(0,1,2)",  
				slot, new Vector<HanoiDisk>(),new Vector<HanoiDisk>());
		myWorlds[0].setParameter(new Object[] {0,1,2});	
		myWorlds[1] = new HanoiWorld(game, "solve(2,0,1)", 
				new Vector<HanoiDisk>(), new Vector<HanoiDisk>(), slot);
		myWorlds[1].setParameter(new Object[] {2,0,1});		
		myWorlds[2] = new HanoiWorld(game, "solve(1,2,0)", 
				new Vector<HanoiDisk>(), slot, new Vector<HanoiDisk>());
		myWorlds[2].setParameter(new Object[] {1,2,0});		
		for (int i=0;i<myWorlds.length;i++) {
			new HanoiEntity("worker",myWorlds[i]);
		}
		
		setup(myWorlds);
	}
}
