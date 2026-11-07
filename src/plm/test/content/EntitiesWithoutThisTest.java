package plm.test.content;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The Java and Scala entity files of the exercises call their methods and fields without "this.": their code runs in a class or
 * object that does not inherit the primitives (it imports them), so "this.forward()" would not compile
 */
public class EntitiesWithoutThisTest {
  private static final Pattern THIS = Pattern.compile("\\bthis\\.\\w");

  /** The entity files of the exercises, which hold the code given to the students */
  static List<Path> entities() throws IOException
  {
    List<Path> sources;
    try (Stream<Path> files = Files.walk(Path.of("src", "lessons"))) {
      sources = files.filter(file -> file.toString().endsWith(".java") || file.toString().endsWith(".scala")).toList();
    }
    List<Path> entities = new ArrayList<>();
    for (Path source : sources)
      if (Files.readString(source).contains("BEGIN TEMPLATE"))
        entities.add(source);
    return entities;
  }

  @ParameterizedTest @MethodSource("entities") public void holdsNoThis(Path entity) throws IOException
  {
    assertFalse(THIS.matcher(Files.readString(entity)).find(), () -> entity + " uses \"this.\"");
  }
}
