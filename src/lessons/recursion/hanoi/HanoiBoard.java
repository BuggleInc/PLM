package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.recursion.hanoi.universe.HanoiWorld;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class HanoiBoard extends ExerciseTemplated {

	public HanoiBoard(Game game, Lesson lesson) {
		super(game, lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		myWorlds[0] = new HanoiWorld(game, "solve(0,1,2)",  
				new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0],new Integer[0]);
		myWorlds[0].setParameter(new Object[] {0,1,2});		
		myWorlds[1] = new HanoiWorld(game, "solve(2,0,1)", 
				new Integer[0] , new Integer[0],new Integer[] {8,7,6,5,4,3,2,1});
		myWorlds[1].setParameter(new Object[] {2,0,1});		
		myWorlds[2] = new HanoiWorld(game, "solve(1,2,0)", 
				new Integer[0], new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0]);
		myWorlds[2].setParameter(new Object[] {1,2,0});		
		for (int i=0;i<myWorlds.length;i++) {
			new HanoiEntity("worker",myWorlds[i]);
		}
		
		setup(myWorlds);
	}
}
