package lessons.welcome;

import java.awt.Color;
import java.util.Stack;

import jlm.universe.Direction;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.SimpleBuggle;



public class BuggleDancingChecker extends SimpleBuggle {

	public BuggleDancingChecker() {
		super();
	}

	public BuggleDancingChecker(BuggleWorld w, String name, int i, int j, Direction dir, Color c, Color bc) {
		super(w,name,i,j,dir, c, bc);
	}

	/* TODO: logic */
	Stack<Character> todoList = new Stack<Character>();
	public void addTODO(String s) {
		for (int i=s.length()-1; i>=0; i--) {
			todoList.push(s.charAt(i));
		}
	}

	boolean complained = false;
	private void complain(String msg) {
		if (!complained)
			System.out.println("XXX "+name+msg);
		complained = true;
	}

	String fmt(char c) {
		String func="";
		switch (c) {
		case 'R': func="right"; break;
		case 'L': func="left";  break;
		case 'I': func="back";  break;

		case 'A': func="plus1";  break;
		case 'B': func="plus2";  break;
		case 'C': func="plus3";  break;
		case 'D': func="plus4";  break;
		case 'E': func="plus5";  break;
		case 'F': func="plus6";  break;

		case 'Z': func="minus1";  break;
		case 'Y': func="minus2";  break;
		case 'X': func="minus3";  break;
		case 'W': func="minus4";  break;
		case 'V': func="minus5";  break;
		case 'U': func="minus6";  break;
		default: throw new RuntimeException("Unknown code: "+c);
		}
		return func+"("+getX()+","+getY()+")";
	}

	/* dance logic */
	
	boolean moreMusic = true;
	
	@Override
	public void run() {
		while (moreMusic) {
			if (isOverMessage()) 
				step(readMessage().charAt(0));
			else 
				endMusic();			
		}
	}
	void step(char read) {
		char todo = ' ';
		if (todoList.size() == 0) 
			complain(name+"I read "+fmt(read)+", but I'm supposed to be done.");
		else
			todo = todoList.pop();

		if (todo != read) {
			complain(name+"I read "+fmt(read)+", but I was supposed to do "+fmt(todo)+". Invalid TODO.");			
		}

		switch (read) {
		case 'R': turnRight();  forward(); break;
		case 'L': turnLeft();   forward(); break;
		case 'I': turnBack();   forward(); break;

		case 'A': forward();  break;
		case 'B': forward(2); break;
		case 'C': forward(3); break;
		case 'D': forward(4); break;
		case 'E': forward(5); break;
		case 'F': forward(6); break;

		case 'Z': backward();  break;
		case 'Y': backward(2); break;
		case 'X': backward(3); break;
		case 'W': backward(4); break;
		case 'V': backward(5); break;
		case 'U': backward(6); break;

		default: endMusic();
		}
	}

	public void endMusic() {
		moreMusic = false;
		if (todoList.size() != 0) 
			complain(name+"I'm done, but I was supposed to do "+fmt(todoList.pop())+";");
	}
}
