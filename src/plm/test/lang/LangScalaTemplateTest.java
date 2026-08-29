package plm.test.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.lang.LangScala;

/**
 * Exercises LangJava.getCorrectedTemplate()'s well-formedness validation.
 */
public class LangScalaTemplateTest {

  private void assertRejected(String correction, String expectedMessageSubstring)
  {
    PLMCompilerException e = Assertions.assertThrows(PLMCompilerException.class, () -> LangScala.getCorrectedTemplate(correction));
    Assertions.assertTrue(e.getMessage().contains(expectedMessageSubstring),
                          "Expected message to contain \"" + expectedMessageSubstring + "\" but got: " + e.getMessage());
  }

  private void assertAccepted(String correction, String expectedTemplate) throws PLMCompilerException
  {
    Assertions.assertEquals(expectedTemplate, LangScala.getCorrectedTemplate(correction));
  }

  @Test public void testNoRunAtAll() { assertRejected("class X { def foo() = 1 }", "No 'def run('"); }

  @Test public void testMultipleRun() { assertRejected("class X { def run() { } def run() { } }", "expected exactly one"); }

  /** The exact welcome.Environment shape that motivated this whole validation pass -- see the class javadoc. */
  @Test public void testNoTemplateMarkersRegression()
  {
    String correction = "class EnvironmentEntity extends SimpleBuggle {\n"
                        + "\tprotected override def run() { \n"
                        + "\t\t/* BEGIN SOLUTION */\n"
                        + "\t\tstepForward();\n"
                        + "\t\t/* END SOLUTION */\n"
                        + "\t}\n"
                        + "}";
    assertRejected(correction, "No '/* BEGIN TEMPLATE */'");
  }

  @Test public void testTemplateBeginWithoutEnd() { assertRejected("def run() { /* BEGIN TEMPLATE */ foo() }", "must be paired one-to-one"); }

  @Test public void testTemplateEndWithoutBegin() { assertRejected("def run() { foo() /* END TEMPLATE */ }", "must be paired one-to-one"); }

  @Test public void testMultipleTemplatePairs()
  {
    assertRejected("def run() { /* BEGIN TEMPLATE */ foo() /* END TEMPLATE */ /* BEGIN TEMPLATE */ bar() /* END TEMPLATE */ }", "exactly one pair");
  }

  @Test public void testTemplateMarkersReversed()
  {
    assertRejected("def run() { /* END TEMPLATE */ foo() /* BEGIN TEMPLATE */ }", "must appear in that order");
  }

  @Test public void testSolutionBeginWithoutEnd()
  {
    assertRejected("def run() { /* BEGIN TEMPLATE */ /* BEGIN SOLUTION */ foo() /* END TEMPLATE */ }", "must be paired one-to-one");
  }

  @Test public void testAmbiguousPartialOverlap()
  {
    String correction = "def run() {\n"
                        + "  /* BEGIN TEMPLATE */\n"
                        + "  foo()\n"
                        + "}\n"
                        + "def bar() { }\n"
                        + "/* END TEMPLATE */";
    assertRejected(correction, "partially overlaps run()");
  }

  /** Well-formed: the templated region includes run()'s own declaration (signature and all). */
  @Test public void testWellFormedTemplateContainsRunDeclaration() throws PLMCompilerException
  {
    String correction = "/* BEGIN TEMPLATE */\n"
                        + "def run() {\n"
                        + "  /* BEGIN SOLUTION */\n"
                        + "  foo()\n"
                        + "  /* END SOLUTION */\n"
                        + "}\n"
                        + "/* END TEMPLATE */";
    assertAccepted(correction, "$package\n\n$imports\n\nobject Entity {\n$dependency\n\t\n$body\n}");
  }

  /** Well-formed: the templated region sits entirely inside run()'s own body, not touching its declaration. */
  @Test public void testWellFormedTemplateInsideRunBody() throws PLMCompilerException
  {
    String correction = "def run() {\n"
                        + "  /* BEGIN TEMPLATE */\n"
                        + "  /* BEGIN SOLUTION */\n"
                        + "  foo()\n"
                        + "  /* END SOLUTION */\n"
                        + "  /* END TEMPLATE */\n"
                        + "}";
    assertAccepted(correction, "$package\n\n$imports\n\nobject Entity {\n$dependency\n\tdef run(): Unit = {\n$body\t}\n}");
  }

  /** Well-formed: the templated region is a separate method entirely, disjoint from run(). */
  @Test public void testWellFormedTemplateDisjointFromRun() throws PLMCompilerException
  {
    String correction = "def run() {\n"
                        + "  foo()\n"
                        + "}\n"
                        + "/* BEGIN TEMPLATE */\n"
                        + "def foo() {\n"
                        + "  /* BEGIN SOLUTION */\n"
                        + "  bar()\n"
                        + "  /* END SOLUTION */\n"
                        + "}\n"
                        + "/* END TEMPLATE */";
    assertAccepted(correction, "$package\n\n$imports\n\nobject Entity {\n$dependency\n$run\n\t\n$body\n}");
  }
}
