package plm.core.lang.primitives;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

/**
 * Writes the gettext template of the texts of the documented primitives: their documentation, their group, and their name under the
 * context "primitive", which translators may translate to give the students a localized alias. Run by the i18n-update maven
 * profile, before xgettext adds the strings of the code to the same file.
 */
public class PrimitivePot {
  public static final String NAME_CONTEXT = "primitive";

  /** A pot entry: its context (or null), its text, the comment for the translators and the classes declaring it. */
  private record Entry(String context, String text, String comment, Set<String> references) {}

  public static void main(String[] args) throws IOException
  {
    Map<String, Entry> entries = new TreeMap<>(); // keyed by context and text, so that an entry gathers all its declarations
    for (Method method : new Reflections("plm", "lessons", Scanners.MethodsAnnotated).getMethodsAnnotatedWith(Primitive.class)) {
      Primitive annotation = method.getAnnotation(Primitive.class);
      if (annotation.value().isEmpty())
        continue;
      String reference = "src/" + method.getDeclaringClass().getName().replace('.', '/') + ".java";
      String name      = method.getName();
      add(entries, null, annotation.value(), "Documentation of the primitive " + name + "()", reference);
      add(entries, null, annotation.group(), "Title of a group of primitives in the documentation of a world", reference);
      add(entries, NAME_CONTEXT, name,
          "Name of the primitive " + name + "() in the students' code. It must be a valid identifier in Java, Scala, Python and C, and must not be "
              + "the name of another primitive of the same world. Leave it untranslated if the English name fits.",
          reference);
    }

    StringBuilder pot = new StringBuilder("msgid \"\"\nmsgstr \"\"\n\"Content-Type: text/plain; charset=UTF-8\\n\"\n\"Content-Transfer-Encoding: 8bit\\n\"\n");
    for (Entry entry : entries.values()) {
      pot.append("\n#. ").append(entry.comment()).append('\n');
      entry.references().forEach(reference -> pot.append("#: ").append(reference).append('\n'));
      if (entry.context() != null)
        pot.append("msgctxt ").append(quote(entry.context())).append('\n');
      pot.append("msgid ").append(quote(entry.text())).append("\nmsgstr \"\"\n");
    }
    Files.writeString(Path.of(args[0]), pot, StandardCharsets.UTF_8);
  }

  private static void add(Map<String, Entry> entries, String context, String text, String comment, String reference)
  {
    if (!text.isEmpty())
      entries.computeIfAbsent(context + "\u0004" + text, key -> new Entry(context, text, comment, new TreeSet<>())).references().add(reference);
  }

  private static String quote(String text) { return '"' + text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + '"'; }
}
