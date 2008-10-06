package lessons.welcome;

public class BDRBuggle extends jlm.bugglequest.SimpleBuggle {
	public char getIndication() { 
		if (isOverMessage()) { 
			return readMessage().charAt(0); 
		} else { 
			return ' '; 
		} 
	}

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		while (true) {
			char c = getIndication();

			 if (c == 'R') { 
			    turnRight(); forward();
			 } else if (c == 'L') {
			    turnLeft(); forward();
			 } else if (c == 'I') {
			    turnBack(); forward(); 
			 } else if (c == 'A')
			    forward();
			 else if (c == 'B')
			    forward(2);  
			 else if (c == 'C')
			    forward(3);
			 else if (c == 'Z')
			    backward();
			 else if (c == 'Y')
			    backward(2);  
			 else if (c == 'X')
			    backward(3);
			 else 
			    return ;
		}		
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	
	
	/* Old solution: removed since student do not know switch yet.
	 while (true) 
		switch (getIndication()) {
			case 'R': turnRight(); forward(); break;
			case 'L': turnLeft();  forward(); break;
			case 'I': turnBack();  forward(); break;

			case 'A': forward(1); break;
			case 'B': forward(2); break;
			case 'C': forward(3); break;

			case 'Z': backward(1); break;
			case 'Y': backward(2); break;
			case 'X': backward(3); break;

			default: return;
		}
	 */

}
