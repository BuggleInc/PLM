package jlm.core.model;

import java.util.Iterator;
import java.util.List;

import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;

/** 
 * This class runs the demo of the current exercise in a separated thread 
 * when the Demo button is clicked. The run and demo buttons are disabled until the demo ends.
 * 
 * Activated by {@link Game#startExerciseDemoExecution()}.
 */
public class DemoRunner extends Thread {

	private Game game;
	private List<Thread> runners = null; // threads who run entities from lesson

	public DemoRunner(Game game, List<Thread> list) {
		super();
		this.game = game;
		this.runners = list;
		this.runners.add(this);
	}

	@Override
	public void run() {
		Lecture lect = this.game.getCurrentLesson().getCurrentExercise();
		if (! (lect instanceof Exercise))
			return;
		Exercise exo = (Exercise) lect;
		
		boolean stepModeWasActivated = this.game.stepModeEnabled();

		try {
			game.setState(GameState.DEMO_STARTED);
			
			this.game.disableStepMode();
			
			exo.runDemo(runners);

			Iterator<Thread> it = runners.iterator();
			while (it.hasNext()) {
				Thread t = it.next();
				if (!t.equals(this)) { /* do not wait for myself */
					t.join();
					it.remove();
				}
			}
		} catch (InterruptedException e) {
			game.getOutputWriter().log(e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stepModeWasActivated) {
				this.game.enableStepMode();
			}
			game.setState(GameState.DEMO_ENDED);			
		}

		runners.remove(this);
	}

}
