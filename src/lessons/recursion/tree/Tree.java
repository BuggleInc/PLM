package lessons.recursion.tree;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.turtles.Turtle;
import jlm.universe.turtles.TurtleWorld;

public class Tree extends ExerciseTemplated {

	public Tree(Lesson lesson) {
		super(lesson);

		/* Create initial situation */
		World[] myWorlds = new World[4];
		myWorlds[0] = new TurtleWorld("tree(7,75,30,0.8)", 400, 400);
		myWorlds[0].setParameter(new Object[] {7,75.,30.,.8});		
		new Turtle(myWorlds[0], "Hawksbill", 200, 350, -90, Color.red);
		
		myWorlds[1] = new TurtleWorld("tree(7,75,15,0.8)", 400, 400);
		myWorlds[1].setParameter(new Object[] {7,75.,15.,.8});		
		new Turtle(myWorlds[1], "Hawksbill", 200, 350, -90, Color.red);

		myWorlds[2] = new TurtleWorld("tree(10,80,45,0.7)", 400, 400);
		myWorlds[2].setParameter(new Object[] {10,80.,45.,.7});		
		new Turtle(myWorlds[2], "Hawksbill", 200, 350, -90, Color.red);

		myWorlds[3] = new TurtleWorld("tree(10,100,90,0.68)", 400, 400);
		myWorlds[3].setParameter(new Object[] {10,100.,90.,.68});		
		new Turtle(myWorlds[3], "Hawksbill", 200, 350,-90, Color.red);
		setup(myWorlds);
	}
}
