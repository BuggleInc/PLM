package plm.test.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.utils.Indentation;

public class IndentationTest {

  @Test public void testExpandLeadingTabs()
  {
    Assertions.assertEquals("        a\n    b\n        c\n", Indentation.expandLeadingTabs("\ta\n    b\n    \tc\n"));
    Assertions.assertEquals("\n x\ty\n", Indentation.expandLeadingTabs("\n x\ty\n")); // only the leading whitespace is touched
  }

  @Test public void testMinLeadingSpaces()
  {
    Assertions.assertEquals(2, Indentation.minLeadingSpaces("    a\n  b\n      c\n"));
    Assertions.assertEquals(4, Indentation.minLeadingSpaces("    a\n\n  \n      c\n")); // blank lines do not count
    Assertions.assertEquals(0, Indentation.minLeadingSpaces("\n   \n"));
  }

  @Test public void testReindent()
  {
    Assertions.assertEquals("  a\n\n    b\n", Indentation.reindent("    a\n\n      b\n", 4, 2));
    Assertions.assertEquals("a", Indentation.reindent("  a", 2, 0));                   // no final newline is added
    Assertions.assertEquals("a\n   \n\n", Indentation.reindent("  a\n   \n\n", 2, 0)); // blank lines are kept as they are
  }
}
