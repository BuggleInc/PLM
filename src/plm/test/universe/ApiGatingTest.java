package plm.test.universe;

import static plm.test.lang.CommandExecutorTest.command;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import lessons.welcome.array.basics.Array1Entity;
import lessons.welcome.loopfor.LoopForEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import plm.core.model.Game;
import plm.core.utils.FileUtils;
import plm.test.lang.CommandExecutorTest.GatedEntity;
import plm.test.lang.CommandExecutorTest.RecordingEntity;
import plm.universe.CommandExecutor;
import plm.universe.Entity;
import plm.universe.PrimitiveRefusedException;
import plm.universe.bugglequest.SimpleBuggle;

/** The primitives that an exercise forbids to the student code are refused with an PrimitiveRefusedException explaining why. */
public class ApiGatingTest {

  public static class TypoEntity extends RecordingEntity {
    public TypoEntity() { forbid("twicee", "Think harder."); }
  }

  @BeforeAll static void initTranslations()
  {
    FileUtils.setLocale(new Locale("en"));
    Game.getInstance();
  }

  /** Array1Entity calls forbidTeleportation(), so the executor refuses these primitives before even reading their arguments. */
  @ParameterizedTest @ValueSource(strings = {"setX", "setY", "setPos"}) public void testTeleportationIsRefused(String primitive)
  {
    PrimitiveRefusedException e =
        Assertions.assertThrows(PrimitiveRefusedException.class, () -> CommandExecutor.command(new Array1Entity(), primitive + " []", null));
    Assertions.assertTrue(e.getMessage().contains("Sorry Dave"), e.getMessage());
    Assertions.assertTrue(e.getMessage().contains("Walk to your goal instead."), e.getMessage());
  }

  @Test public void testForbiddenPrimitivesAreThoseOfTheEntity()
  {
    Assertions.assertEquals(Set.of("setX", "setY", "setPos"), new Array1Entity().getForbiddenPrimitives().keySet());
    Assertions.assertEquals(Set.of("forward", "backward"), new LoopForEntity().getForbiddenPrimitives().keySet());
  }

  /** LoopForEntity calls forbidMultiStep(), so moving by a count is refused while step() and stepBack() remain allowed. */
  @ParameterizedTest @ValueSource(strings = {"forward [2]", "backward [3]"}) public void testMultiStepIsRefused(String command)
  {
    PrimitiveRefusedException e = Assertions.assertThrows(PrimitiveRefusedException.class, () -> CommandExecutor.command(new LoopForEntity(), command, null));
    Assertions.assertTrue(e.getMessage().contains("Use a loop instead."), e.getMessage());
  }

  @Test public void testForbidRejectsAnUnknownPrimitive()
  {
    IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, TypoEntity::new);
    Assertions.assertTrue(e.getMessage().contains("twicee"), e.getMessage());
  }

  @Test public void testForbiddenPrimitivesAreReadOnly()
  {
    Map<String, String> forbidden = new GatedEntity().getForbiddenPrimitives();

    Assertions.assertEquals(Map.of("twice", "Think harder."), forbidden);
    Assertions.assertThrows(UnsupportedOperationException.class, () -> forbidden.put("setFlag", "No."));
  }

  /** The worlds are copied by instantiating the entities again, so the constructor must be enough to forbid the primitives. */
  @Test public void testEveryNewInstanceForbidsTheSamePrimitives() throws ReflectiveOperationException
  {
    Entity copy = new GatedEntity().getClass().getDeclaredConstructor().newInstance();

    Assertions.assertEquals(Map.of("twice", "Think harder."), copy.getForbiddenPrimitives());
  }

  /** Left() and the like are primitives that only explain why the call is refused (wrong case, primitive of the turtles). */
  @ParameterizedTest @ValueSource(strings = {"Left", "Right", "penDown", "penUp"}) public void testLikelyMistakesAreExplained(String mistake)
  {
    assertRefused(new SimpleBuggle(), command(mistake));
  }

  /** The exercises of the lessons that gate their API, and a call that each of them refuses */
  static Stream<Arguments> refusedCallsInExercises()
  {
    return Stream.of(Arguments.of("lessons.welcome.traversal.column.TraversalByColumnEntity", command("forward", 1)),
                     Arguments.of("lessons.welcome.traversal.column.TraversalByColumnEntity", command("step")),
                     Arguments.of("lessons.welcome.methods.basics.MethodsDogHouseEntity", command("right")),
                     Arguments.of("lessons.recursion.hanoi.LinearHanoiEntity", command("move", 0, 2)));
  }

  @ParameterizedTest(name = "{0}: {1}")
  @MethodSource("refusedCallsInExercises")
  public void testExercisesRefuseTheirForbiddenCalls(String entityClass, String command) throws ReflectiveOperationException
  {
    var constructor = Class.forName(entityClass).getDeclaredConstructor();
    constructor.setAccessible(true);

    assertRefused((Entity)constructor.newInstance(), command);
  }

  private static void assertRefused(Entity entity, String command)
  {
    PrimitiveRefusedException e = Assertions.assertThrows(PrimitiveRefusedException.class, () -> CommandExecutor.command(entity, command, null));
    Assertions.assertTrue(e.getMessage().contains("Sorry Dave"), e.getMessage());
  }
}
