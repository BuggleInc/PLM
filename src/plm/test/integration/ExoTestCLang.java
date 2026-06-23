package plm.test.integration;

import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lesson;

public class ExoTestCLang extends ExoTest {

  //        @ParameterizedTest
  //        @MethodSource("exercises")
  public void testCEntityExists(Lesson l, Exercise e) throws BrokenProgrammingLanguageException
  {
    initExerciseState(l, e);
    if (!e.getProgLanguages().contains(Game.getInstance().programmingLanguageManager.C))
      Assertions.fail("Exercise " + e.getId() + " does not support C");
    testCorrectionEntityExists(e, Game.getInstance().programmingLanguageManager.C);
  }
  //        @ParameterizedTest
  //        @MethodSource("exercises")
  public void testCEntity(Lesson l, Exercise e) throws BrokenProgrammingLanguageException
  {
    initExerciseState(l, e);
    if (!e.getProgLanguages().contains(Game.getInstance().programmingLanguageManager.C))
      Assertions.fail("Exercise " + e.getId() + " does not support C");
    Assertions.assertTimeoutPreemptively(
        Duration.ofSeconds(5), () -> { testCorrectionEntity(e, Game.getInstance().programmingLanguageManager.C); });
  }
}
