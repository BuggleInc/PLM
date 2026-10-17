package plm.test.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The [!langs]...[/!] blocks of every mission text, translations included, are balanced and only name known languages, and no
 * tag is malformed. The [!thelang] and [!primitives/] placeholders are not blocks.
 */
public class MissionMarkupTest {
  static final Pattern TAG       = Pattern.compile("\\[!([^\\]]*)\\]|\\[/!\\]");
  static final Set<String> LANGS = Set.of("java", "python", "scala", "c");

  /** Whether a [! or [/! is left once the well-formed tags are removed, or a tag lacks its ! (such as [java|c]). */
  static boolean isMalformed(String text)
  {
    return TAG.matcher(text).replaceAll("").matches("(?s).*(\\[/?!|\\[(java|python|scala|c)(\\|(java|python|scala|c))*\\]).*");
  }

  static List<Path> missions() throws IOException
  {
    try (Stream<Path> files = Files.walk(Path.of("src"))) {
      return files.filter(file -> file.toString().endsWith(".html")).toList();
    }
  }

  @ParameterizedTest @MethodSource("missions") public void markupIsWellFormed(Path mission) throws IOException
  {
    String text = Files.readString(mission).replaceAll("\\[!(thelang/?|primitives/)\\]", "");
    int depth   = 0;
    Matcher tag = TAG.matcher(text);
    while (tag.find()) {
      int at                 = tag.start();
      Supplier<String> where = () -> mission + ":" + text.substring(0, at).split("\n", -1).length;
      if (tag.group(1) == null) {
        depth--;
        assertTrue(depth >= 0, () -> where.get() + ": [/!] closes no block");
      } else {
        String langs = tag.group(1);
        assertTrue(LANGS.containsAll(List.of(langs.split("\\|"))), () -> where.get() + ": unknown language in [!" + langs + "]");
        depth++;
      }
    }
    assertEquals(0, depth, mission + ": unclosed block");
    assertFalse(isMalformed(text), mission + ": malformed tag, such as [/! without its closing bracket or [java] without its !");
  }
}
