package lessons.welcome.bdr;

public class BDREntity extends jlm.universe.bugglequest.SimpleBuggle {
	public char getIndication() { 
		if (isOverMessage()) { 
			return readMessage().charAt(0); 
		} else { 
			return ' '; 
		} 
	}

	public void run() {
		/* BEGIN SOLUTION */
		while (true) {
			char c = getIndication();

			if (c == 'R') { 
				right(); forward();
			} else if (c == 'L') {
				left(); forward();
			} else if (c == 'I') {
				back(); forward(); 
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
}
