package lessons.welcome.bdr;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.BuggleWorldCell;
import plm.universe.bugglequest.SimpleBuggle;

public class BDR2 extends ExerciseTemplated {

	BuggleWorld myWorld ;
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
	void plus4(int x, int y){  set(x, y, "D"); }
	void plus5(int x, int y){  set(x, y, "E"); }
	void plus6(int x, int y){  set(x, y, "F"); }

	void minus1 (int x, int y){  set(x, y, "Z"); }
	void minus2 (int x, int y){  set(x, y, "Y"); }
	void minus3 (int x, int y){  set(x, y, "X"); }
	void minus4 (int x, int y){  set(x, y, "W"); }
	void minus5 (int x, int y){  set(x, y, "V"); }
	void minus6 (int x, int y){  set(x, y, "U"); }

	void right(int x, int y){  set(x, y, "R"); }
	void left (int x, int y){  set(x, y, "L"); }
	void back (int x, int y){  set(x, y, "I"); }

	public BDR2(Game game, Lesson lesson) {
		super(game, lesson);
		tabName = "BDR2";
		setToolbox();
		// TODO: May we have to specify that run() method is provided ? and that 'I' is not backward()

		myWorld = new BuggleWorld(game, "Dance Floor",11,11);
		/* please applause the dancers*/
		new SimpleBuggle(myWorld, "John Travolta", 0, 0, Direction.EAST, Color.red, Color.red);
		new SimpleBuggle(myWorld, "Break Dancer", 10, 0, Direction.SOUTH, Color.magenta, Color.magenta);
		new SimpleBuggle(myWorld, "Moon Walker", 0, 10, Direction.NORTH, Color.pink, Color.pink);
		new SimpleBuggle(myWorld, "Elwood Blues", 10, 10, Direction.WEST, Color.blue, Color.blue);
		
		/* welcome to the dance floor, each dancer on a column */
		plus5(0,0);		plus5(10,0);	plus5(0,10);	plus5(10,10);
		right(5,0);		right(10,5);	right(0,5);		right(5,10);
		right(5,1);		right(9,5);		right(1,5);		right(5,9);
		left(4,1);		left(9,4);		left(1,6);		left(6,9);
		right(4,2);		right(8,4);		right(2,6);		right(6,8);
		left(3,2);		left(8,3);		left(2,7);		left(7,8);
		right(3,3);		right(7,3);		right(3,7);		right(7,7);
		plus2(2,3);		plus2(7,2);		plus2(3,8);		plus2(8,7);
		left(0,3);		left(7,0);		left(3,10);		left(10,7);
		left(0,4);		left(6,0);		left(4,10);		left(10,6);
		plus3(1,4);		plus3(6,1);		plus3(4,9);		plus3(9,6);
		right(4,4);		right(6,4);						right(6,6);
		left(4,5);		left(5,4);		left(5,6);		left(6,5);
		back(5,5);
		right(4,6);		right(4,3);		right(6,7);		right(7,4);
		right(3,6);
		minus4(3,5);	minus4(5,3);	minus4(5,7);	minus4(7,5);
		left(3,9);		left(1,3);		left(9,7);		left(7,1);
		minus6(2,9);	minus6(1,2);	minus6(9,8);	minus6(8,1);
		left(8,9);		left(1,8);		left(9,2);		left(2,1);
		minus5(8,10);	minus5(0,8);	minus5(10,2);	minus5(2,0);
		minus5(8,5);	minus5(5,8);	minus5(5,2);	minus5(2,5);
		left(8,0);		left(10,8);		left(0,2);		left(2,10);
		right(9,0);		right(10,9);	right(0,1);		right(1,10);

		myWorld.setParameter(new String[] {"ERRLRLRBLLCRLILRRWLULVVLR"});
		setup(myWorld);
		
	}
}
