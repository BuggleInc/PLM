package plm.core.model;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import javax.swing.SwingUtilities;
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

    RunOutcome outcome = runExercise(exo, lang);

    if (!game.isCreativeEnabled()) {
      try {
        if (outcome.isPassed()) {
          Game.getInstance().studentWork.setPassed(exo, outcome.language, true);
          SwingUtilities.invokeAndWait(() -> new ExercisePassedDialog(exo, outcome));
        } else {
          SwingUtilities.invokeAndWait(() -> new ExerciseFailedDialog(outcome));
        }
      } catch (InvocationTargetException | InterruptedException e) {
        if (e instanceof InterruptedException)
          Thread.currentThread().interrupt();
        e.printStackTrace();
      }
      Game.getInstance().getGitTracker().executed(exo, outcome);
    }
  }

  /**
   * Compiles, runs and checks the exercise, going through the states of the game. The outcome is a failure without any test if
   * something went wrong with the execution itself (for example if the student clicked "Stop").
   */
  public RunOutcome runExercise(Exercise exo, ProgrammingLanguage lang)
  {
    try {
      game.saveSession(); // for safety reasons;

      game.setState(Game.GameState.COMPILATION_STARTED);
      RunOutcome outcome = exo.compileRunCheck(game.getOutputWriter(), StudentOrCorrection.STUDENT, lang, runners, () -> {
        game.setState(Game.GameState.COMPILATION_ENDED);
        game.setState(Game.GameState.EXECUTION_STARTED);
      });
      if (outcome.isCompilationError())
        game.setState(Game.GameState.COMPILATION_ENDED); // the callback above was not called
      game.setState(Game.GameState.EXECUTION_ENDED);
      return outcome;

    } catch (InterruptedException | UserAbortException e) {
      if (e instanceof InterruptedException)
        Thread.currentThread().interrupt();
      e.printStackTrace();
      game.setState(Game.GameState.COMPILATION_ENDED);
      game.setState(Game.GameState.EXECUTION_ENDED);
    }
    RunOutcome notRun     = new RunOutcome(lang);
    notRun.executionError = Game.i18n.tr("Not run, probably because the user interrupted the execution.");
    return notRun;
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
