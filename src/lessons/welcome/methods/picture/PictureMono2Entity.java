package lessons.welcome.methods.picture;


public class PictureMono2Entity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	void mark() {
		brushDown();
		brushUp();
	}

	void makeV() {
		forward();
		mark();

		forward();
		left();
		forward();
		mark();

		backward();
		right();
		forward();
		mark();

		forward();
		left();
	}

	void makePattern() {
		makeV();
		makeV();
		makeV();
		makeV();
		forward(5);
	}

	void makeLine(int count){
		for (int i=0; i<count;i++)
			makePattern();
		backward(count*5);
	}

	void nextLine() {
		left();
		forward(5);
		right();	
	}

	public void run() {
		for (int i=0; i<3;i++) {
			makeLine(3);
			nextLine();
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
