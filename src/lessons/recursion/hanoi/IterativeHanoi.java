package lessons.recursion.hanoi;

import java.util.Vector;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiEntity;
import plm.universe.hanoi.HanoiWorld;

public class IterativeHanoi extends ExerciseTemplated {

	public IterativeHanoi(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		Vector<HanoiDisk> slot00 = HanoiDisk.generateHanoiDisks(new Integer[] {5,4,3,2,1});
		Vector<HanoiDisk> slot11 = HanoiDisk.generateHanoiDisks(new Integer[] {6,5,4,3,2,1});
		myWorlds[0] = new HanoiWorld(game, "hanoi(0,true)",  slot00, new Vector<HanoiDisk>(), new Vector<HanoiDisk>());
		myWorlds[0].setParameter(new Object[] {0,true});		
		myWorlds[1] = new HanoiWorld(game, "hanoi(1,true)",  new Vector<HanoiDisk>(), slot11, new Vector<HanoiDisk>());
		myWorlds[1].setParameter(new Object[] {1,true});		
		myWorlds[2] = new HanoiWorld(game, "hanoi(1,false)", new Vector<HanoiDisk>(), slot11, new Vector<HanoiDisk>());
		myWorlds[2].setParameter(new Object[] {1,false});		
		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);
		
		setup(myWorlds);
	}
}
