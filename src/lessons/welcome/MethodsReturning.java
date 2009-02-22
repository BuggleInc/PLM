package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;
import universe.bugglequest.exception.AlreadyHaveBaggleException;

public class MethodsReturning extends ExerciseTemplated {

	public MethodsReturning(Lesson lesson) {
		super(lesson);
		tabName = "Program";

		BuggleWorld[] myWorld = new BuggleWorld[2];
		for (int i=0; i<2;i++) {
			myWorld[i] = new BuggleWorld("World "+(i+1),7,7);
			new Buggle(myWorld[i], "Searcher", 0, 6, Direction.NORTH, Color.black, Color.lightGray);
		}

		try {
			myWorld[0].getCell(3, 2).newBaggle();
			myWorld[1].getCell(5, 1).newBaggle();
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}

		setup(myWorld);
	}
/*
		newSource("Program",
				(debug?"public boolean haveBaggle() {\n" +
						"  boolean res = false;\n"+
						"  for (int i=0; i<6; i++) {\n" +
						"     if (isOverBaggle()) { \n" +
						"        res = true;\n" +
						"     }\n" +
						"     forward();\n" +
						"   }\n" +
						"   for (int i=0;i<6;i++) {\n" +
						"     backward();\n" +
						"   }\n"+
						"   return res;\n" +
						"}":""),
						
						"$package "+
						"import java.awt.Color; " +
						"public class Program extends bugglequest.core.SimpleBuggle { "+
						"  public void forward(int i) { throw new RuntimeException(\"Pas le droit d'utiliser forward(int) dans cet exercice\");} "+
						"  public void backward(int i) { throw new RuntimeException(\"Pas le droit d'utiliser backard(int) dans cet exercice\");} "+
						" "+
						"  $body;"+
						" "+
						"  public void run() { " +
						"	 for (int i=0; i<7; i++) {"+
						"      if (haveBaggle()) " +
						"         return;" +
						"    turnRight();" +
						"    forward();" +
						"    turnLeft();" +
						"    }"+
						"  } "+
		"}");
	}



	@Override
	public void reset(){
		World[] myWorld = new World[2];
		
		

		myWorld[0] = new World("One Baggle",7,7);
		new Buggle(myWorld[0], "Sentinel", 0, 6, Direction.NORTH, Color.black, Color.lightGray);
		myWorld[1] = new World("Faraway Baggle",7,7);
		new Buggle(myWorld[1], "Lynx", 0, 6, Direction.NORTH, Color.black, Color.lightGray);

		try {
			myWorld[0].getCell(3, 2).newBaggle();
			myWorld[1].getCell(5, 1).newBaggle();
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}

		worldDuplicate(myWorld);

		try {
			for (int wit = 0 ; wit <2; wit++) {// World ITerator
				AbstractBuggle b = answerWorld[wit].buggles().next();
				for (int i=0; i<7; i++) {
					if (haveBaggle(b)) 
						break;
					b.turnRight();
					b.forward();
					b.turnLeft();
				}
			}
		} catch (BuggleException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		run("Program");
	}
	*/
}
