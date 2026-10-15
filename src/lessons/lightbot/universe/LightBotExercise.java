package lessons.lightbot.universe;

import java.util.List;
import java.util.concurrent.Future;
import javax.swing.JScrollPane;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.session.SourceFile;
import plm.universe.World;

public class LightBotExercise extends ExerciseTemplated {
  public LightBotExercise(Lesson lesson)
  {
    super(lesson);
    addProgLanguage(Game.getInstance().programmingLanguageManager.LIGHTBOT);
    if (getProgLanguages().size() > 1)
      throw new RuntimeException("More than one language defined in a LightbotExercise. Please report this bug.");
    newSource(Game.getInstance().programmingLanguageManager.LIGHTBOT, new SourceFile());
  }

  @Override public JScrollPane getEditorPanel(ProgrammingLanguage lang) { return new LightBotEditorPanel(this, lang); }

  @Override protected void setup(World[] ws)
  {
    for (World w : ws)
      ((LightBotWorld)w).rotateLeft();

    setupWorlds(ws);
    /* remove entities from the answer world: we don't care of where the bot is at the end */
    for (World w : answerWorld)
      w.emptyEntities();
    computeAnswer();
  }

  @Override protected void computeAnswer()
  {
    for (int w = 0; w < answerWorld.size(); w++) {
      LightBotWorld.CellIterator ci = ((LightBotWorld)answerWorld.get(w)).new CellIterator();
      while (ci.hasNext()) {
        ci.next().setLightOn();
      }
    }
  }
  @Override public void check(RunOutcome result)
  {
    for (int w = 0; w < currentWorld.size(); w++) {
      LightBotWorld.CellIterator ci = ((LightBotWorld)currentWorld.get(w)).new CellIterator();
      while (ci.hasNext()) {
        LightBotWorldCell cell = ci.next();
        if (cell.isLight()) {
          result.totalTests++;
          if (cell.isLightOn())
            result.passedTests++;
        }
      }
      int stillOff = result.totalTests - result.passedTests;
      if (stillOff == 1)
        result.executionError = Game.i18n.tr("A light is still off.");
      if (stillOff > 1)
        result.executionError = Game.i18n.tr("{0} lights (out of {1}) are still off.", stillOff, result.totalTests);

      if (stillOff == 0)
        result.outcome = RunOutcome.kind.PASS;
      else
        result.outcome = RunOutcome.kind.FAIL;
    }
  }
  @Override public RunOutcome run(List<Future<?>> runnerVect, ProgrammingLanguage lang, String executable) throws InterruptedException
  {
    reset();
    return runAll(WorldKind.CURRENT, runnerVect, lang, executable);
  }

  @Override
  public void runDemo(List<Future<?>> runnerVect, ProgrammingLanguage lang)
  { /* No demo for lightbot: this is a puzzle game, you have to search for the answer by yourself */ }
}
