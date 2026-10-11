package plm.test.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lesson;

public class ExoTestCLang extends ExoTest {

  @Execution(ExecutionMode.CONCURRENT)
  @ParameterizedTest
  @MethodSource("exercises")
  public void testCEntity(Lesson l, Exercise e) throws BrokenProgrammingLanguageException
  {
    initExerciseState(e);
    if (!e.getProgLanguages().contains(Game.getInstance().programmingLanguageManager.C))
      Assertions.fail("Exercise " + e.getId() + " has no C entity");
    Assertions.assertTimeoutPreemptively(timeout(e), () -> { testCorrectionEntity(e, Game.getInstance().programmingLanguageManager.C); });
  }
}
