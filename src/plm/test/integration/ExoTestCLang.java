package plm.test.integration;

import java.time.Duration;
import java.util.Set;
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
    // Turmite exercises run tens of thousands of remote-primitive round trips, so we need to increase the timeout value.
    Duration timeout = Set.of("turmites.TurmiteCreator", "turmites.LangtonColors").contains(e.getId()) ? Duration.ofSeconds(60) : Duration.ofSeconds(20);
    Assertions.assertTimeoutPreemptively(timeout, () -> { testCorrectionEntity(e, Game.getInstance().programmingLanguageManager.C); });
  }
}
