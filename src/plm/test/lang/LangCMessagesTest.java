package plm.test.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import plm.core.lang.LangC;

/** The compiler messages show the real names of the functions that the header of a universe renames with macros */
public class LangCMessagesTest {
  private static final String HEADER = "ssize_t plm_send(int fd, const void* buf, size_t len, int flags);\n#define send plm_send\n";

  @Test public void realNamesKeepTheMarksOfGccAligned()
  {
    String gcc      = String.join("\n", "TcpFramingIrc.c:9:27: error: too few arguments to function 'plm_send'", //
                                  "    9 |   int res = send(p, todo);",                                          //
                                  "      |             ^~~~",                                                    //
                                  "RemotePosix.h:33:9: note: declared here",                                     //
                                  "   33 | ssize_t plm_send(int fd, const void* buf, size_t len, int flags);",   //
                                  "      |         ^~~~~~~~",                                                    //
                                  "RemotePosix.h:33:22: note: expected 'int' but argument is of type 'char *'",
                                  "   33 | ssize_t plm_send(int fd, const void* buf, size_t len, int flags);", //
                                  "      |                  ~~~~^~",                                           //
                                  "");
    String expected = String.join("\n", "TcpFramingIrc.c:9:27: error: too few arguments to function 'send'", //
                                  "    9 |   int res = send(p, todo);",                                      //
                                  "      |             ^~~~",                                                //
                                  "RemotePosix.h:33:9: note: declared here",                                 //
                                  "   33 | ssize_t send(int fd, const void* buf, size_t len, int flags);",   //
                                  "      |         ^~~~",                                                    //
                                  "RemotePosix.h:33:22: note: expected 'int' but argument is of type 'char *'",
                                  "   33 | ssize_t send(int fd, const void* buf, size_t len, int flags);", //
                                  "      |              ~~~~^~",                                           //
                                  "");
    assertEquals(expected, LangC.withRealNames(gcc, HEADER));
  }

  @Test public void realNamesKeepTheCaretOfClang()
  {
    String clang = "RemotePosix.h:33:9: note: 'plm_send' declared here\nssize_t plm_send(int fd, const void* buf, size_t len, int flags);\n        ^\n";
    assertEquals("RemotePosix.h:33:9: note: 'send' declared here\nssize_t send(int fd, const void* buf, size_t len, int flags);\n        ^\n",
                 LangC.withRealNames(clang, HEADER));
  }

  @Test public void onlyTheRenamedFunctionsChange()
  {
    assertEquals("'plm_other' and 'send'", LangC.withRealNames("'plm_other' and 'plm_send'", HEADER));
    assertEquals("'plm_send'", LangC.withRealNames("'plm_send'", "/* no renaming */"));
  }
}
