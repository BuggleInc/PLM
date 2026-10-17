package plm.test.content;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.lang.primitives.CodeCreation;
import plm.core.lang.primitives.PrimitiveAliases;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveRegistration;
import plm.core.utils.PoFile;

/**
 * The work of the translators does not break the PLM. The primitive names of l10n/engine/*.po can be added to the glue of every
 * universe. Every translation of the mission texts (l10n/missions/*.po) keeps the [!langs]...[/!] blocks of the English text with
 * known languages only, and calls each primitive of the English text under its English name or its alias in that language. Fuzzy,
 * untranslated and outdated entries (absent from the pot) are not checked.
 */
public class TranslationsTest {
  private static final Path MISSIONS = Path.of("l10n/missions");

  static List<Path> catalogs() throws IOException
  {
    try (Stream<Path> files = Files.list(MISSIONS)) {
      return files.filter(file -> file.toString().endsWith(".po")).sorted().toList();
    }
  }

  @Test public void primitiveNamesAreValid() throws IOException
  {
    Map<String, Map<String, String>> aliases = PrimitiveAliases.load(PrimitiveAliases.PO_DIRECTORY);
    for (CodeCreation.Remote remote : CodeCreation.REMOTES)
      assertDoesNotThrow(() -> PrimitiveAliases.withAliases(remote.name(), PrimitiveRegistration.getMaximalPrimitiveForEntity(remote.entity()), aliases));
  }

  @ParameterizedTest @MethodSource("catalogs") public void translationsKeepMarkupAndPrimitives(Path catalog) throws IOException
  {
    String locale       = catalog.getFileName().toString().replace(".po", "");
    Set<String> current = new TreeSet<>();
    PoFile.read(MISSIONS.resolve("plm.pot")).forEach(entry -> current.add(entry.id()));
    Map<String, String> aliases = PrimitiveAliases.load(PrimitiveAliases.PO_DIRECTORY).getOrDefault(locale, Map.of());
    Set<String> primitives      = new TreeSet<>();
    for (CodeCreation.Remote remote : CodeCreation.REMOTES)
      for (PrimitiveMethod primitive : PrimitiveRegistration.getMaximalPrimitiveForEntity(remote.entity()).values())
        if (!primitive.doc().isEmpty())
          primitives.add(primitive.name());

    List<String> errors = new ArrayList<>();
    for (PoFile.Entry entry : PoFile.read(catalog)) {
      if (entry.fuzzy() || entry.translation().isEmpty() || !current.contains(entry.id()))
        continue;
      String original = entry.id().replace("\\n", "\n"), translation = entry.translation().replace("\\n", "\n");
      String where = "\n  " + abbreviate(entry.id()) + "\n  -> " + abbreviate(entry.translation());

      Matcher tag = MissionMarkupTest.TAG.matcher(translation);
      while (tag.find())
        if (tag.group(1) != null && !tag.group(1).matches("thelang/?|primitives/") && !MissionMarkupTest.LANGS.containsAll(List.of(tag.group(1).split("\\|"))))
          errors.add("unknown language in " + tag.group() + where);
      if (MissionMarkupTest.isMalformed(translation))
        errors.add("malformed tag, such as [/! without its closing bracket or [java] without its !" + where);
      if (depth(original) != depth(translation))
        errors.add("unbalanced [!langs]...[/!] blocks" + where);

      for (String primitive : primitives)
        if (calls(original, primitive) && !calls(translation, primitive) && !(aliases.containsKey(primitive) && calls(translation, aliases.get(primitive))))
          errors.add(primitive + "() is missing, or called under another name than " + aliases.getOrDefault(primitive, primitive) + "()" + where);
    }
    assertTrue(errors.isEmpty(), () -> catalog + ": " + errors.size() + " problems:\n" + String.join("\n", errors));
  }

  /** Opened minus closed [!langs] blocks: a block may span several entries, as long as the translation does the same. */
  private static int depth(String text)
  {
    int depth   = 0;
    Matcher tag = MissionMarkupTest.TAG.matcher(text);
    while (tag.find())
      if (tag.group(1) == null)
        depth--;
      else if (!tag.group(1).matches("thelang/?|primitives/"))
        depth++;
    return depth;
  }

  private static boolean calls(String text, String name)
  {
    return Pattern.compile("(?<![\\w.])" + Pattern.quote(name) + "\\(", Pattern.UNICODE_CHARACTER_CLASS).matcher(text).find();
  }

  private static String abbreviate(String text) { return text.length() <= 120 ? text : text.substring(0, 120) + "..."; }
}
