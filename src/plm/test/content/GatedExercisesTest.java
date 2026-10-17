package plm.test.content;

import static plm.test.lang.CommandExecutorTest.command;

import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.model.Game;
import plm.core.utils.FileUtils;
import plm.universe.CommandExecutor;
import plm.universe.Entity;
import plm.universe.PrimitiveRefusedException;

/** The exercises that gate the API of their world refuse the calls that would defeat their pedagogical goal. */
public class GatedExercisesTest {

  @BeforeAll static void initTranslations()
  {
    FileUtils.setLocale(new Locale("en"));
    Game.getInstance();
  }

  /** Entities of exercises that gate their API, and a call that each of them refuses */
  static Stream<Arguments> refusedCalls()
  {
    return Stream.of(Arguments.of("lessons.welcome.array.basics.Array1Entity", command("setX", 1)),
                     Arguments.of("lessons.welcome.loopfor.LoopForEntity", command("forward", 2)),
                     Arguments.of("lessons.welcome.traversal.column.TraversalByColumnEntity", command("forward", 1)),
                     Arguments.of("lessons.welcome.traversal.column.TraversalByColumnEntity", command("step")),
                     Arguments.of("lessons.welcome.methods.basics.MethodsDogHouseEntity", command("right")),
                     Arguments.of("lessons.recursion.hanoi.LinearHanoiEntity", command("move", 0, 2)));
  }

  @ParameterizedTest(name = "{0}: {1}")
  @MethodSource("refusedCalls")
  public void exercisesRefuseTheirForbiddenCalls(String entityClass, String command) throws ReflectiveOperationException
  {
    var constructor = Class.forName(entityClass).getDeclaredConstructor();
    constructor.setAccessible(true);
    Entity entity = (Entity)constructor.newInstance();

    PrimitiveRefusedException e = Assertions.assertThrows(PrimitiveRefusedException.class, () -> CommandExecutor.command(entity, command, null));
    Assertions.assertTrue(e.getMessage().contains("Sorry Dave"), e.getMessage());
  }
}
