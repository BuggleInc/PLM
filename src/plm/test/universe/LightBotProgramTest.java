package plm.test.universe;

import lessons.lightbot.universe.LightBotProgram;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** The program of a LightBot student is stored as a text: one line per function, one character per instruction. */
public class LightBotProgramTest {

  private static final String EMPTY = " ".repeat(12) + "\n"
                                      + " ".repeat(8) + "\n"
                                      + " ".repeat(8) + "\n";

  @Test public void testEmptyTextIsAProgramOfNoops() { Assertions.assertEquals(EMPTY, LightBotProgram.parse("").toString()); }

  @Test public void testRoundTrip()
  {
    String text = "FFJLR1O2    \n"
                  + "FO      \n"
                  + "LLRR    \n";
    Assertions.assertEquals(text, LightBotProgram.parse(text).toString());
  }

  @Test public void testMissingInstructionsAreNoops()
  {
    Assertions.assertEquals("FL          \n"
                                + "R       \n"
                                + " ".repeat(8) + "\n",
                            LightBotProgram.parse("FL\nR").toString());
  }

  @Test public void testInstructionsBeyondTheSizeOfAFunctionAreIgnored()
  {
    Assertions.assertEquals("F".repeat(12) + "\n"
                                + " ".repeat(8) + "\n"
                                + " ".repeat(8) + "\n",
                            LightBotProgram.parse("F".repeat(20)).toString());
  }

  @Test public void testUnknownInstructionIsRefused() { Assertions.assertThrows(RuntimeException.class, () -> LightBotProgram.parse("FX")); }
}
