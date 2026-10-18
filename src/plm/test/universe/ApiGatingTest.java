package plm.test.universe;

import static plm.test.lang.CommandExecutorTest.command;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.utils.FileUtils;
import plm.test.lang.CommandExecutorTest.GatedEntity;
import plm.test.lang.CommandExecutorTest.RecordingEntity;
import plm.universe.CommandExecutor;
import plm.universe.Entity;
import plm.universe.PrimitiveRefusedException;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

/** The primitives that an exercise forbids to the student code are refused with an PrimitiveRefusedException explaining why. */
public class ApiGatingTest {

  public static class TypoEntity extends RecordingEntity {
    public TypoEntity() { forbid("twicee", "Think harder."); }
  }

  public static class TeleportingBuggle extends SimpleBuggle {
    public TeleportingBuggle() { forbidTeleportation(); }
  }

  public static class MultiStepBuggle extends SimpleBuggle {
    public MultiStepBuggle() { forbidMultiStep(); }
  }

  @BeforeAll static void initTranslations()
  {
    FileUtils.setLocale(new Locale("en"));
    Game.getInstance();
  }

  /** forbidTeleportation() makes the executor refuse these primitives before even reading their arguments. */
  @ParameterizedTest @ValueSource(strings = {"setX", "setY", "setPos"}) public void testTeleportationIsRefused(String primitive)
  {
    PrimitiveRefusedException e =
        Assertions.assertThrows(PrimitiveRefusedException.class, () -> CommandExecutor.command(new TeleportingBuggle(), primitive + " []", null));
    Assertions.assertTrue(e.getMessage().contains("Sorry Dave"), e.getMessage());
    Assertions.assertTrue(e.getMessage().contains("Walk to your goal instead."), e.getMessage());
  }

  @Test public void testForbiddenPrimitivesAreThoseOfTheEntity()
  {
    Assertions.assertEquals(Set.of("setX", "setY", "setPos"), new TeleportingBuggle().getForbiddenPrimitives().keySet());
    Assertions.assertEquals(Set.of("forward", "backward"), new MultiStepBuggle().getForbiddenPrimitives().keySet());
  }

  /** forbidMultiStep() refuses moving by a count, while step() and stepBack() remain allowed. */
  @ParameterizedTest @ValueSource(strings = {"forward [2]", "backward [3]"}) public void testMultiStepIsRefused(String command)
  {
    PrimitiveRefusedException e = Assertions.assertThrows(PrimitiveRefusedException.class, () -> CommandExecutor.command(new MultiStepBuggle(), command, null));
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

  private static void assertRefused(Entity entity, String command)
  {
    PrimitiveRefusedException e = Assertions.assertThrows(PrimitiveRefusedException.class, () -> CommandExecutor.command(entity, command, null));
    Assertions.assertTrue(e.getMessage().contains("Sorry Dave"), e.getMessage());
  }

  /** The documentation of the world (Ctrl-H) hides the primitives forbidden by the exercise and the undocumented ones, and orders the groups. */
  @Test public void testDocumentationHidesForbiddenPrimitives() throws BrokenProgrammingLanguageException
  {
    ProgrammingLanguage previous = Game.getInstance().getProgrammingLanguage();
    try {
      Game.getInstance().setProgrammingLanguage(Game.getInstance().programmingLanguageManager.JAVA);
      BuggleWorld world = new BuggleWorld("doc", 3, 3);
      world.addEntity(new TeleportingBuggle()); // forbids setX(), setY() and setPos()
      String about = world.getAbout();

      Assertions.assertTrue(about.contains("void step()"), about);
      for (String hidden : new String[] {"setX(", "setPos(", "Left()", "penDown()"})
        Assertions.assertFalse(about.contains(hidden), hidden + " should be hidden:\n" + about);

      int moving = about.indexOf("<b>Moving</b>"), brush = about.indexOf("<b>About the brush</b>"),
          interacting = about.indexOf("<b>Interacting with the world</b>");
      Assertions.assertTrue(0 <= moving && moving < brush && brush < interacting, about);
    } finally {
      Game.getInstance().setProgrammingLanguage(previous);
    }
  }
}
