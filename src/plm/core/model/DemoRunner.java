package plm.core.model;

import java.util.List;
import java.util.concurrent.Future;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;

/**
 * This class runs the demo of the current exercise in a separated thread
 * when the Demo button is clicked. The run and demo buttons are disabled until the demo ends.
 *
 * Activated by {@link Game#startExerciseDemoExecution()}.
 */
public class DemoRunner extends Thread {

  private Game game;
  private List<Future<?>> runners = null; // entity-run tasks from this lesson, on the shared pool

  public DemoRunner(Game game, List<Future<?>> list)
  {
    super();
    this.game    = game;
    this.runners = list;
  }

  public void runDemo(Exercise exo, ProgrammingLanguage lang) throws Exception
  {
    game.setState(Game.GameState.DEMO_STARTED);

    this.game.disableStepMode();

    exo.runDemo(runners, lang);
  }

  @Override public void run()
  {
    Lecture lect = this.game.getCurrentLesson().getCurrentExercise();
    if (!(lect instanceof Exercise))
      return;
    Exercise exo = (Exercise)lect;

    boolean stepModeWasActivated = this.game.stepModeEnabled();

    try {
      runDemo(exo, this.game.getProgrammingLanguage());
    } catch (InterruptedException e) {
      game.getOutputWriter().log(e);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (stepModeWasActivated) {
        this.game.enableStepMode();
      }
      game.setState(Game.GameState.DEMO_ENDED);
    }
  }
}
