package plm.core.lang;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.swing.ImageIcon;
import plm.core.model.lesson.Exercise;

/**
 * Ancestor of the all programming languages, in charge of generating student code by injecting extracted pieces of the
 * student/correction source (the run() method, its imports...) into a language-specific template,
 * then to compile and run as an external process.
 */
public abstract class TemplatedRemoteLang extends RemoteExecutionLang {

  public TemplatedRemoteLang(String lang, String ext, ImageIcon i) { super(lang, ext, i); }

  /**
   * Read a classloader resource at {@code path} (relative to the classpath root) as a UTF-8 string. Low-level
   * primitive behind {@link #loadRemoteFile} and LangC's own resource reading.
   */
  protected static String readClasspathResource(String path) throws IOException
  {
    try (InputStream in = TemplatedRemoteLang.class.getClassLoader().getResourceAsStream(path)) {
      if (in == null)
        throw new IOException("Resource '" + path + "' does not exist.");
      return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  /**
   * Load the raw content of a "RemoteXxx" universe-glue file (e.g. RemoteBuggle.java/.scala/.py), shipped as a
   * classloader resource under "resources/langages/&lt;langDir&gt;/". {@code remoteName} is normalized the same way in
   * every caller: null/empty defaults to plain "Remote", "Remote" is prepended if missing, and {@code extension} is
   * appended if missing. Package-declaration handling (Java/Scala only) is left to the caller, since Python has none.
   */
  protected static String loadRemoteFile(String remoteName, String langDir, String extension)
  {
    String remote = (remoteName == null || remoteName.isEmpty()) ? "Remote" + extension : remoteName;
    if (!remote.startsWith("Remote"))
      remote = "Remote" + remote;
    if (!remote.endsWith(extension))
      remote = remote + extension;

    String path = "resources/langages/" + langDir + "/" + remote;
    try {
      return readClasspathResource(path);
    } catch (IOException e) {
      throw new IllegalArgumentException("Remote '" + path + "' do not exist (argument passed: '" + remoteName + "').");
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
