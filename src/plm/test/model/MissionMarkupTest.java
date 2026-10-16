package plm.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

/** The [!langs]...[/!] blocks of every mission text, translations included, are balanced and only name known languages. */
public class MissionMarkupTest {
  private static final Pattern TAG       = Pattern.compile("\\[!([^\\]]*)\\]|\\[/!\\]");
  private static final Set<String> LANGS = Set.of("java", "python", "scala", "c");

  static List<Path> missions() throws IOException
  {
    try (Stream<Path> files = Files.walk(Path.of("src"))) {
      return files.filter(file -> file.toString().endsWith(".html")).toList();
    }
  }

  @ParameterizedTest @MethodSource("missions") public void markupIsWellFormed(Path mission) throws IOException
  {
    String text = Files.readString(mission).replaceAll("\\[!thelang/?\\]", "");
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
  }
}
