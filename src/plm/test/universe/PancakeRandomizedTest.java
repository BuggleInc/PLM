package plm.test.universe;

import lessons.sort.pancake.GatesPancakeEntity;
import lessons.sort.pancake.universe.PancakeWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** The Gates entity reports the instances that it sees only in the randomized worlds, since the other ones are reproducible. */
public class PancakeRandomizedTest {

  private boolean wasRandom(PancakeWorld world)
  {
    GatesPancakeEntity entity = new GatesPancakeEntity();
    world.addEntity(entity);
    return entity.wasRandom();
  }

  @Test public void testRandomizedWorldWasRandom() { Assertions.assertTrue(wasRandom(new PancakeWorld("random", 7, false).randomize())); }

  @Test public void testFixedWorldWasNotRandom() { Assertions.assertFalse(wasRandom(new PancakeWorld("fixed", new int[] {2, 1, 3}, false))); }

  @Test public void testTheCopyOfARandomizedWorldWasRandom()
  {
    Assertions.assertTrue(wasRandom(new PancakeWorld(new PancakeWorld("random", 7, false).randomize())));
  }
}
