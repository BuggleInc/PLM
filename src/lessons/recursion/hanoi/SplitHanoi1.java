package lessons.recursion.hanoi;

import java.awt.Color;

import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.recursion.hanoi.universe.HanoiWorld;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class SplitHanoi1 extends ExerciseTemplated {

	public SplitHanoi1(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		HanoiWorld w;
		w = new HanoiWorld(game, "solve(3,2,0,1)",  
				new Integer[0], new Integer[0],new Integer[0], new Integer[] {7,7,6,6,5,5,4,4,3,3,2,2,1,1});
		for (int i=0; i<w.getSlotSize(3);i++)
			if (i%2==0)
				w.setColor(3,i,Color.white);
			else
				w.setColor(3,i,Color.black);
		w.setParameter(new Integer[]{3,2,0,1});
		myWorlds[0] = w;
		
		w = new HanoiWorld(game, "slove(1,3,0,2)",  
				new Integer[0], new Integer[]{6,6,5,5,4,4,3,3,2,2,1,1},new Integer[0],new Integer[0]);
		for (int i=0; i<w.getSlotSize(1);i++)
			if (i%2==0)
				w.setColor(1,i,Color.white);
			else
				w.setColor(1,i,Color.black);
		w.setParameter(new Integer[]{1,3,0,2});
		myWorlds[1] = w;
		
		w = new HanoiWorld(game, "solve(2,1,0,3)",  
				new Integer[0], new Integer[0],new Integer[]{5,5,4,4,3,3,2,2,1,1}, new Integer[0]);
		for (int i=0; i<w.getSlotSize(2);i++)
			if (i%2==0)
				w.setColor(2,i,Color.white);
			else
				w.setColor(2,i,Color.black);
		w.setParameter(new Integer[]{2,1,0,3});
		myWorlds[2] = w;
		
		for (int i=0;i<myWorlds.length;i++) {
			new HanoiEntity("worker",myWorlds[i]);
		}
		
		setup(myWorlds);
	}
}
