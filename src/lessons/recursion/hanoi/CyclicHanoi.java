package lessons.recursion.hanoi;

import java.util.Vector;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiEntity;
import plm.universe.hanoi.HanoiWorld;

public class CyclicHanoi extends ExerciseTemplated {

	public CyclicHanoi(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[4];
		Vector<HanoiDisk> slot00 = HanoiDisk.generateHanoiDisks(new Integer[] {5,4,3,2,1});
		myWorlds[0] = new HanoiWorld(game, "solve(0,2,1), 5 disks",  
				slot00, new Vector<HanoiDisk>(),new Vector<HanoiDisk>());
		myWorlds[0].setParameter(new Object[] {0,2,1});
		
		Vector<HanoiDisk> slot10 = HanoiDisk.generateHanoiDisks(new Integer[] {4,3,2,1});
		myWorlds[1] = new HanoiWorld(game, "solve(0,2,1), 4 disks",  
				slot10, new Vector<HanoiDisk>(),new Vector<HanoiDisk>());
		myWorlds[1].setParameter(new Object[] {0,2,1});
		
		Vector<HanoiDisk> slot20 = HanoiDisk.generateHanoiDisks(new Integer[] {3,2,1});
		myWorlds[2] = new HanoiWorld(game, "solve(0,2,1), 3 disks",  
				slot20, new Vector<HanoiDisk>(),new Vector<HanoiDisk>());
		myWorlds[2].setParameter(new Object[] {0,2,1});
		
		Vector<HanoiDisk> slot30 = HanoiDisk.generateHanoiDisks(new Integer[] {2,1});
		myWorlds[3] = new HanoiWorld(game, "solve(0,2,1), 2 disks",  
				slot30, new Vector<HanoiDisk>(),new Vector<HanoiDisk>());
		myWorlds[3].setParameter(new Object[] {0,2,1});		
		
		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);		
		setup(myWorlds);
	}
}
