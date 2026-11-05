package plm.test.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import plm.core.lang.CompilerMessage;

/**
 * The messages of the compilers are located in the editor when they surely designate one of its lines, with their notes but
 * without the code that they quote. The samples are the real outputs of each compiler, as rendered by its language.
 */
public class CompilerMessageTest {

  private static CompilerMessage error(int line, String text) { return new CompilerMessage(line, true, false, text); }
  private static CompilerMessage warning(int line, String text) { return new CompilerMessage(line, true, true, text); }
  private static CompilerMessage unsure(boolean warning, String text) { return new CompilerMessage(1, false, warning, text); }

  @Test public void linesWithoutLocationAreUnsureErrors()
  {
    assertEquals(List.of(unsure(false, "Your code must call left() exactly once.\nIt calls it twice."), unsure(false, "Factorize it.")),
                 CompilerMessage.parse("Your code must call left() exactly once.\n  It calls it twice.\nFactorize it.\n", "Entity.java", 10));
  }

  @Test public void onlyTheLinesOfTheEditorAreSure()
  {
    String text = """
        Entity.java:0: error: before the editor
        Entity.java:4: error: last line of the editor
        Entity.java:5: error: after the editor
        Main.java:2: error: in another file
        error: nowhere
        """;
    assertEquals(List.of(unsure(false, "before the editor"), error(4, "last line of the editor"), unsure(false, "after the editor"),
                         unsure(false, "in another file"), unsure(false, "nowhere")),
                 CompilerMessage.parse(text, "Entity.java", 4));
  }

  @Test public void notesGoToThePreviousMessageOrAreWarnings()
  {
    String text = """
        note: alone
        Tab.c:3:1: error: first
        Other.h:2:5: note: declared here
        """;
    assertEquals(List.of(unsure(true, "alone"), error(3, "first\nnote: declared here")), CompilerMessage.parse(text, "Tab.c", 9));
  }
}
