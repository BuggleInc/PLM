package plm.test.integration;

import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lesson;

public class ExoTestJavaLang extends ExoTest {

  @Execution(ExecutionMode.CONCURRENT)
  @ParameterizedTest
  @MethodSource("exercises")
  public void testJavaEntity(Lesson l, Exercise e) throws BrokenProgrammingLanguageException
  {
    initExerciseState(e);
    Assertions.assertTimeoutPreemptively(Duration.ofSeconds(20), () -> { testCorrectionEntity(e, Game.getInstance().programmingLanguageManager.JAVA); });
  }
}
