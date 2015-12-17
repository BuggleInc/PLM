package lessons.recursion.hanoi;

import java.awt.Color;
import java.util.Vector;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiEntity;
import plm.universe.hanoi.HanoiWorld;

public class TricolorHanoi2 extends ExerciseTemplated {

	public TricolorHanoi2(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld w;
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		Vector<HanoiDisk> slot0 = HanoiDisk.generateHanoiDisks(new Integer[] {4,3,2,1}, Color.white);
		Vector<HanoiDisk> slot1 = HanoiDisk.generateHanoiDisks(new Integer[] {4,3,2,1}, Color.yellow);
		Vector<HanoiDisk> slot2 = HanoiDisk.generateHanoiDisks(new Integer[] {4,3,2,1}, Color.black);
		w = new HanoiWorld(game, "gather(0,1,2)", slot0, slot1, slot2);
		w.setParameter(new Object[] {0,1,2});		
		myWorlds[0] = w;
		
		w = new HanoiWorld(game, "gather(1,2,0)", slot0, slot1, slot2);
		w.setParameter(new Object[] {1,2,0});		
		myWorlds[1] = w;

		w = new HanoiWorld(game, "gather(2,0,1)", slot0, slot1, slot2);
		w.setParameter(new Object[] {2,0,1});		
		myWorlds[2] = w;

		
		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);
		
		setup(myWorlds);
	}
}
