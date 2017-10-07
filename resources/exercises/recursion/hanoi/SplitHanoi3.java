package recursion.hanoi;

import java.awt.Color;
import java.util.Vector;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiEntity;
import plm.universe.hanoi.HanoiWorld;

public class SplitHanoi3 extends ExerciseTemplated {

	public SplitHanoi3() {
		super("SplitHanoi3", "SplitHanoi3");
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		HanoiWorld w;
		Vector<HanoiDisk> slot0 = HanoiDisk.generateHanoiDisks(new Integer[] {7,7,6,6,5,5,4,4,3,3,2,2,1,1}, Color.black);
		Vector<HanoiDisk> slot1 = HanoiDisk.generateHanoiDisks(new Integer[] {6,6,5,5,4,4,3,3,2,2,1,1}, Color.black);
		Vector<HanoiDisk> slot2 = HanoiDisk.generateHanoiDisks(new Integer[] {5,5,4,4,3,3,2,2,1,1}, Color.black);
		w = new HanoiWorld("solve(3,2,0,1)",  
				new Vector<HanoiDisk>(), new Vector<HanoiDisk>(), new Vector<HanoiDisk>(), slot0);
		for (int i=0; i<w.getSlotSize(3);i++) {
			if (i%2==0) {
				w.setColor(3,i,Color.white);
			}
		}
		w.setParameter(new Integer[]{3,2,0,1});
		myWorlds[0] = w;
		
		w = new HanoiWorld("solve(1,3,0,2)",
				new Vector<HanoiDisk>(), slot1, new Vector<HanoiDisk>(), new Vector<HanoiDisk>());
		for (int i=0; i<w.getSlotSize(1);i++) {
			if (i%2==0) {
				w.setColor(1,i,Color.white);
			}
		}
		w.setParameter(new Integer[]{1,3,0,2});
		myWorlds[1] = w;
		
		w = new HanoiWorld("solve(2,1,0,3)",
				new Vector<HanoiDisk>(), new Vector<HanoiDisk>(), slot2, new Vector<HanoiDisk>());
		for (int i=0; i<w.getSlotSize(2);i++) {
			if (i%2==0) {
				w.setColor(2,i,Color.white);
			}
		}
		w.setParameter(new Integer[]{2,1,0,3});
		myWorlds[2] = w;
		
		for (int i=0;i<myWorlds.length;i++) {
			new HanoiEntity("worker",myWorlds[i]);
		}
		
		setup(myWorlds);
	}
}
