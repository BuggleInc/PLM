package lessons.recursion.hanoi;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.recursion.hanoi.universe.HanoiWorld;

public class TricolorHanoi3 extends ExerciseTemplated {

	public TricolorHanoi3(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld w;
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		w = new HanoiWorld(game, "tricolor(0,1,2)", new Integer[] {4,3,2,1}, new Integer[] {4,3,2,1},new Integer[] {4,3,2,1});
		w.setParameter(new Object[] {0,1,2});		
		for (int i=0; i<w.getSlotSize(0);i++) {
			w.setColor(0, i, Color.white);
			w.setColor(1, i, Color.yellow);
			w.setColor(2, i, Color.black);
		}
		myWorlds[0] = w;
		
		w = new HanoiWorld(game, "tricolor(1,2,0)", new Integer[] {4,3,2,1}, new Integer[] {4,3,2,1},new Integer[] {4,3,2,1});
		w.setParameter(new Object[] {1,2,0});		
		for (int i=0; i<w.getSlotSize(0);i++) {
			w.setColor(0, i, Color.white);
			w.setColor(1, i, Color.yellow);
			w.setColor(2, i, Color.black);
		}
		myWorlds[1] = w;

		w = new HanoiWorld(game, "tricolor(2,0,1)", new Integer[] {4,3,2,1}, new Integer[] {4,3,2,1},new Integer[] {4,3,2,1});
		w.setParameter(new Object[] {2,0,1});		
		for (int i=0; i<w.getSlotSize(0);i++) {
			w.setColor(0, i, Color.white);
			w.setColor(1, i, Color.yellow);
			w.setColor(2, i, Color.black);
		}
		myWorlds[2] = w;

		
		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);
		
		setup(myWorlds);
	}
}
