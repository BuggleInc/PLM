package plm.test.content;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.model.lesson.MissionMarkup;

/**
 * The [!langs]...[/!] blocks of every mission text, translations included, are balanced and only name known languages, and no
 * tag is malformed. The [!thelang], [!primitives/], [!colors/] and [!protocols/] placeholders are not blocks.
 */
public class MissionMarkupTest {
  static List<Path> missions() throws IOException
  {
    try (Stream<Path> files = Files.walk(Path.of("src"))) {
      return files.filter(file -> file.toString().endsWith(".html")).toList();
    }
  }

  @ParameterizedTest @MethodSource("missions") public void markupIsWellFormed(Path mission) throws IOException
  {
    List<String> mistakes = MissionMarkup.mistakes(Files.readString(mission), true);
    assertTrue(mistakes.isEmpty(), () -> mission + ": " + String.join("; ", mistakes));
  }
}
