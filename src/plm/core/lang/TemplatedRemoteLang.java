package plm.core.lang;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;

/**
 * Ancestor of the all programming languages, in charge of generating student code by injecting extracted pieces of the
 * student/correction source (the run() method, its imports...) into a language-specific template,
 * then to compile and run as an external process.
 */
public abstract class TemplatedRemoteLang extends RemoteExecutionLang {

  /** For each compiled executable, the number of lines to subtract from the locations of the entity in the diagnostics: 0 for the correction. */
  protected final Map<String, Integer> lineShifts = new ConcurrentHashMap<>();

  public TemplatedRemoteLang(String lang, String ext, ImageIcon i) { super(lang, ext, i); }

  /**
   * How many lines of the generated source come before the body's own first line, so that the line number of a compiler
   * error or of a stack trace can be translated back into the student's own editor coordinates.
   */
  protected static int countLinesBeforeBody(String pre) { return (int)pre.chars().filter(c -> c == '\n').count(); }

  /**
   * Translates the line numbers matched by {@code location} in {@code text} back into the editor's, by subtracting the shift of the
   * executable. The group 1 of the pattern is the text before the number, and the group 2 is the number. Locations before the body,
   * and all of them when debugging is enabled, are left untouched.
   */
  protected String shiftLines(Pattern location, String text, String executable)
  {
    int shift = lineShifts.getOrDefault(executable, 0);
    if (shift == 0 || Game.getInstance().isDebugEnabled())
      return text;
    return location.matcher(text).replaceAll(m -> {
      int lineNumber = Integer.parseInt(m.group(2));
      return m.group(1) + (lineNumber > shift ? lineNumber - shift : lineNumber);
    });
  }

  /**
   * Read a classloader resource at {@code path} (relative to the classpath root) as bytes.
   */
  protected static byte[] readClasspathBytes(String path) throws IOException
  {
    try (InputStream in = TemplatedRemoteLang.class.getClassLoader().getResourceAsStream(path)) {
      if (in == null)
        throw new IOException("Resource '" + path + "' does not exist.");
      return in.readAllBytes();
    }
  }

  /**
   * Copies the classloader resource "resources/langages/&lt;resource&gt;" to targetDir, under its own file name, unless an identical
   * file is already there, and returns its path. The copy goes through a temporary file moved atomically, as several PLM instances
   * may deploy it at once.
   */
  protected static synchronized Path deployResource(String resource, Path targetDir)
  {
    try {
      byte[] content = readClasspathBytes("resources/langages/" + resource);
      Path target    = targetDir.resolve(resource.substring(resource.lastIndexOf('/') + 1));
      if (!Files.exists(target) || !Arrays.equals(Files.readAllBytes(target), content)) {
        Files.createDirectories(targetDir);
        Path tmp = Files.createTempFile(targetDir, target.getFileName().toString(), ".tmp");
        Files.write(tmp, content);
        Files.move(tmp, target, StandardCopyOption.ATOMIC_MOVE);
      }
      return target;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Unique-enough package name for one compile, derived from the exercise id and which side gets compiled
   * (STUDENT/CORRECTION) -- instead of a shared, manually-incremented counter (the old packageNameSuffix), which
   * raced across concurrent compiles whenever this (singleton) language instance served two threads at once: one
   * thread could read the workspace directory name right as another thread bumped the counter again, so the jar
   * ended up in a directory named after one value but with a manifest naming another.
   *
   * This name is stable across compiles of the same exercise and the same side: reusing it is normally harmless,
   * since nothing changed. The one case it does not cover is an entity file edited on disk without restarting PLM
   * (there is no live-reload of entities otherwise, so this is a developer-only corner case): the old id-plus-a-
   * hash-of-the-source scheme picked up such changes automatically by landing in a fresh directory; this one does
   * not, so pick "clean workspace" (or restart PLM) after editing an entity file, or delete the stale directory by
   * hand under the language's own tempFolder.
   */
  protected String packageNameForExercise(Exercise exo, Exercise.StudentOrCorrection whatToCompile)
  {
    String id = exo.getId().replaceAll("[^a-zA-Z0-9]", "_");
    return packageNamePrefix + id + "_" + whatToCompile;
  }

  protected static final String packageNamePrefix = "plm.runtime";
}
