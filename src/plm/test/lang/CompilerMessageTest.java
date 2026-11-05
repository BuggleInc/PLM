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

  /** javac, as rendered by LangJava: a template line, a warning, two errors on a line, and notes without location */
  @Test public void javac()
  {
    String text = """
        /tmp/plm/java/plm/runtime4/Entity.java:6: error: cannot find symbol
          symbol:   variable notDeclared
          location: class generated.Entity
        Entity.java:2: error: cannot find symbol
          symbol:   variable y
          location: class generated.Entity
        Entity.java:3: warning: Integer(int) in java.lang.Integer has been deprecated and marked for removal
        Entity.java:4: error: incompatible types: int cannot be converted to java.lang.String
        Entity.java:4: error: incompatible types: java.lang.String cannot be converted to int
        note: /tmp/plm/java/plm/runtime4/Entity.java uses unchecked or unsafe operations.
        note: Recompile with -Xlint:unchecked for details.
        """;
    assertEquals(List.of(unsure(false, "cannot find symbol\nsymbol:   variable notDeclared\nlocation: class generated.Entity"),
                         error(2, "cannot find symbol\nsymbol:   variable y\nlocation: class generated.Entity"),
                         warning(3, "Integer(int) in java.lang.Integer has been deprecated and marked for removal"),
                         error(4, "incompatible types: int cannot be converted to java.lang.String"),
                         error(4, "incompatible types: java.lang.String cannot be converted to int\n"
                                      + "note: /tmp/plm/java/plm/runtime4/Entity.java uses unchecked or unsafe operations.\n"
                                      + "note: Recompile with -Xlint:unchecked for details.")),
                 CompilerMessage.parse(text, "Entity.java", 5));
  }

  /** javac when the compilation succeeds: the notes follow the last warning */
  @Test public void javacWarnings()
  {
    String text = """
        Entity.java:3: warning: Integer(int) in java.lang.Integer has been deprecated and marked for removal
        note: /tmp/plm/java/plm/runtime4/Entity.java uses unchecked or unsafe operations.
        note: Recompile with -Xlint:unchecked for details.
        """;
    assertEquals(List.of(warning(3, "Integer(int) in java.lang.Integer has been deprecated and marked for removal\n"
                                        + "note: /tmp/plm/java/plm/runtime4/Entity.java uses unchecked or unsafe operations.\n"
                                        + "note: Recompile with -Xlint:unchecked for details.")),
                 CompilerMessage.parse(text, "Entity.java", 5));
  }

  /** dotc, as rendered by LangScala: a template line, two errors on a line, and the quoted code after a | */
  @Test public void dotc()
  {
    String text = """
        /tmp/plm/scala/plm/runtime4/Entity.scala:6:20: error: Not found: notDeclared
          |   val templ: Int = notDeclared
          |                    ^
        Entity.scala:2:16: error: Found:    ("s" : String)
          Required: Int
          |   val x: Int = "s"; val y: String = 1
          |                ^
        Entity.scala:2:37: error: Found:    (1 : Int)
          Required: String
          |   val x: Int = "s"; val y: String = 1
          |                                     ^
        Entity.scala:4:3: error: Not found: toto
          |   toto += 1
          |   ^
        """;
    assertEquals(List.of(unsure(false, "Not found: notDeclared"), error(2, "Found:    (\"s\" : String)\nRequired: Int"),
                         error(2, "Found:    (1 : Int)\nRequired: String"), error(4, "Not found: toto")),
                 CompilerMessage.parse(text, "Entity.scala", 5));
  }

  /** dotc when the compilation succeeds with warnings, whose explanations hold blank lines */
  @Test public void dotcWarnings()
  {
    String text = """
        Entity.scala:2:30: warning: match may not be exhaustive.

          It would fail on pattern case: None
          |   val o: Option[Int] = None; o match { case Some(v) => v }
          |                              ^
        Entity.scala:3:15: warning: match may not be exhaustive.

          It would fail on pattern case: List(_, _*), Nil
          |   val l = List(1) match { case 1 :: Nil => 1 }
          |               ^
        """;
    assertEquals(List.of(warning(2, "match may not be exhaustive.\nIt would fail on pattern case: None"),
                         warning(3, "match may not be exhaustive.\nIt would fail on pattern case: List(_, _*), Nil")),
                 CompilerMessage.parse(text, "Entity.scala", 5));
  }
}
