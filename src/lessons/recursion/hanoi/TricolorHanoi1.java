package lessons.recursion.hanoi;

import java.awt.Color;
import java.util.Vector;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiEntity;
import plm.universe.hanoi.HanoiWorld;

public class TricolorHanoi1 extends ExerciseTemplated {

	public TricolorHanoi1(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		Vector<HanoiDisk> slot0 = HanoiDisk.generateHanoiDisks(new Integer[] {5,5,5,4,4,4,3,3,3,2,2,2,1,1,1});
		Vector<HanoiDisk> slot1 = HanoiDisk.generateHanoiDisks(new Integer[] {4,4,4,3,3,3,2,2,2,1,1,1});
		Vector<HanoiDisk> slot2 = HanoiDisk.generateHanoiDisks(new Integer[] {3,3,3,2,2,2,1,1,1});
		HanoiWorld w = new HanoiWorld(game, "moveStack(0,1,2)", 
				slot0, new Vector<HanoiDisk>(), new Vector<HanoiDisk>());
		w.setParameter(new Object[] {0,1,2});		
		for (int i=0; i<w.getSlotSize(0);i++) {
			Color c=Color.yellow;
			switch (i%3) {
			case 0: c = Color.white;  break;
			case 1: c = Color.yellow; break;
			case 2: c = Color.black;  break;
			}
			w.setColor(0, i, c);
		}
		myWorlds[0] = w;
		
		w = new HanoiWorld(game, "moveStack(1,2,0)", 
				new Vector<HanoiDisk>(), slot1, new Vector<HanoiDisk>());
		w.setParameter(new Object[] {1,2,0});		
		for (int i=0; i<w.getSlotSize(1);i++) {
			Color c=Color.yellow;
			switch (i%3) {
			case 0: c = Color.white;  break;
			case 1: c = Color.yellow; break;
			case 2: c = Color.black;  break;
			}
			w.setColor(1, i, c);
		}
		myWorlds[1] = w;

		w = new HanoiWorld(game, "moveStack(2,0,1)", 
				new Vector<HanoiDisk>(), new Vector<HanoiDisk>(), slot2);
		w.setParameter(new Object[] {2,0,1});		
		for (int i=0; i<w.getSlotSize(2);i++) {
			Color c=Color.yellow;
			switch (i%3) {
			case 0: c = Color.white;  break;
			case 1: c = Color.yellow; break;
			case 2: c = Color.black;  break;
			}
			w.setColor(2, i, c);
		}
		myWorlds[2] = w;

		
		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);
		
		setup(myWorlds);
	}
}
