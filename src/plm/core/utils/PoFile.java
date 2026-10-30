package plm.core.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the entries of a gettext catalog (.po or .pot). Obsolete entries (#~) are skipped, and the strings are kept escaped. The
 * comments extracted from the code for the translators (#.) are kept, one per line.
 */
public final class PoFile {
  /** An entry; context is null when absent, and translation is empty when untranslated. Plural forms are not read. */
  public record Entry(String context, String id, String translation, boolean fuzzy, String comments) {}

  private PoFile() {}

  public static List<Entry> read(Path po) throws IOException
  {
    List<Entry> entries = new ArrayList<>();
    String context = null, id = null, translation = "", field = null, comments = "";
    boolean fuzzy = false;
    for (String line : Files.readAllLines(po, StandardCharsets.UTF_8)) {
      if (line.isBlank()) { // end of entry. Flags of obsolete entries are reset here too, so that they don't leak to the next entry
        if (id != null)
          entries.add(new Entry(context, id, translation, fuzzy, comments));
        context = id = field = null;
        translation = comments = "";
        fuzzy                = false;
      } else if (line.startsWith("#,"))
        fuzzy = line.contains("fuzzy");
      else if (line.startsWith("#. "))
        comments += (comments.isEmpty() ? "" : "\n") + line.substring(3);
      else if (line.startsWith("\"")) { // continuation of the current field
        String value = line.substring(1, line.length() - 1);
        if ("msgctxt".equals(field))
          context += value;
        else if ("msgid".equals(field))
          id += value;
        else if ("msgstr".equals(field))
          translation += value;
      } else if (!line.startsWith("#")) {
        int space    = line.indexOf(' ');
        field        = line.substring(0, space);
        String value = line.substring(space + 2, line.length() - 1);
        switch (field) {
          case "msgctxt" -> context = value;
          case "msgid" -> id = value;
          case "msgstr" -> translation = value;
          default -> {
          } // msgid_plural, msgstr[n]
        }
      }
    }
    if (id != null)
      entries.add(new Entry(context, id, translation, fuzzy, comments));
    return entries;
  }
}
