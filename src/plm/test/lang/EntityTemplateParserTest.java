package plm.test.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.lang.LangC;
import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.LangScala;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.EntityTemplateParser;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.EntityFileSegments;
import plm.core.model.session.SourceFile;

/**
 * Characterization tests of {@link EntityTemplateParser#parse}: expected values were derived by hand from the current
 * behavior on small synthetic entity files.
 */
public class EntityTemplateParserTest {

  /** Checks what surrounds the body in the source compiled for {@code which}. */
  private static void assertFrame(SourceFile e, StudentOrCorrection which, String pre, String post)
  {
    EntityFileSegments segments = e.getSegments(which);
    Assertions.assertEquals(pre, segments.pre());
    Assertions.assertEquals(post, segments.post());
  }

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

    Assertions.assertEquals("a = 1\nc = 3\n", e.getEditorContent());
    Assertions.assertEquals(2, e.getBodyIndent());
    assertFrame(e, StudentOrCorrection.STUDENT, "import x\ndef run():\n", "\n  end()\n");
  }

  /** Only BEGIN/END SOLUTION: the template is empty and the tail starts right after the solution. */
  @Test public void testPythonSolutionOnly() throws PLMCompilerException
  {
    SourceFile e = parse(lines("def run():", "  # BEGIN SOLUTION", "  x = 1", "  # END SOLUTION", "  end()"), new LangPython());

    Assertions.assertEquals("", e.getEditorContent());
    Assertions.assertEquals(2, e.getBodyIndent()); // the indentation of the solution itself
    assertFrame(e, StudentOrCorrection.STUDENT, "def run():\n", "\n  end()\n");
    Assertions.assertEquals("  # BEGIN SOLUTION\n  x = 1\n  # END SOLUTION\n", e.getSegments(StudentOrCorrection.CORRECTION).body());
  }

  /** A second BEGIN/END SOLUTION inside the template is kept in the correction only. */
  @Test public void testPythonSecondSolutionInTemplate() throws PLMCompilerException
  {
    String content    = lines("def run():", "  # BEGIN TEMPLATE", "  a = 1", "  # BEGIN SOLUTION", "  h = 0", "  # END SOLUTION", "  # END TEMPLATE");
    SourceFile e = parse(content, new LangPython());

    Assertions.assertEquals("a = 1\n", e.getEditorContent());
    assertFrame(e, StudentOrCorrection.STUDENT, "def run():\n", "\n");
  }

  /** Java: head keeps its own lines/comments, initial content is dedented, and the correction is a faithful copy of the file. */
  @Test public void testJavaFlattening() throws PLMCompilerException
  {
    String content    = lines("package foo;", "public class FooEntity {", "  // comment", "  /* BEGIN TEMPLATE */", "  int a;", "  /* BEGIN SOLUTION */",
                              "  int b;", "  /* END SOLUTION */", "  /* END TEMPLATE */", "}");
    SourceFile e = parse(content, new LangJava());

    Assertions.assertEquals("int a;\n", e.getEditorContent());
    assertFrame(e, StudentOrCorrection.STUDENT, "package foo;\npublic class FooEntity {\n  // comment\n", "\n}\n");
  }

  /** C: the template is left as is (the {@code #line} directive is added later, when compiling). */
  @Test public void testCTemplate() throws PLMCompilerException
  {
    SourceFile e =
        parse(lines("int x;", "/* BEGIN TEMPLATE */", "a();", "/* BEGIN SOLUTION */", "b();", "/* END SOLUTION */", "/* END TEMPLATE */"), new LangC());

    Assertions.assertEquals("a();\n", e.getEditorContent());
    assertFrame(e, StudentOrCorrection.STUDENT, "int x;\n", "\n");
  }

  /** IMPORT sections are exposed separately and removed from the head, but kept in the correction. */
  @Test public void testJavaImports() throws PLMCompilerException
  {
    SourceFile e = parse(lines("/* BEGIN IMPORT */", "import java.util.Stack;", "/* END IMPORT */", "public class FooEntity {", "  /* BEGIN TEMPLATE */",
                               "  int a;", "  /* BEGIN SOLUTION */", "  int b;", "  /* END SOLUTION */", "  /* END TEMPLATE */", "}"),
                         new LangJava());

    Assertions.assertEquals("import java.util.Stack;\n", e.getImports());
    assertFrame(e, StudentOrCorrection.STUDENT, "public class FooEntity {\n", "\n}\n");
    Assertions.assertEquals("int a;\n", e.getEditorContent());
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

  /** REMOTE narrows head/tail down to what is written between its markers; the correction body keeps the TEMPLATE markers. */
  @Test public void testRemoteNarrowsHeadAndTail() throws PLMCompilerException
  {
    SourceFile e = parse(lines("package foo;", "public class FooEntity {", "  /* BEGIN REMOTE */", "  void run() {", "    /* BEGIN TEMPLATE */", "    int a;",
                               "    /* BEGIN SOLUTION */", "    int b;", "    /* END SOLUTION */", "    /* END TEMPLATE */", "  }", "  /* END REMOTE */", "}"),
                         new LangJava());

    assertFrame(e, StudentOrCorrection.STUDENT, "  void run() {\n", "\n  }\n");
    Assertions.assertEquals("int a;\n", e.getEditorContent());
    Assertions.assertEquals("    /* BEGIN TEMPLATE */\n    int a;\n    /* BEGIN SOLUTION */\n    int b;\n    /* END SOLUTION */\n    /* END TEMPLATE */\n",
                            e.getSegments(StudentOrCorrection.CORRECTION).body());
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

  /** SOLUTION in REMOTE but outside the template: kept in the correction frame only. */
  @Test public void testHiddenSolutionInRemoteHeadAndTail() throws PLMCompilerException
  {
    SourceFile e = parse(lines("public class FooEntity {", "  /* BEGIN REMOTE */", "  /* BEGIN SOLUTION */", "  int h;", "  /* END SOLUTION */",
                                    "  void run() {", "    /* BEGIN TEMPLATE */", "    int a;", "    /* END TEMPLATE */", "    /* BEGIN SOLUTION */",
                                    "    check();", "    /* END SOLUTION */", "  }", "  /* END REMOTE */", "}"),
                              new LangJava());

    assertFrame(e, StudentOrCorrection.STUDENT, "  void run() {\n", "\n  }\n");
    assertFrame(e, StudentOrCorrection.CORRECTION, "  int h;\n  void run() {\n", "\n    check();\n  }\n");
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

    Assertions.assertEquals("", e.getEditorContent());
    assertFrame(e, StudentOrCorrection.STUDENT, "", "\n");
    assertFrame(e, StudentOrCorrection.CORRECTION, "int h;\n", "\nint t;\n");
  }

  /** Python: the tabs of the templated region are expanded the way python reads them, to find out its indentation. */
  @Test public void testPythonTabs() throws PLMCompilerException
  {
    SourceFile e =
        parse(lines("def run():", "\t# BEGIN TEMPLATE", "\ta = 1", "\t# BEGIN SOLUTION", "\tb = 2", "\t# END SOLUTION", "\t# END TEMPLATE"), new LangPython());

    Assertions.assertEquals("a = 1\n", e.getEditorContent());
    Assertions.assertEquals(8, e.getBodyIndent());
  }

  /** A TEMPLATE without any SOLUTION is rejected as a likely mistake, unless an (empty) SOLUTION says that the template is the answer. */
  @Test public void testTemplateWithoutSolution() throws PLMCompilerException
  {
    Assertions.assertThrows(RuntimeException.class, () -> parse(lines("/* BEGIN TEMPLATE */", "a();", "/* END TEMPLATE */"), new LangC()));

    SourceFile e = parse(lines("/* BEGIN TEMPLATE */", "a();", "/* BEGIN SOLUTION */", "/* END SOLUTION */", "/* END TEMPLATE */"), new LangC());
    Assertions.assertEquals("a();\n", e.getEditorContent());
  }

  /** The universe is guessed from words found in the entity, the same way in every language. */
  @Test public void testRemote() throws PLMCompilerException
  {
    String solution = "# BEGIN TEMPLATE\n# BEGIN SOLUTION\n# END SOLUTION\n# END TEMPLATE";
    Assertions.assertEquals("RemoteBuggle", parse(lines("from RemoteBuggle import *", solution), new LangPython()).getRemote());
    Assertions.assertEquals("RemoteBat", parse(lines("#include \"RemoteBat.h\"", solution), new LangC()).getRemote());
    Assertions.assertEquals("RemoteTurtle", parse(lines("val t = new Turtle()", solution), new LangScala()).getRemote());
    Assertions.assertEquals("RemoteCons", parse(lines("#include \"RemoteBat.h\"", "#include \"universe/RecList.h\"", solution), new LangC()).getRemote());
    Assertions.assertNull(parse(lines("int x;", solution), new LangC()).getRemote());
  }
}
