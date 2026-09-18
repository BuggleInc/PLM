package plm.core.model;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import javax.swing.SwingUtilities;
import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.RunOutcome;
import plm.core.ui.ExerciseFailedDialog;
import plm.core.ui.ExercisePassedDialog;

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
  private List<Future<?>> runners = new LinkedList<Future<?>>(); // entity-run tasks from this lesson, on the shared pool

  public LessonRunner(Game game)
  {
    super();
    this.game = game;
  }

  @Override public void run()
  {
    Lecture lect = this.game.getCurrentLesson().getCurrentExercise();
    if (!(lect instanceof Exercise))
      return;
    final Exercise exo = (Exercise)lect;
    final ProgrammingLanguage lang = this.game.getProgrammingLanguage();

    exo.lastResult = new RunOutcome();

    try {
      game.saveSession(); // for safety reasons;

      if (!game.isCreativeEnabled())
        exo.reset();

      game.setState(Game.GameState.COMPILATION_STARTED);
      String executable = exo.compile(this.game.getOutputWriter(), StudentOrCorrection.STUDENT, lang);
      game.setState(Game.GameState.COMPILATION_ENDED);

      game.setState(Game.GameState.EXECUTION_STARTED);

      exo.run(runners, lang, executable);

      if (!game.isCreativeEnabled())
        exo.check();
      game.setState(Game.GameState.EXECUTION_ENDED);

    } catch (InterruptedException e) {
      e.printStackTrace();
      game.setState(Game.GameState.EXECUTION_ENDED);
    } catch (PLMCompilerException e) {
      game.setState(Game.GameState.COMPILATION_ENDED);
      game.setState(Game.GameState.EXECUTION_ENDED);
    } catch (Exception e) {
      e.printStackTrace();
      game.setState(Game.GameState.COMPILATION_ENDED);
      game.setState(Game.GameState.EXECUTION_ENDED);
    }

    if (!game.isCreativeEnabled()) {
      try {
        if (exo.lastResult.outcome == RunOutcome.kind.PASS) {
          Game.getInstance().studentWork.setPassed(exo, exo.lastResult.language, true);

          SwingUtilities.invokeAndWait(new Runnable() {
            @Override public void run()
            {
              new ExercisePassedDialog(exo);
            }
          });
        } else {
          SwingUtilities.invokeAndWait(new Runnable() {
            public void run()
            {
              new ExerciseFailedDialog(exo.lastResult);
            }
          });
        }
      } catch (InvocationTargetException | InterruptedException e) {
        e.printStackTrace();
      }
      Game.getInstance().fireProgressSpy(exo);
    }
    runners.remove(this);
  }

  /**
   * Stop all the entity-run tasks that were already started.
   *
   * Thread.stop() was used here historically, but it is deprecated for removal:
   * on recent JDKs its presence in this framework source -- which PLM recompiles
   * in process when running a Java exercise -- makes that compilation fail, so
   * no Java exercise can run. Replace it with Future.cancel(true), which interrupts
   * the pool worker thread currently running the task -- a cooperative request to stop.
   */
  public void stopAll()
  {
    while (runners.size() > 0) {
      Future<?> f = runners.remove(runners.size() - 1);
      f.cancel(true);
    }
  }
}
