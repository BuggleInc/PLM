package lessons.recursion.dragoncurve1;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.turtles.Turtle;
import jlm.universe.turtles.TurtleWorld;

public class DragonCurve1 extends ExerciseTemplated {

	public DragonCurve1(Lesson lesson) {
		super(lesson);

		/* Create initial situation */
		World[] myWorlds = new World[7];
		myWorlds[0] = new TurtleWorld("dragon(1,100.,100.,200.,200.)", 300, 300);
		myWorlds[0].setParameter(new Object[] {1,100.,100.,200.,200.});		
		new Turtle(myWorlds[0], "Lee", 100, 100, 0, Color.red);

		myWorlds[1] = new TurtleWorld("dragon(2,100.,100.,200.,200.)", 300, 300);
		myWorlds[1].setParameter(new Object[] {2,100.,100.,200.,200.});		
		new Turtle(myWorlds[1], "Lee", 100, 100, 0, Color.red);

		myWorlds[2] = new TurtleWorld("dragon(3,100.,100.,200.,200.)", 300, 300);
		myWorlds[2].setParameter(new Object[] {3,100.,100.,200.,200.});		
		new Turtle(myWorlds[2], "Lee", 100, 100, 0, Color.red);

		myWorlds[3] = new TurtleWorld("dragon(4,100.,100.,200.,200.)", 300, 300);
		myWorlds[3].setParameter(new Object[] {4,100.,100.,200.,200.});		
		new Turtle(myWorlds[3], "Lee", 100, 100, 0, Color.red);

		myWorlds[4] = new TurtleWorld("dragon(5,100.,100.,200.,200.)", 300, 300);
		myWorlds[4].setParameter(new Object[] {5,100.,100.,200.,200.});		
		new Turtle(myWorlds[4], "Lee", 100, 100, 0, Color.red);
		
		myWorlds[5] = new TurtleWorld("dragon(10,100.,100.,200., 200.)", 300, 300);
		myWorlds[5].setParameter(new Object[] {10,100.,100.,200., 200.});		
		new Turtle(myWorlds[5], "Lee", 100, 100, 0, Color.red);

		myWorlds[6] = new TurtleWorld("dragon(15,100.,100.,200., 200.)", 300, 300);
		myWorlds[6].setParameter(new Object[] {15,100.,100.,200., 200.});		
		new Turtle(myWorlds[6], "Lee", 100, 100, 0, Color.red);

		setup(myWorlds);
	}	
	
}
