package lessons.welcome;

import java.awt.Color;

import jlm.universe.Direction;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.SimpleBuggle;



public class BuggleDancing extends SimpleBuggle {

	private boolean moreMusic = true;

	public BuggleDancing() {
		super();
	}

	public BuggleDancing(BuggleWorld w, String name, int i, int j, Direction dir, Color c, Color bc) {
		super(w, name, i, j,dir, c, bc);
	}

	public boolean moreMusic() {
		return moreMusic;
	}

	public void stopMusic() {
		moreMusic = false;
	}

	boolean step(char c) {
		switch (c) {
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

		default: return false;
		}
		return true;
	}

	
	public void danceOneStep() {
		if (isOverMessage()) {
			if (!step(readMessage().charAt(0))) 
				stopMusic();
		} else 
			stopMusic();

	}

	@Override
	public void run() {
	}
}
