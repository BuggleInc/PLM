package recursion.hanoi;

import java.awt.Color;
import java.util.Vector;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiEntity;
import plm.universe.hanoi.HanoiWorld;

public class LinearTwinHanoi extends ExerciseTemplated {

	public LinearTwinHanoi() {
		super("LinearTwinHanoi", "LinearTwinHanoi");

		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[1];
		Vector<HanoiDisk> slot00 = HanoiDisk.generateHanoiDisks(new Integer[] {6,5,4,3,2,1}, Color.black);
		Vector<HanoiDisk> slot02 = HanoiDisk.generateHanoiDisks(new Integer[] {6,5,4,3,2,1}, Color.white);
		HanoiWorld w = new HanoiWorld("solve(0,1,2)",  
				slot00, new Vector<HanoiDisk>(), slot02);
		w.setParameter(new Object[] {0,1,2});
		myWorlds[0] = w;
		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);
		
		setup(myWorlds);
	}
}
