package lessons.recursion.logo.dragoncurve;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class DragonCurve1 extends ExerciseTemplated {

	public DragonCurve1(Game game, Lesson lesson) {
		super(game, lesson);

		/* Create initial situation */
		World[] myWorlds = new World[7];
		myWorlds[0] = new TurtleWorld(game, "dragon(1,100.,100.,200.,200.)", 300, 300);
		myWorlds[0].setParameter(new Object[] {1,100.,100.,200.,200.});		
		new Turtle(myWorlds[0], "Lee", 100, 100, 0, Color.red);

		myWorlds[1] = new TurtleWorld(game, "dragon(2,100.,100.,200.,200.)", 300, 300);
		myWorlds[1].setParameter(new Object[] {2,100.,100.,200.,200.});		
		new Turtle(myWorlds[1], "Lee", 100, 100, 0, Color.red);

		myWorlds[2] = new TurtleWorld(game, "dragon(3,100.,100.,200.,200.)", 300, 300);
		myWorlds[2].setParameter(new Object[] {3,100.,100.,200.,200.});		
		new Turtle(myWorlds[2], "Lee", 100, 100, 0, Color.red);

		myWorlds[3] = new TurtleWorld(game, "dragon(4,100.,100.,200.,200.)", 300, 300);
		myWorlds[3].setParameter(new Object[] {4,100.,100.,200.,200.});		
		new Turtle(myWorlds[3], "Lee", 100, 100, 0, Color.red);

		myWorlds[4] = new TurtleWorld(game, "dragon(5,100.,100.,200.,200.)", 300, 300);
		myWorlds[4].setParameter(new Object[] {5,100.,100.,200.,200.});		
		new Turtle(myWorlds[4], "Lee", 100, 100, 0, Color.red);
		
		myWorlds[5] = new TurtleWorld(game, "dragon(10,100.,100.,200., 200.)", 300, 300);
		myWorlds[5].setParameter(new Object[] {10,100.,100.,200., 200.});		
		new Turtle(myWorlds[5], "Lee", 100, 100, 0, Color.red);

		myWorlds[6] = new TurtleWorld(game, "dragon(15,100.,100.,200., 200.)", 300, 300);
		myWorlds[6].setParameter(new Object[] {15,100.,100.,200., 200.});		
		new Turtle(myWorlds[6], "Lee", 100, 100, 0, Color.red);

		setup(myWorlds);
	}	
	
}
