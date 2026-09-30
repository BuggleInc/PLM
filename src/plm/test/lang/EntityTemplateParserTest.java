package plm.test.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.lang.LangC;
import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.EntityTemplateParser;
import plm.core.model.lesson.TemplatedEntity;

/**
 * Characterization tests of {@link EntityTemplateParser#parse}: expected values were derived by hand from the current
 * behavior on small synthetic entity files.
 */
public class EntityTemplateParserTest {

  private static TemplatedEntity parse(String content, ProgrammingLanguage lang) throws PLMCompilerException
  {
    return EntityTemplateParser.parse(content, lang, "Bar", "Foo");
  }

  private static String lines(String... l) { return String.join("\n", l); }

  /** BEGIN/END TEMPLATE around a BEGIN/END SOLUTION, Python: no whitespace rewriting at all. */
  @Test public void testPythonTemplateWithSolution() throws PLMCompilerException
  {
    String content    = lines("import x", "def run():", "  # BEGIN TEMPLATE", "  a = 1", "  # BEGIN SOLUTION", "  b = 2", "  # END SOLUTION", "  c = 3",
                              "  # END TEMPLATE", "  end()");
    TemplatedEntity e = parse(content, new LangPython());

    Assertions.assertEquals("  a = 1\n  c = 3\n", e.initialContent());
    Assertions.assertEquals("import x\ndef run():\n$body\n  end()\n", e.template());
    Assertions.assertEquals(content + "\n", e.correction());
    Assertions.assertNotNull(e.extraction());
  }

  /** Only BEGIN/END SOLUTION: the template is empty and the tail starts right after the solution. */
  @Test public void testPythonSolutionOnly() throws PLMCompilerException
  {
    TemplatedEntity e = parse(lines("def run():", "  # BEGIN SOLUTION", "  x = 1", "  # END SOLUTION", "  end()"), new LangPython());

    Assertions.assertEquals("", e.initialContent());
    Assertions.assertEquals("def run():\n$body\n  end()\n", e.template());
    Assertions.assertEquals("  # BEGIN SOLUTION\n  x = 1\n  # END SOLUTION\n", e.correctionBody());
  }

  /** BEGIN/END SOLUTIONHELPER inside the template head is kept in the correction only. */
  @Test public void testPythonSolutionHelperInTemplateHead() throws PLMCompilerException
  {
    String content = lines("def run():", "  # BEGIN TEMPLATE", "  a = 1", "  # BEGIN SOLUTIONHELPER", "  h = 0", "  # END SOLUTIONHELPER", "  # END TEMPLATE");
    TemplatedEntity e = parse(content, new LangPython());

    Assertions.assertEquals("  a = 1\n", e.initialContent());
    Assertions.assertEquals("def run():\n$body\n", e.template());
    Assertions.assertEquals(content + "\n", e.correction());
  }

  /** Java: class declaration rewritten, package line dropped, initial content dedented; head keeps its own lines/comments now. */
  @Test public void testJavaFlatteningAndRewrites() throws PLMCompilerException
  {
    String content    = lines("package foo;", "public class FooEntity {", "  // comment", "  /* BEGIN TEMPLATE */", "  int a;", "  /* BEGIN SOLUTION */",
                              "  int b;", "  /* END SOLUTION */", "  /* END TEMPLATE */", "}");
    TemplatedEntity e = parse(content, new LangJava());

    Assertions.assertEquals("int a;\n", e.initialContent());
    Assertions.assertEquals("package generated;\npublic class Bar {\n  // comment\n$body\n}\n", e.template());
    Assertions.assertFalse(e.template().contains("package foo")); // no per-exercise package declaration
    Assertions.assertTrue(e.correction().startsWith("\npublic class Bar {\n"));
    Assertions.assertTrue(e.correction().contains("// comment")); // correction stays a faithful copy of the file, comments included
  }

  /** C: a {@code #line} directive is inserted in the head right before the template. */
  @Test public void testCLineDirective() throws PLMCompilerException
  {
    TemplatedEntity e = parse(lines("int x;", "/* BEGIN TEMPLATE */", "a();", "/* END TEMPLATE */"), new LangC());

    Assertions.assertEquals("a();\n", e.initialContent());
    Assertions.assertEquals("int x;\n#line 1 \"Bar.c\" \n$body\n", e.template());
  }

  /** IMPORT sections are exposed separately and removed from the head, but kept in the correction. */
  @Test public void testJavaImports() throws PLMCompilerException
  {
    TemplatedEntity e = parse(lines("/* BEGIN IMPORT */", "import java.util.Stack;", "/* END IMPORT */", "public class FooEntity {", "  /* BEGIN TEMPLATE */",
                                    "  int a;", "  /* END TEMPLATE */", "}"),
                              new LangJava());

    Assertions.assertEquals("import java.util.Stack;\n", e.imports());
    Assertions.assertEquals("public class Bar {\n$body\n}\n", e.template());
    Assertions.assertEquals("int a;\n", e.initialContent());
    Assertions.assertTrue(e.correction().contains("import java.util.Stack;"));
  }

  @Test public void testUnclosedImport()
  {
    Assertions.assertThrows(RuntimeException.class, () -> parse(lines("/* BEGIN IMPORT */", "import java.util.Stack;"), new LangJava()));
  }

  @Test public void testSolutionInsideImport()
  {
    Assertions.assertThrows(RuntimeException.class,
                            () -> parse(lines("/* BEGIN IMPORT */", "/* BEGIN SOLUTION */", "/* END SOLUTION */", "/* END IMPORT */"), new LangJava()));
  }

  /** REMOTE narrows head/tail down to what is written between its markers; correctionBody keeps the TEMPLATE markers. */
  @Test public void testRemoteNarrowsHeadAndTail() throws PLMCompilerException
  {
    TemplatedEntity e = parse(lines("package foo;", "public class FooEntity {", "  /* BEGIN REMOTE */", "  void run() {", "    /* BEGIN TEMPLATE */",
                                    "    int a;", "    /* END TEMPLATE */", "  }", "  /* END REMOTE */", "}"),
                              new LangJava());

    Assertions.assertEquals("  void run() {\n$body\n  }\n", e.template());
    Assertions.assertEquals("int a;\n", e.initialContent());
    Assertions.assertEquals("    /* BEGIN TEMPLATE */\n    int a;\n    /* END TEMPLATE */\n", e.correctionBody());
  }

  @Test public void testUnclosedRemote()
  {
    Assertions.assertThrows(RuntimeException.class, () -> parse(lines("/* BEGIN REMOTE */", "/* BEGIN TEMPLATE */", "/* END TEMPLATE */"), new LangJava()));
  }

  /** REMOTE must fully enclose the templated region: closing it mid-template is rejected. */
  @Test public void testRemoteClosingInsideTemplate()
  {
    Assertions.assertThrows(RuntimeException.class,
                            () -> parse(lines("/* BEGIN REMOTE */", "/* BEGIN TEMPLATE */", "/* END REMOTE */", "/* END TEMPLATE */"), new LangJava()));
  }

  @Test public void testNoTemplateNorSolution()
  {
    Assertions.assertThrows(RuntimeException.class, () -> parse(lines("public class FooEntity {", "}"), new LangJava()));
  }

  /** SOLUTIONHELPER inside REMOTE but outside the template: kept in correctionTemplate only. */
  @Test public void testSolutionHelperInRemoteHeadAndTail() throws PLMCompilerException
  {
    TemplatedEntity e = parse(lines("public class FooEntity {", "  /* BEGIN REMOTE */", "  /* BEGIN SOLUTIONHELPER */", "  int h;",
                                    "  /* END SOLUTIONHELPER */", "  void run() {", "    /* BEGIN TEMPLATE */", "    int a;", "    /* END TEMPLATE */",
                                    "    /* BEGIN SOLUTIONHELPER */", "    check();", "    /* END SOLUTIONHELPER */", "  }", "  /* END REMOTE */", "}"),
                              new LangJava());

    Assertions.assertEquals("  void run() {\n$body\n  }\n", e.template());
    Assertions.assertEquals("  int h;\n  void run() {\n$body\n    check();\n  }\n", e.correctionTemplate());
  }

  /** SOLUTIONHELPER before BEGIN REMOTE or after END REMOTE would be silently dropped: rejected. */
  @Test public void testSolutionHelperOutsideRemote()
  {
    Assertions.assertThrows(RuntimeException.class,
                            ()
                                -> parse(lines("/* BEGIN SOLUTIONHELPER */", "/* END SOLUTIONHELPER */", "/* BEGIN REMOTE */", "/* BEGIN TEMPLATE */",
                                               "/* END TEMPLATE */", "/* END REMOTE */"),
                                         new LangJava()));
    Assertions.assertThrows(RuntimeException.class,
                            ()
                                -> parse(lines("/* BEGIN REMOTE */", "/* BEGIN TEMPLATE */", "/* END TEMPLATE */", "/* END REMOTE */",
                                               "/* BEGIN SOLUTIONHELPER */", "/* END SOLUTIONHELPER */"),
                                         new LangJava()));
  }

  /** SOLUTIONHELPER must not straddle the template boundary. */
  @Test public void testSolutionHelperStraddlingTemplate()
  {
    Assertions.assertThrows(
        RuntimeException.class,
        () -> parse(lines("/* BEGIN SOLUTIONHELPER */", "/* BEGIN TEMPLATE */", "/* END SOLUTIONHELPER */", "/* END TEMPLATE */"), new LangJava()));
  }
}
