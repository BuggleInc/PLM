package plm.test.universe;

import java.util.Locale;
import java.util.Set;
import lessons.welcome.array.basics.Array1Entity;
import lessons.welcome.loopfor.LoopForEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import plm.core.model.Game;
import plm.core.utils.FileUtils;
import plm.universe.CommandExecutor;

/** The primitives that an exercise forbids to the student code are refused with an UnsupportedOperationException explaining why. */
public class ApiGatingTest {

  @BeforeAll static void initTranslations()
  {
    FileUtils.setLocale(new Locale("en"));
    Game.getInstance();
  }

  /** Array1Entity calls forbidTeleportation(), so the executor refuses these primitives before even reading their arguments. */
  @ParameterizedTest @ValueSource(strings = {"setX", "setY", "setPos"}) public void testTeleportationIsRefused(String primitive)
  {
    UnsupportedOperationException e =
        Assertions.assertThrows(UnsupportedOperationException.class, () -> CommandExecutor.command(new Array1Entity(), primitive + " []", null));
    Assertions.assertTrue(e.getMessage().contains("Sorry Dave"), e.getMessage());
    Assertions.assertTrue(e.getMessage().contains("Walk to your goal instead."), e.getMessage());
  }

  @Test public void testForbiddenPrimitivesAreThoseOfTheEntity()
  {
    Assertions.assertEquals(Set.of("setX", "setY", "setPos"), new Array1Entity().getForbiddenPrimitives().keySet());
    Assertions.assertTrue(new LoopForEntity().getForbiddenPrimitives().isEmpty());
  }

  /** LoopForEntity calls forbidMultiStep(): moving by a count is refused, while stepForward() and stepBackward() remain allowed. */
  @Test public void testMultiStepIsRefused()
  {
    LoopForEntity buggle                   = new LoopForEntity();
    UnsupportedOperationException forward  = Assertions.assertThrows(UnsupportedOperationException.class, () -> buggle.forward(2));
    UnsupportedOperationException backward = Assertions.assertThrows(UnsupportedOperationException.class, () -> buggle.backward(2));
    Assertions.assertTrue(forward.getMessage().contains("Use a loop instead."), forward.getMessage());
    Assertions.assertTrue(backward.getMessage().contains("Use a loop instead."), backward.getMessage());
  }
}
