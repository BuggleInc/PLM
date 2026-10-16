package plm.test.universe;

import lessons.sort.pancake.universe.PancakeWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** A randomized world stays known as such, also once copied, so that its answer is never taken from the cache. */
public class PancakeRandomizedTest {

  @Test public void testRandomizedWorldIsRandomized() { Assertions.assertTrue(new PancakeWorld("random", 7, false).randomize().isRandomized()); }

  @Test public void testFixedWorldIsNotRandomized() { Assertions.assertFalse(new PancakeWorld("fixed", new int[] {2, 1, 3}, false).isRandomized()); }

  @Test public void testTheCopyOfARandomizedWorldIsRandomized()
  {
    Assertions.assertTrue(new PancakeWorld(new PancakeWorld("random", 7, false).randomize()).isRandomized());
  }
}
