package plm.test.integration;

import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lesson;

public class ExoTestScalaLang extends ExoTest {

  @ParameterizedTest
  @MethodSource("exercises")
  public void testScalaEntityExists(Lesson l, Exercise e) throws BrokenProgrammingLanguageException
  {
    initExerciseState(l, e);
    if (!e.getProgLanguages().contains(Game.getInstance().programmingLanguageManager.SCALA))
      Assertions.fail("Exercise " + e.getId() + " does not support scala");
    testCorrectionEntityExists(e, Game.getInstance().programmingLanguageManager.SCALA);
  }

  @ParameterizedTest
  @MethodSource("exercises")
  public void testScalaEntity(Lesson l, Exercise e) throws BrokenProgrammingLanguageException
  {
    initExerciseState(l, e);
    if (!e.getProgLanguages().contains(Game.getInstance().programmingLanguageManager.SCALA))
      Assertions.fail("Exercise " + e.getId() + " does not support scala");
    Assertions.assertTimeoutPreemptively(
        Duration.ofSeconds(5), () -> { testCorrectionEntity(e, Game.getInstance().programmingLanguageManager.SCALA); });
  }
}
