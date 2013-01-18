package lessons.welcome;

public class BDR2Entity extends jlm.universe.bugglequest.SimpleBuggle {
	public char getIndication() { 
		if (isOverMessage()) { 
			return readMessage().charAt(0); 
		} else { 
			return ' '; 
		} 
	}

	/* BEGIN SOLUTION */
	boolean moreMusic = true;

	public void danceOneStep() {
		switch (getIndication()) {
		case 'R': turnRight(); forward(); break;
		case 'L': turnLeft();  forward(); break;
		case 'I': turnBack();  forward(); break;

		case 'A': forward(1); break;
		case 'B': forward(2); break;
		case 'C': forward(3); break;
		case 'D': forward(4); break;
		case 'E': forward(5); break;
		case 'F': forward(6); break;

		case 'Z': backward(1); break;
		case 'Y': backward(2); break;
		case 'X': backward(3); break;
		case 'W': backward(4); break;
		case 'V': backward(5); break;
		case 'U': backward(6); break;

		default: moreMusic = false;
		}
	}
	/* END TEMPLATE */

	public void run() { 
		while (moreMusic)
			danceOneStep();
	}
}
