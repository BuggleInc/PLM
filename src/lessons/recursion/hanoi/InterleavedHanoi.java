package lessons.recursion.hanoi;

import java.awt.Color;

import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.recursion.hanoi.universe.HanoiWorld;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class InterleavedHanoi extends ExerciseTemplated {

	public InterleavedHanoi(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		HanoiWorld w;
		w = new HanoiWorld(game, "solve(0,1,2,3)",  
				new Integer[] {7,6,5,4,3,2,1}, new Integer[] {7,6,5,4,3,2,1}, new Integer[0],new Integer[0]);
		for (int i=0; i<w.getSlotSize(0);i++) 
			w.setColor(0,i,Color.white);
		for (int i=0; i<w.getSlotSize(1);i++) 
			w.setColor(1,i,Color.black);
		w.setParameter(new Integer[]{0,1,2,3});
		myWorlds[0] = w;
		
		w = new HanoiWorld(game, "slove(0,2,3,1)",  
				new Integer[] {6,5,4,3,2,1}, new Integer[0],new Integer[] {6,5,4,3,2,1},new Integer[0]);
		for (int i=0; i<w.getSlotSize(0);i++) 
			w.setColor(0,i,Color.black);
		for (int i=0; i<w.getSlotSize(2);i++) 
			w.setColor(2,i,Color.white);
		w.setParameter(new Integer[]{0,2,3,1});
		myWorlds[1] = w;
		
		w = new HanoiWorld(game, "solve(0,3,1,2)",  
				new Integer[] {5,4,3,2,1}, new Integer[0],new Integer[0], new Integer[] {5,4,3,2,1});
		for (int i=0; i<w.getSlotSize(0);i++) 
			w.setColor(0,i,Color.white);
		for (int i=0; i<w.getSlotSize(3);i++) 
			w.setColor(3,i,Color.black);
		w.setParameter(new Integer[]{0,3,1,2});
		myWorlds[2] = w;

		for (int i=0;i<myWorlds.length;i++) {
			new HanoiEntity("worker",myWorlds[i]);
		}
		
		setup(myWorlds);
	}
}
