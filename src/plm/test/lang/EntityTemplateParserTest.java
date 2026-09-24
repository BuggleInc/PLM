package plm.test.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

  private static TemplatedEntity parse(String content, ProgrammingLanguage lang) { return EntityTemplateParser.parse(content, lang, "Bar", "Foo", null); }

  private static String lines(String... l) { return String.join("\n", l); }

  /** BEGIN/END TEMPLATE around a BEGIN/END SOLUTION, Python: no whitespace rewriting at all. */
  @Test public void testPythonTemplateWithSolution()
  {
    String content    = lines("import x", "def run():", "  # BEGIN TEMPLATE", "  a = 1", "  # BEGIN SOLUTION", "  b = 2", "  # END SOLUTION", "  c = 3",
                              "  # END TEMPLATE", "  end()");
    TemplatedEntity e = parse(content, new LangPython());

    Assertions.assertEquals("  a = 1\n  c = 3\n", e.initialContent());
    Assertions.assertEquals("import x\ndef run():\n$body\n  end()\n", e.template());
    Assertions.assertEquals(2, e.offset());
    Assertions.assertEquals(content + "\n", e.correction());
    Assertions.assertNull(e.extraction());
  }

  /** Only BEGIN/END SOLUTION: the template is empty and the tail starts right after the solution. */
  @Test public void testPythonSolutionOnly()
  {
    TemplatedEntity e = parse(lines("def run():", "  # BEGIN SOLUTION", "  x = 1", "  # END SOLUTION", "  end()"), new LangPython());

    Assertions.assertEquals("", e.initialContent());
    Assertions.assertEquals("def run():\n$body\n  end()\n", e.template());
    Assertions.assertEquals(1, e.offset());
  }

  /** BEGIN/END HIDDEN inside the template head is kept in the correction only. */
  @Test public void testPythonHiddenInTemplateHead()
  {
    String content    = lines("def run():", "  # BEGIN TEMPLATE", "  a = 1", "  # BEGIN HIDDEN", "  h = 0", "  # END HIDDEN", "  # END TEMPLATE");
    TemplatedEntity e = parse(content, new LangPython());

    Assertions.assertEquals("  a = 1\n", e.initialContent());
    Assertions.assertEquals("def run():\n$body\n", e.template());
    Assertions.assertEquals(content + "\n", e.correction());
  }

  /** Java: line comments dropped from the flattened head only, class/package lines rewritten, initial content dedented. */
  @Test public void testJavaFlatteningAndRewrites()
  {
    String content    = lines("package foo;", "public class FooEntity {", "  // comment", "  /* BEGIN TEMPLATE */", "  int a;", "  /* BEGIN SOLUTION */",
                              "  int b;", "  /* END SOLUTION */", "  /* END TEMPLATE */", "}");
    TemplatedEntity e = parse(content, new LangJava());

    Assertions.assertEquals("int a;\n", e.initialContent());
    Assertions.assertEquals("$package  public class Bar {    $body } ", e.template());
    Assertions.assertEquals(0, e.offset());
    Assertions.assertTrue(e.correction().startsWith("$package \npublic class Bar {\n"));
    Assertions.assertTrue(e.correction().contains("// comment")); // correction stays a faithful copy of the file, comments included
  }

  /** C: a {@code #line} directive is inserted in the head right before the template. */
  @Test public void testCLineDirective()
  {
    TemplatedEntity e = parse(lines("int x;", "/* BEGIN TEMPLATE */", "a();", "/* END TEMPLATE */"), new LangC());

    Assertions.assertEquals("a();\n", e.initialContent());
    Assertions.assertEquals("int x;\n#line 1 \"Bar.c\" \n$body\n", e.template());
    Assertions.assertEquals(2, e.offset());
  }

  /** The {@code s/regex/replacement/} rewrites apply to both template and initial content. */
  @Test public void testPatternString()
  {
    TemplatedEntity e =
        EntityTemplateParser.parse(lines("def run():", "  # BEGIN TEMPLATE", "  a = 1", "  # END TEMPLATE"), new LangPython(), "Bar", "Foo", "s/a/z/");

    Assertions.assertEquals("  z = 1\n", e.initialContent());
  }

  /** IMPORT sections are exposed separately and removed from the head, but kept in the correction. */
  @Test public void testJavaImports()
  {
    TemplatedEntity e = parse(lines("/* BEGIN IMPORT */", "import java.util.Stack;", "/* END IMPORT */", "public class FooEntity {", "  /* BEGIN TEMPLATE */",
                                    "  int a;", "  /* END TEMPLATE */", "}"),
                              new LangJava());

    Assertions.assertEquals("import java.util.Stack;\n", e.imports());
    Assertions.assertEquals("public class Bar { $body } ", e.template());
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

  /** DEPENDENCY sections are exposed separately and removed from the head, but kept in the correction. */
  @Test public void testJavaDependencies()
  {
    TemplatedEntity e = parse(lines("/* BEGIN DEPENDENCY */", "class Helper {}", "/* END DEPENDENCY */", "public class FooEntity {", "  /* BEGIN TEMPLATE */",
                                    "  int a;", "  /* END TEMPLATE */", "}"),
                              new LangJava());

    Assertions.assertEquals("class Helper {}\n", e.dependencies());
    Assertions.assertEquals("public class Bar { $body } ", e.template());
    Assertions.assertEquals("int a;\n", e.initialContent());
    Assertions.assertTrue(e.correction().contains("class Helper {}"));
  }

  @Test public void testUnclosedDependency()
  {
    Assertions.assertThrows(RuntimeException.class, () -> parse(lines("/* BEGIN DEPENDENCY */", "class Helper {}"), new LangJava()));
  }

  @Test public void testImportInsideDependency()
  {
    Assertions.assertThrows(RuntimeException.class,
                            () -> parse(lines("/* BEGIN DEPENDENCY */", "/* BEGIN IMPORT */", "/* END IMPORT */", "/* END DEPENDENCY */"), new LangJava()));
  }
}
