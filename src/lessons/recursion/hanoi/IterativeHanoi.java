package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.recursion.hanoi.universe.HanoiWorld;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class IterativeHanoi extends ExerciseTemplated {

	public IterativeHanoi(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		myWorlds[0] = new HanoiWorld(game, "hanoi(0,true)",  new Integer[] {5,4,3,2,1}, new Integer[0],new Integer[0]);
		myWorlds[0].setParameter(new Object[] {0,true});		
		myWorlds[1] = new HanoiWorld(game, "hanoi(1,true)",  new Integer[0], new Integer[] {6,5,4,3,2,1}, new Integer[0]);
		myWorlds[1].setParameter(new Object[] {1,true});		
		myWorlds[2] = new HanoiWorld(game, "hanoi(1,false)", new Integer[0], new Integer[] {6,5,4,3,2,1}, new Integer[0]);
		myWorlds[2].setParameter(new Object[] {1,false});		
		for (int i=0;i<myWorlds.length;i++) 
			new HanoiEntity("worker",myWorlds[i]);
		
		setup(myWorlds);
	}
}
