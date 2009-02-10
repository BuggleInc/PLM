package jlm.core;

import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import jlm.exception.JLMCompilerException;
import jlm.lesson.Exercise;
import jlm.ui.ResourcesCache;

public class LessonRunner extends Thread {

	private Game game;
	private List<Thread> runners = null; // threads who run entities from lesson

	public LessonRunner(Game game, List<Thread> list) {
		super();
		this.game = game;
		this.runners = list;
		this.runners.add(this);
	}

	@Override
	public void run() {
		try {
			game.saveSession(); // for safety reasons;
			
			Exercise exo = this.game.getCurrentLesson().getCurrentExercise();

			game.setState(GameState.COMPILATION_STARTED);
			exo.compileAll(this.game.getOutputWriter());
			game.setState(GameState.COMPILATION_ENDED);
			
			game.setState(GameState.EXECUTION_STARTED);
			exo.run(runners);

			Iterator<Thread> it = runners.iterator();
			while (it.hasNext()) {
				Thread t = it.next();
				if (!t.equals(this)) { /* do not wait for myself */
					t.join();
					it.remove();
				}
			}
			game.setState(GameState.EXECUTION_ENDED);

			if (!exo.check()) {
				JOptionPane.showMessageDialog(null, "Your world differs from the expected one.", "Test failed",
						JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Congratulations, you passed this test.", "Congratulations", JOptionPane.PLAIN_MESSAGE,
						ResourcesCache.getIcon("resources/success.png"));
				//this.game.getCurrentLesson().exercisePassed();
				exo.successfullyPassed();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
//			game.getOutputWriter().log(e);
			game.setState(GameState.EXECUTION_ENDED);
		} catch (JLMCompilerException e) {
			game.setState(GameState.COMPILATION_ENDED);
			game.setState(GameState.EXECUTION_ENDED);
		} catch (Exception e) {
			e.printStackTrace();
//			game.getOutputWriter().log(e);
			game.setState(GameState.COMPILATION_ENDED);
			game.setState(GameState.EXECUTION_ENDED);
		}
		
		runners.remove(this);
	}

}
