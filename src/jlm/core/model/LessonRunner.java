package jlm.core.model;

import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.JLMCompilerException;
import jlm.core.model.lesson.Lecture;
import jlm.core.ui.ResourcesCache;

/** 
 * This class runs the student code of the current exercise in a separated thread 
 * when the Run button is clicked. The run and demo buttons are disabled until the demo ends.
 * 
 * It sends a twitter update when the exercise is successfully passed.
 * 
 * Activated by {@link Game#startExerciseExecution()} and {@link Game#startExerciseStepExecution()}.
 */
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
		Lecture lect = this.game.getCurrentLesson().getCurrentExercise();
		if (! (lect instanceof Exercise))
			return;
		Exercise exo = (Exercise) lect;
		
		try {
			game.saveSession(); // for safety reasons;
			
			game.setState(GameState.COMPILATION_STARTED);
			exo.compileAll(this.game.getOutputWriter());
			game.setState(GameState.COMPILATION_ENDED);
			
			game.setState(GameState.EXECUTION_STARTED);
			exo.reset();
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
			Game.getInstance().fireProgressSpy(exo);									
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
