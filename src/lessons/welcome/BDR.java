package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.BuggleWorldCell;

public class BDR extends ExerciseTemplated {

	BuggleWorld myWorld;
	void set(int x, int y, String clue) {
		BuggleWorldCell c = (BuggleWorldCell) myWorld.getCell(x, y);
		if (!c.hasContent())
			c.addContent(clue);
		
		if (!c.getContent().equals(clue))
			throw new RuntimeException("Cell ("+x+","+y+") already contains "+c.getContent()+". Cannot put "+clue);
	}
	void plus1(int x, int y){  set(x, y, "A"); }
	void plus2(int x, int y){  set(x, y, "B"); }
	void plus3(int x, int y){  set(x, y, "C"); }

	void minus1 (int x, int y){  set(x, y, "Z"); }
	void minus2 (int x, int y){  set(x, y, "Y"); }
	void minus3 (int x, int y){  set(x, y, "X"); }

	void right(int x, int y){  set(x, y, "R"); }
	void left (int x, int y){  set(x, y, "L"); }
	void back (int x, int y){  set(x, y, "I"); }

	public BDR(Lesson lesson) {
		super(lesson);
		tabName = "BDRBuggle";			

		/*FIXME: well if I'm right some statements in mission are wrong
		 * "tutu" == "tutu" is true in Java...
		 * Because String are non-mutable objects.
		 * 
		 * the proposed template (while (true) ... return) is not 
		 * very pedagogical...
		 */
		
		myWorld = new BuggleWorld("Dance Floor",7,7);
		/* first dancer, plus its steps */
		new BuggleDancing(myWorld, "John Travolta", 0, 6, Direction.EAST, Color.red, Color.lightGray);
		plus1(0,6);
		left(1,6);
		plus2(1,5);
		right(1,3);
		
		/* second dancer */
		new BuggleDancing(myWorld, "Break Dancer", 0,0,Direction.WEST, Color.magenta, Color.lightGray);
		minus1(0,0);
		left(1,0);
		plus2(1,1);
		minus3(0,3);
		minus1(3,3);
		
		/* third one */
		new BuggleDancing(myWorld, "Moon Walker", 6,0, Direction.WEST, Color.pink,Color.lightGray);
		plus1(6,0);
		left(5,0);
		plus2(5,1);
		back(5,3);
		plus2(5,2);
		plus1(4,0);
		left(3,0);
		plus2(3,1);
		
		/* last one */
		new BuggleDancing(myWorld, "Elwood Blues", 6,6, Direction.WEST, Color.blue,Color.lightGray);
		plus1(6,6);
		right(5,6);
		plus2(5,5);
		plus2(5,4);
		plus2(4,6);
		minus1(2, 6);
		right(3,6);
		plus2(3,5);
		
		setup(myWorld);
	}
	/*
		worldDuplicate(myWorld);
		mutateBuggle(answerWorld, "lessons.welcome.BuggleDancing");
				
		Iterator<AbstractBuggle> it;
		for (it = answerWorld[0].buggles(); it.hasNext();) {
			BuggleDancing b = (BuggleDancing)it.next();
			while (b.moreMusic())
				b.danceOneStep();
		}
	}

	@Override
	public void run(){
		super.run("Program");
	}
	*/
}
