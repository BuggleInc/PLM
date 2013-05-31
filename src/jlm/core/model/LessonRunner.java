package jlm.core.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import jlm.core.JLMCompilerException;
import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.ui.ExerciseFailedDialog;
import jlm.core.ui.ResourcesCache;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/** 
 * This class runs the student code of the current exercise in a separated thread 
 * when the Run button is clicked. The run and demo buttons are disabled until the demo ends.
 * 
 * It sends an update to the remote GoogleAppEngine when the exercise is successfully passed.
 * 
 * Activated by {@link Game#startExerciseExecution()} and {@link Game#startExerciseStepExecution()}.
 */
public class LessonRunner extends Thread {
	
	private Game game;
	private List<Thread> runners = new LinkedList<Thread>(); // threads who run entities from lesson
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages", FileUtils.getLocale(), I18nFactory.FALLBACK);

	public LessonRunner(Game game) {
		super();
		this.game = game;
	}

	@Override
	public void run() {
		Lecture lect = this.game.getCurrentLesson().getCurrentExercise();
		if (! (lect instanceof Exercise))
			return;
		final Exercise exo = (Exercise) lect;
		
		exo.lastResult = new ExecutionProgress();
		
		try {
			game.saveSession(); // for safety reasons;
			
			game.setState(Game.GameState.COMPILATION_STARTED);
			exo.compileAll(this.game.getOutputWriter());
			game.setState(Game.GameState.COMPILATION_ENDED);
			
			game.setState(Game.GameState.EXECUTION_STARTED);
			exo.reset();
			
			exo.run(runners);
			while (runners.size()>0) {
				Thread t = runners.remove(0);
				t.join();
			}
			
			exo.check();
			game.setState(Game.GameState.EXECUTION_ENDED);

		} catch (InterruptedException e) {
			e.printStackTrace();
//			game.getOutputWriter().log(e);
			game.setState(Game.GameState.EXECUTION_ENDED);
		} catch (JLMCompilerException e) {
			game.setState(Game.GameState.COMPILATION_ENDED);
			game.setState(Game.GameState.EXECUTION_ENDED);
		} catch (Exception e) {
			e.printStackTrace();
//			game.getOutputWriter().log(e);
			game.setState(Game.GameState.COMPILATION_ENDED);
			game.setState(Game.GameState.EXECUTION_ENDED);
		}
		
		if (   exo.lastResult.totalTests > 0 
			&& exo.lastResult.totalTests == exo.lastResult.passedTests) {
			Game.getInstance().studentWork.setPassed(exo.getId(), null, true);
			
			Vector<Lecture> nextExercises =  exo.getDependingLectures();	
			if ( nextExercises.size() == 0) {
				JOptionPane.showMessageDialog(null, 
						i18n.tr("Congratulations, you passed this exercise.\n {0} tests passed.\n",
								exo.lastResult.passedTests) + exo.lastResult.details, 
						i18n.tr("Exercice passed \\o/"), 
						JOptionPane.PLAIN_MESSAGE, ResourcesCache.getIcon("resources/success.png"));
			} else {
				
				Lecture selectedValue = (Lecture) JOptionPane.showInputDialog(null, 
						i18n.tr("Congratulations, you passed this test. Which exercise will you do now?\n"), 
						i18n.tr("Exercice passed \\o/"),
						JOptionPane.PLAIN_MESSAGE, ResourcesCache.getIcon("resources/success.png"),
						nextExercises.toArray(), nextExercises.get(0));
				if (selectedValue != null) 
					Game.getInstance().setCurrentExercise(selectedValue);
			}
		} else {
			 SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	new ExerciseFailedDialog(exo.lastResult);
		            }
		        });

		}
		Game.getInstance().fireProgressSpy(exo);									

		runners.remove(this);
	}
	
	/** Stop all the threads that were already started. Harmful but who cares? */
	@SuppressWarnings("deprecation")
	public void stopAll() {
		while (runners.size()>0) {
			Thread t = runners.remove(runners.size() - 1);
			t.stop(); // harmful but who cares ?
		}
	}
	
}
