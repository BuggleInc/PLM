package plm.test.lang;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import plm.core.lang.primitives.CodeCreation;

/** The glue generated for every universe and language is identical to the one that the build packed with the PLM. */
public class GlueGenerationTest {
  private static final Path BUILT_GLUE = Path.of("target/classes/resources/langages");

  @Test public void generationIsReproducible(@TempDir Path dir) throws IOException
  {
    CodeCreation.generateGlue(dir.toFile());

    List<Path> generated;
    try (Stream<Path> files = Files.walk(dir)) {
      generated = files.filter(Files::isRegularFile).toList();
    }
    Assertions.assertFalse(generated.isEmpty(), "No glue was generated");
    for (Path file : generated) {
      Path relative = dir.relativize(file);
      Assertions.assertEquals(Files.readString(BUILT_GLUE.resolve(relative)), Files.readString(file),
                              "The generated " + relative + " differs from the one of the build");
    }
  }
}
