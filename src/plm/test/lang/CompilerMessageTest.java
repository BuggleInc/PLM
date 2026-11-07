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

  private static CompilerMessage error(int line, int column, String text) { return new CompilerMessage(line, column, true, false, text); }
  private static CompilerMessage warning(int line, int column, String text) { return new CompilerMessage(line, column, true, true, text); }
  private static CompilerMessage unsure(boolean warning, String text) { return new CompilerMessage(1, 0, false, warning, text); }

  @Test public void linesWithoutLocationAreUnsureErrors()
  {
    assertEquals(List.of(unsure(false, "Your code must call left() exactly once.\nIt calls it twice."), unsure(false, "Factorize it.")),
                 CompilerMessage.parse("Your code must call left() exactly once.\n  It calls it twice.\nFactorize it.\n", "Entity.java", 0, 10));
  }

  @Test public void onlyTheLinesOfTheEditorAreSure()
  {
    String text = """
        Entity.java:2: error: before the editor (in the template)
        Entity.java:6:3: error: last line of the editor
        Entity.java:7:4: error: after the editor
        Main.java:2: error: in another file
        error: nowhere
        """;
    assertEquals(List.of(unsure(false, "before the editor (in the template)"), error(4, 3, "last line of the editor"), unsure(false, "after the editor"),
                         unsure(false, "in another file"), unsure(false, "nowhere")),
                 CompilerMessage.parse(text, "Entity.java", 2, 4));
  }

  @Test public void notesGoToThePreviousMessageOrAreWarnings()
  {
    String text = """
        note: alone
        Tab.c:3:1: error: first
        Other.h:2:5: note: declared here
        """;
    assertEquals(List.of(unsure(true, "alone"), error(3, 1, "first\nnote: declared here")), CompilerMessage.parse(text, "Tab.c", 0, 9));
  }

  /**
   * javac, as rendered by LangJava with the lines of the generated source: a template line (with its full path), a warning, two errors on a line, and notes
   * without location
   */
  @Test public void javac()
  {
    String text = """
        /tmp/plm/java/plm/runtime/lessons_welcome_Environment_STUDENT/Entity.java:6:20: error: cannot find symbol
          symbol:   variable notDeclared
          location: class generated.Entity
        Entity.java:8:5: error: cannot find symbol
          symbol:   variable y
          location: class generated.Entity
        Entity.java:9:17: warning: Integer(int) in java.lang.Integer has been deprecated and marked for removal
        Entity.java:10:22: error: incompatible types: int cannot be converted to java.lang.String
        Entity.java:10:45: error: incompatible types: java.lang.String cannot be converted to int
        note: /tmp/plm/java/plm/runtime/lessons_welcome_Environment_STUDENT/Entity.java uses unchecked or unsafe operations.
        note: Recompile with -Xlint:unchecked for details.
        """;
    assertEquals(List.of(unsure(false, "cannot find symbol\nsymbol:   variable notDeclared\nlocation: class generated.Entity"),
                         error(2, 5, "cannot find symbol\nsymbol:   variable y\nlocation: class generated.Entity"),
                         warning(3, 17, "Integer(int) in java.lang.Integer has been deprecated and marked for removal"),
                         error(4, 22, "incompatible types: int cannot be converted to java.lang.String"),
                         error(4, 45,
                               "incompatible types: java.lang.String cannot be converted to int\n"
                                   + "note: /tmp/plm/java/plm/runtime/lessons_welcome_Environment_STUDENT/Entity.java uses unchecked or unsafe operations.\n"
                                   + "note: Recompile with -Xlint:unchecked for details.")),
                 CompilerMessage.parse(text, "Entity.java", 6, 5));
  }

  /** javac when the compilation succeeds: the notes follow the last warning */
  @Test public void javacWarnings()
  {
    String text = """
        Entity.java:9:17: warning: Integer(int) in java.lang.Integer has been deprecated and marked for removal
        note: /tmp/plm/java/plm/runtime/lessons_welcome_Environment_STUDENT/Entity.java uses unchecked or unsafe operations.
        note: Recompile with -Xlint:unchecked for details.
        """;
    assertEquals(List.of(warning(3, 17,
                                 "Integer(int) in java.lang.Integer has been deprecated and marked for removal\n"
                                     + "note: /tmp/plm/java/plm/runtime/lessons_welcome_Environment_STUDENT/Entity.java uses unchecked or unsafe operations.\n"
                                     + "note: Recompile with -Xlint:unchecked for details.")),
                 CompilerMessage.parse(text, "Entity.java", 6, 5));
  }

  /**
   * dotc, as rendered by LangScala with the lines of the generated source: a template line (with its full path), two errors on a line, and the quoted code
   * after a |
   */
  @Test public void dotc()
  {
    String text = """
        /tmp/plm/scala/plm/runtime/lessons_welcome_Environment_STUDENT/Entity.scala:6:20: error: Not found: notDeclared
          |   val templ: Int = notDeclared
          |                    ^
        Entity.scala:8:16: error: Found:    ("s" : String)
          Required: Int
          |   val x: Int = "s"; val y: String = 1
          |                ^
        Entity.scala:8:37: error: Found:    (1 : Int)
          Required: String
          |   val x: Int = "s"; val y: String = 1
          |                                     ^
        Entity.scala:10:3: error: Not found: toto
          |   toto += 1
          |   ^
        """;
    assertEquals(List.of(unsure(false, "Not found: notDeclared"), error(2, 16, "Found:    (\"s\" : String)\nRequired: Int"),
                         error(2, 37, "Found:    (1 : Int)\nRequired: String"), error(4, 3, "Not found: toto")),
                 CompilerMessage.parse(text, "Entity.scala", 6, 5));
  }

  /** dotc when the compilation succeeds with warnings, whose explanations hold blank lines */
  @Test public void dotcWarnings()
  {
    String text = """
        Entity.scala:8:30: warning: match may not be exhaustive.

          It would fail on pattern case: None
          |   val o: Option[Int] = None; o match { case Some(v) => v }
          |                              ^
        Entity.scala:9:15: warning: match may not be exhaustive.

          It would fail on pattern case: List(_, _*), Nil
          |   val l = List(1) match { case 1 :: Nil => 1 }
          |               ^
        """;
    assertEquals(List.of(warning(2, 30, "match may not be exhaustive.\nIt would fail on pattern case: None"),
                         warning(3, 15, "match may not be exhaustive.\nIt would fail on pattern case: List(_, _*), Nil")),
                 CompilerMessage.parse(text, "Entity.scala", 6, 5));
  }

  /**
   * The syntax check of LangPython, with the lines of the generated source: a warning of the template (with its full path), then an error with its quoted code
   */
  @Test public void python()
  {
    String text = """
        /tmp/plm/python/plm.runtime.lessons_welcome_Environment_STUDENT_Environment/Entity.py:2: warning: SyntaxWarning: invalid escape sequence '\\w'
        Entity.py:7:8: error: SyntaxError: invalid syntax
          |     toto +=
          |            ^
        """;
    assertEquals(List.of(unsure(true, "SyntaxWarning: invalid escape sequence '\\w'"), error(3, 8, "SyntaxError: invalid syntax")),
                 CompilerMessage.parse(text, "Entity.py", 4, 3));
  }

  /** The syntax check of LangPython when the compilation succeeds: warnings only */
  @Test public void pythonWarnings()
  {
    String text = """
        /tmp/plm/python/plm.runtime.lessons_welcome_Environment_STUDENT_Environment/Entity.py:2: warning: SyntaxWarning: invalid escape sequence '\\w'
        Entity.py:8: warning: SyntaxWarning: invalid escape sequence '\\d'
        Entity.py:6: warning: SyntaxWarning: "is" with 'int' literal. Did you mean "=="?
        """;
    assertEquals(List.of(unsure(true, "SyntaxWarning: invalid escape sequence '\\w'"), warning(4, 0, "SyntaxWarning: invalid escape sequence '\\d'"),
                         warning(2, 0, "SyntaxWarning: \"is\" with 'int' literal. Did you mean \"==\"?")),
                 CompilerMessage.parse(text, "Entity.py", 4, 4));
  }

  /**
   * gcc, after the #line directive of LangC: an error in the template (quoted), the context lines, a note, several messages on a
   * line, and an error in the code after the body, whose line is beyond the editor
   */
  @Test public void gcc()
  {
    String text = """
        lessons.welcome.Tab.c:3:13: error: 'notDeclared' undeclared here (not in a function)
            3 | int templ = notDeclared;
              |             ^~~~~~~~~~~
        Tab.c: In function 'run':
        Tab.c:3:3: error: 'toto' undeclared (first use in this function)
        Tab.c:3:3: note: each undeclared identifier is reported only once for each function it appears in
        Tab.c:3:21: warning: initialization of 'int' from 'char *' makes integer from pointer without a cast [-Wint-conversion]
        Tab.c:4:10: warning: format '%d' expects argument of type 'int', but argument 2 has type 'double' [-Wformat=]
        Tab.c:3:17: warning: unused variable 'x' [-Wunused-variable]
        Tab.c:2:7: warning: unused variable 'unused' [-Wunused-variable]
        Tab.c: In function 'post':
        Tab.c:7:25: error: 'undeclaredInPost' undeclared (first use in this function)
        Tab.c:7:43: warning: control reaches end of non-void function [-Wreturn-type]
        """;
    assertEquals(List.of(unsure(false, "'notDeclared' undeclared here (not in a function)"),
                         error(3, 3,
                               "'toto' undeclared (first use in this function)\n"
                                   + "note: each undeclared identifier is reported only once for each function it appears in"),
                         warning(3, 21, "initialization of 'int' from 'char *' makes integer from pointer without a cast [-Wint-conversion]"),
                         warning(4, 10, "format '%d' expects argument of type 'int', but argument 2 has type 'double' [-Wformat=]"),
                         warning(3, 17, "unused variable 'x' [-Wunused-variable]"), warning(2, 7, "unused variable 'unused' [-Wunused-variable]"),
                         unsure(false, "'undeclaredInPost' undeclared (first use in this function)"),
                         unsure(true, "control reaches end of non-void function [-Wreturn-type]")),
                 CompilerMessage.parse(text, "Tab.c", 0, 6));
  }

  /** gcc when the link fails: the messages of the linker are errors of their own, after the warnings of the compiler */
  @Test public void gccLinker()
  {
    String text = """
        Tab.c: In function 'run':
        Tab.c:4:3: warning: implicit declaration of function 'undefinedFunction' [-Wimplicit-function-declaration]
        /usr/bin/ld: /tmp/cckejCOI.o: in function `run':
        /tmp/plm/C/lessons.welcome.Tab-STUDENT-1/lessons.welcome.Tab.c:4:(.text+0x39): undefined reference to `undefinedFunction'
        collect2: error: ld returned 1 exit status
        """;
    assertEquals(List.of(warning(4, 3, "implicit declaration of function 'undefinedFunction' [-Wimplicit-function-declaration]"),
                         unsure(false, "/tmp/plm/C/lessons.welcome.Tab-STUDENT-1/lessons.welcome.Tab.c:4:(.text+0x39): undefined reference to "
                                           + "`undefinedFunction'"),
                         unsure(false, "collect2: error: ld returned 1 exit status")),
                 CompilerMessage.parse(text, "Tab.c", 0, 6));
  }
}
