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
}
