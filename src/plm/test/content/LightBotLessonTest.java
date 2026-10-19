package plm.test.content;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.tracking.GitTracker;

/** The LightBot boards have no correction to test: they are puzzles. Every board loads, and runs the empty program of a new student. */
public class LightBotLessonTest {

  @Test public void theEmptyProgramLeavesTheLightsOff() throws Exception
  {
    Game g = Game.getInstance();
    g.setGitTracker(Mockito.mock(GitTracker.class));
    g.removeSessionKit();
    g.setBatchExecution();
    Lesson lesson = g.switchLesson("lessons.lightbot", false);
    Assertions.assertNotEquals(0, lesson.getExerciseCount(), "Cannot find any LightBot board");

    for (Lecture lecture : lesson.exercises())
      if (lecture instanceof Exercise exo) {
        RunOutcome outcome = exo.compileRunCheck(null, StudentOrCorrection.STUDENT, g.programmingLanguageManager.LIGHTBOT, new ArrayList<>(), () -> {});
        Assertions.assertFalse(outcome.isPassed(), exo.getId() + " is solved by the empty program, please fix it");
        Assertions.assertTrue(outcome.totalTests > 0, exo.getId() + " has no light to turn on, please fix it");
      }
  }
}
