package jlm.core;

import java.util.Iterator;
import java.util.Vector;


import lessons.Exercise;

public class DemoRunner extends Thread {

	private Game game;
	private Vector<Thread> runners = null; // threads who run entities from lesson

	public DemoRunner(Game game, Vector<Thread> list) {
		super();
		this.game = game;
		this.runners = list;
		this.runners.add(this);
	}

	@Override
	public void run() {
		try {
			Exercise exo = this.game.getCurrentLesson().getCurrentExercise();

			game.setState(GameState.DEMO_STARTED);
			exo.runDemo(runners);

			Iterator<Thread> it = runners.iterator();
			while (it.hasNext()) {
				Thread t = it.next();
				if (!t.equals(this)) { /* do not wait for myself */
					t.join();
					it.remove();
				}
			}
			game.setState(GameState.DEMO_ENDED);
		} catch (InterruptedException e) {
			game.getOutputWriter().log(e);
			game.setState(GameState.DEMO_ENDED);
		}

		runners.remove(this);
	}

}
