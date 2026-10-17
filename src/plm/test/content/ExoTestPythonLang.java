package plm.test.content;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lesson;

public class ExoTestPythonLang extends ExoTest {

  @Execution(ExecutionMode.CONCURRENT)
  @ParameterizedTest
  @MethodSource("exercises")
  public void testPythonEntity(Lesson l, Exercise e) throws BrokenProgrammingLanguageException
  {
    initExerciseState(e);
    if (!e.getProgLanguages().contains(Game.getInstance().programmingLanguageManager.PYTHON))
      Assertions.fail("Exercise " + e.getId() + " has no Python entity");
    Assertions.assertTimeoutPreemptively(
        timeout(e), () -> { testCorrectionEntity(e, Game.getInstance().programmingLanguageManager.PYTHON); }, "Test " + e.getId() + " timeouted.");
  }
}
