package plm.test.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import plm.core.GameStateListener;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.Game.GameState;
import plm.core.model.LessonRunner;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.RunOutcome;
import plm.core.utils.FileUtils;

/** What LessonRunner reports and the states that it goes through, whatever happens to the execution of the exercise. */
public class LessonRunnerTest {
  private final List<GameState> states     = new ArrayList<>();
  private final GameStateListener listener = states::add;
  private Game game;
  private Object sessionKit;
  private ProgrammingLanguage lang;

  @BeforeEach public void setUp() throws ReflectiveOperationException
  {
    FileUtils.setLocale(new Locale("en"));
    game = Game.getInstance();
    lang = game.programmingLanguageManager.JAVA;

    // The session must not be saved on the disk of whoever runs the tests
    Field field = Game.class.getDeclaredField("sessionKit");
    field.setAccessible(true);
    sessionKit = field.get(game);
    field.set(game, null);

    game.addGameStateListener(listener);
  }

  @AfterEach public void tearDown() throws ReflectiveOperationException
  {
    game.removeGameStateListener(listener);
    Field field = Game.class.getDeclaredField("sessionKit");
    field.setAccessible(true);
    field.set(game, sessionKit);
  }

  private Exercise exerciseThat(RunOutcome outcome) throws Exception
  {
    Exercise exo = Mockito.mock(Exercise.class);
    Mockito
        .when(exo.compileRunCheck(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.<List<Future<?>>>any(),
                                  ArgumentMatchers.any()))
        .thenReturn(outcome);
    return exo;
  }

  @Test public void testCompilationErrorIsTheOutcome() throws Exception
  {
    RunOutcome error = RunOutcome.newCompilationError(lang, "boom");

    RunOutcome outcome = new LessonRunner(game).runExercise(exerciseThat(error), lang);

    Assertions.assertSame(error, outcome);
    Assertions.assertEquals(List.of(GameState.COMPILATION_STARTED, GameState.COMPILATION_ENDED, GameState.EXECUTION_ENDED), states);
  }

  @Test public void testOutcomeOfTheExecutionIsReturnedAsIs() throws Exception
  {
    RunOutcome passed = new RunOutcome(lang);
    passed.outcome    = RunOutcome.kind.PASS;

    Assertions.assertSame(passed, new LessonRunner(game).runExercise(exerciseThat(passed), lang));
    Assertions.assertEquals(GameState.EXECUTION_ENDED, states.get(states.size() - 1));
  }

  @Test public void testStopGivesAnEmptyOutcome() throws Exception
  {
    Exercise exo = Mockito.mock(Exercise.class);
    Mockito
        .when(exo.compileRunCheck(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.<List<Future<?>>>any(),
                                  ArgumentMatchers.any()))
        .thenThrow(new InterruptedException());

    RunOutcome outcome = new LessonRunner(game).runExercise(exo, lang);

    Assertions.assertSame(lang, outcome.language);
    Assertions.assertFalse(outcome.isPassed());
    Assertions.assertTrue(outcome.executionError.contains("Not run"), outcome.executionError);
    Assertions.assertNull(outcome.compilationError);
    Assertions.assertEquals(0, outcome.totalTests);
    Assertions.assertEquals(List.of(GameState.COMPILATION_STARTED, GameState.COMPILATION_ENDED, GameState.EXECUTION_ENDED), states);
  }
}
