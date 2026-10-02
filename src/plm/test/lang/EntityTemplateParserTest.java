package plm.test.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.lang.LangC;
import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.EntityTemplateParser;
import plm.core.model.session.SourceFile;

/**
 * Characterization tests of {@link EntityTemplateParser#parse}: expected values were derived by hand from the current
 * behavior on small synthetic entity files.
 */
public class EntityTemplateParserTest {

  private static SourceFile parse(String content, ProgrammingLanguage lang) throws PLMCompilerException
  {
    return EntityTemplateParser.parse(content, lang, "Bar", "Foo");
  }

  private static String lines(String... l) { return String.join("\n", l); }

  /** BEGIN/END TEMPLATE around a BEGIN/END SOLUTION, Python: the common indentation is removed from the editor content, and kept apart. */
  @Test public void testPythonTemplateWithSolution() throws PLMCompilerException
  {
    String content    = lines("import x", "def run():", "  # BEGIN TEMPLATE", "  a = 1", "  # BEGIN SOLUTION", "  b = 2", "  # END SOLUTION", "  c = 3",
                              "  # END TEMPLATE", "  end()");
    SourceFile e = parse(content, new LangPython());

    Assertions.assertEquals("a = 1\nc = 3\n", e.getBody());
    Assertions.assertEquals(2, e.getBodyIndent());
    Assertions.assertEquals("import x\ndef run():\n$body\n  end()\n", e.getTemplate());
    Assertions.assertEquals(content + "\n", e.getCorrection());
  }

  /** Only BEGIN/END SOLUTION: the template is empty and the tail starts right after the solution. */
  @Test public void testPythonSolutionOnly() throws PLMCompilerException
  {
    SourceFile e = parse(lines("def run():", "  # BEGIN SOLUTION", "  x = 1", "  # END SOLUTION", "  end()"), new LangPython());

    Assertions.assertEquals("", e.getBody());
    Assertions.assertEquals(2, e.getBodyIndent()); // the indentation of the solution itself
    Assertions.assertEquals("def run():\n$body\n  end()\n", e.getTemplate());
    Assertions.assertEquals("  # BEGIN SOLUTION\n  x = 1\n  # END SOLUTION\n", e.getCorrectionBody());
  }

  /** A second BEGIN/END SOLUTION inside the template is kept in the correction only. */
  @Test public void testPythonSecondSolutionInTemplate() throws PLMCompilerException
  {
    String content    = lines("def run():", "  # BEGIN TEMPLATE", "  a = 1", "  # BEGIN SOLUTION", "  h = 0", "  # END SOLUTION", "  # END TEMPLATE");
    SourceFile e = parse(content, new LangPython());

    Assertions.assertEquals("a = 1\n", e.getBody());
    Assertions.assertEquals("def run():\n$body\n", e.getTemplate());
    Assertions.assertEquals(content + "\n", e.getCorrection());
  }

  /** Java: head keeps its own lines/comments, initial content is dedented, and the correction is a faithful copy of the file. */
  @Test public void testJavaFlattening() throws PLMCompilerException
  {
    String content    = lines("package foo;", "public class FooEntity {", "  // comment", "  /* BEGIN TEMPLATE */", "  int a;", "  /* BEGIN SOLUTION */",
                              "  int b;", "  /* END SOLUTION */", "  /* END TEMPLATE */", "}");
    SourceFile e = parse(content, new LangJava());

    Assertions.assertEquals("int a;\n", e.getBody());
    Assertions.assertEquals("package foo;\npublic class FooEntity {\n  // comment\n$body\n}\n", e.getTemplate());
    Assertions.assertEquals(content + "\n", e.getCorrection());
  }

  /** C: the template is left as is (the {@code #line} directive is added later, when compiling). */
  @Test public void testCTemplate() throws PLMCompilerException
  {
    SourceFile e = parse(lines("int x;", "/* BEGIN TEMPLATE */", "a();", "/* END TEMPLATE */"), new LangC());

    Assertions.assertEquals("a();\n", e.getBody());
    Assertions.assertEquals("int x;\n$body\n", e.getTemplate());
  }

  /** IMPORT sections are exposed separately and removed from the head, but kept in the correction. */
  @Test public void testJavaImports() throws PLMCompilerException
  {
    SourceFile e = parse(lines("/* BEGIN IMPORT */", "import java.util.Stack;", "/* END IMPORT */", "public class FooEntity {", "  /* BEGIN TEMPLATE */",
                                    "  int a;", "  /* END TEMPLATE */", "}"),
                              new LangJava());

    Assertions.assertEquals("import java.util.Stack;\n", e.getImports());
    Assertions.assertEquals("public class Bar {\n$body\n}\n", e.getTemplate());
    Assertions.assertEquals("int a;\n", e.getBody());
    Assertions.assertTrue(e.getCorrection().contains("import java.util.Stack;"));
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
    SourceFile e = parse(lines("package foo;", "public class FooEntity {", "  /* BEGIN REMOTE */", "  void run() {", "    /* BEGIN TEMPLATE */",
                                    "    int a;", "    /* END TEMPLATE */", "  }", "  /* END REMOTE */", "}"),
                              new LangJava());

    Assertions.assertEquals("  void run() {\n$body\n  }\n", e.getTemplate());
    Assertions.assertEquals("int a;\n", e.getBody());
    Assertions.assertEquals("    /* BEGIN TEMPLATE */\n    int a;\n    /* END TEMPLATE */\n", e.getCorrectionBody());
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

  /** SOLUTION in REMOTE but outside the template: kept in correctionTemplate only. */
  @Test public void testHiddenSolutionInRemoteHeadAndTail() throws PLMCompilerException
  {
    SourceFile e = parse(lines("public class FooEntity {", "  /* BEGIN REMOTE */", "  /* BEGIN SOLUTION */", "  int h;", "  /* END SOLUTION */",
                                    "  void run() {", "    /* BEGIN TEMPLATE */", "    int a;", "    /* END TEMPLATE */", "    /* BEGIN SOLUTION */",
                                    "    check();", "    /* END SOLUTION */", "  }", "  /* END REMOTE */", "}"),
                              new LangJava());

    Assertions.assertEquals("  void run() {\n$body\n  }\n", e.getTemplate());
    Assertions.assertEquals("  int h;\n  void run() {\n$body\n    check();\n  }\n", e.getCorrectionTemplate());
  }

  /** A SOLUTION before BEGIN REMOTE or after END REMOTE would be silently dropped: rejected. */
  @Test public void testHiddenSolutionOutsideRemote()
  {
    Assertions.assertThrows(RuntimeException.class,
                            ()
                                -> parse(lines("/* BEGIN SOLUTION */", "/* END SOLUTION */", "/* BEGIN REMOTE */", "/* BEGIN TEMPLATE */", "/* END TEMPLATE */",
                                               "/* END REMOTE */"),
                                         new LangJava()));
    Assertions.assertThrows(RuntimeException.class,
                            ()
                                -> parse(lines("/* BEGIN REMOTE */", "/* BEGIN TEMPLATE */", "/* END TEMPLATE */", "/* END REMOTE */", "/* BEGIN SOLUTION */",
                                               "/* END SOLUTION */"),
                                         new LangJava()));
  }

  /** A SOLUTION must not straddle the template boundary. */
  @Test public void testHiddenSolutionStraddlingTemplate()
  {
    Assertions.assertThrows(RuntimeException.class,
                            () -> parse(lines("/* BEGIN SOLUTION */", "/* BEGIN TEMPLATE */", "/* END SOLUTION */", "/* END TEMPLATE */"), new LangJava()));
  }

  /** Without a TEMPLATE, the single SOLUTION plays its role: several of them are ambiguous and rejected. */
  @Test public void testSeveralSolutionsWithoutTemplate()
  {
    Assertions.assertThrows(RuntimeException.class,
                            () -> parse(lines("/* BEGIN SOLUTION */", "/* END SOLUTION */", "/* BEGIN SOLUTION */", "/* END SOLUTION */"), new LangJava()));
  }

  /** With a TEMPLATE, any number of SOLUTIONs is fine, before, inside and after it. */
  @Test public void testSeveralSolutionsWithTemplate() throws PLMCompilerException
  {
    SourceFile e =
        parse(lines("/* BEGIN SOLUTION */", "int h;", "/* END SOLUTION */", "/* BEGIN TEMPLATE */", "/* BEGIN SOLUTION */", "int a;", "/* END SOLUTION */",
                    "/* BEGIN SOLUTION */", "int b;", "/* END SOLUTION */", "/* END TEMPLATE */", "/* BEGIN SOLUTION */", "int t;", "/* END SOLUTION */"),
              new LangJava());

    Assertions.assertEquals("", e.getBody());
    Assertions.assertEquals("$body\n", e.getTemplate());
    Assertions.assertEquals("int h;\n$body\nint t;\n", e.getCorrectionTemplate());
  }

  /** Python: the tabs of the templated region are expanded the way python reads them, to find out its indentation. */
  @Test public void testPythonTabs() throws PLMCompilerException
  {
    SourceFile e =
        parse(lines("def run():", "\t# BEGIN TEMPLATE", "\ta = 1", "\t# BEGIN SOLUTION", "\tb = 2", "\t# END SOLUTION", "\t# END TEMPLATE"), new LangPython());

    Assertions.assertEquals("a = 1\n", e.getBody());
    Assertions.assertEquals(8, e.getBodyIndent());
  }
}
