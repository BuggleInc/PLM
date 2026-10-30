package plm.test.content;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.lang.primitives.CodeCreation;
import plm.core.lang.primitives.PrimitiveAliases;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveRegistration;
import plm.core.model.lesson.MissionMarkup;
import plm.core.utils.PoFile;

/**
 * The work of the translators does not break the PLM. The primitive names of l10n/engine/*.po can be added to the glue of every
 * universe. Every translation of the mission texts (l10n/missions/*.po) keeps the [!langs]...[/!] blocks of the English text with
 * known languages only, and calls each primitive of the English text under its English name or its alias in that language. Fuzzy,
 * untranslated and outdated entries (absent from the pot) are not checked. The translations of the engine keep the texts that the
 * comments for the translators ask to keep, and the English messages with arguments double their apostrophes.
 */
public class TranslationsTest {
  private static final Path MISSIONS = Path.of("l10n/missions");

  static List<Path> catalogs() throws IOException { return poFiles(MISSIONS); }

  static List<Path> engineCatalogs() throws IOException { return poFiles(PrimitiveAliases.PO_DIRECTORY); }

  private static List<Path> poFiles(Path directory) throws IOException
  {
    try (Stream<Path> files = Files.list(directory)) {
      return files.filter(file -> file.toString().endsWith(".po")).sorted().toList();
    }
  }

  /** The translated messages of the engine are valid MessageFormat patterns that use the same arguments as the English text. */
  @ParameterizedTest @MethodSource("engineCatalogs") public void messageFormatsAreKept(Path catalog) throws IOException
  {
    List<String> errors = new ArrayList<>();
    for (PoFile.Entry entry : PoFile.read(catalog))
      if (!entry.fuzzy() && !entry.translation().isEmpty() && entry.id().matches("(?s).*\\{\\d.*")) {
        Set<Integer> expected = arguments(entry.id()), found = arguments(entry.translation());
        if (!expected.equals(found))
          errors.add("arguments " + expected + " expected, but " + (found == null ? "invalid pattern" : found) + "\n  " + entry.id() + "\n  -> " +
                     entry.translation());
      }
    assertTrue(errors.isEmpty(), () -> catalog + ": " + errors.size() + " problems:\n" + String.join("\n", errors));
  }

  /**
   * A text that a comment for the translators asks to keep unchanged, as the URL that the programs of the students extract in
   * TcpFramingIrc: // Translator: please keep this string unchanged (without the quotes): "<xmpp:robots@conference.buggleinc.lan>"
   */
  private static final Pattern KEPT = Pattern.compile("keep this string unchanged \\(without the quotes\\): \"([^\"]+)\"", Pattern.CASE_INSENSITIVE);

  /** The texts to keep in the translation of each English message of the engine that has some, as its comments ask */
  private static Map<String, List<String>> keptTexts() throws IOException
  {
    Map<String, List<String>> res = new TreeMap<>();
    for (PoFile.Entry entry : PoFile.read(PrimitiveAliases.PO_DIRECTORY.resolve("plm.pot"))) {
      List<String> kept = KEPT.matcher(entry.comments()).results().map(match -> match.group(1)).toList();
      if (!kept.isEmpty())
        res.put(entry.id(), kept);
    }
    return res;
  }

  /** The texts to keep are in their English message, so that their comments are not outdated */
  @Test public void keptTextsAreInTheirMessage() throws IOException
  {
    List<String> errors = new ArrayList<>();
    keptTexts().forEach((id, kept) -> kept.stream().filter(text -> !id.contains(text)).forEach(text -> errors.add(text + " is not in " + id)));
    assertTrue(errors.isEmpty(), () -> String.join("\n", errors));
  }

  /** The translated messages of the engine keep the texts that their comments ask to keep */
  @ParameterizedTest @MethodSource("engineCatalogs") public void keptTextsAreKept(Path catalog) throws IOException
  {
    Map<String, List<String>> kept = keptTexts();
    List<String> errors            = new ArrayList<>();
    for (PoFile.Entry entry : PoFile.read(catalog))
      if (!entry.fuzzy() && !entry.translation().isEmpty())
        for (String text : kept.getOrDefault(entry.id(), List.of()))
          if (!entry.translation().contains(text))
            errors.add(text + " must be kept\n  " + entry.id() + "\n  -> " + entry.translation());
    assertTrue(errors.isEmpty(), () -> catalog + ": " + errors.size() + " problems:\n" + String.join("\n", errors));
  }

  /**
   * The English messages of the engine taking arguments are MessageFormat patterns, whose single apostrophes would quote the rest
   * of the text (removing the apostrophe, and showing the arguments as {0}): they must be doubled.
   */
  @Test public void englishMessageFormatsDoubleTheirApostrophes() throws IOException
  {
    List<String> errors = new ArrayList<>();
    for (PoFile.Entry entry : PoFile.read(PrimitiveAliases.PO_DIRECTORY.resolve("plm.pot")))
      if (entry.id().matches("(?s).*\\{\\d.*") && entry.id().replace("''", "").contains("'"))
        errors.add(entry.id());
    assertTrue(errors.isEmpty(), () -> "single apostrophes in patterns:\n" + String.join("\n", errors));
  }

  /** The arguments that the MessageFormat pattern actually substitutes, or null if it is invalid */
  private static Set<Integer> arguments(String escaped)
  {
    Object[] markers = new Object[10];
    for (int i = 0; i < markers.length; i++)
      markers[i] = "\u0001" + i + "\u0001";
    try {
      String formatted  = new MessageFormat(escaped.replace("\\n", "\n").replace("\\\"", "\"")).format(markers);
      Set<Integer> used = new TreeSet<>();
      for (int i = 0; i < markers.length; i++)
        if (formatted.contains((String)markers[i]))
          used.add(i);
      return used;
    } catch (IllegalArgumentException e) {
      return null;
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

      MissionMarkup.mistakes(translation, false).forEach(mistake -> errors.add(mistake + where));
      if (MissionMarkup.depth(original) != MissionMarkup.depth(translation))
        errors.add("unbalanced [!langs]...[/!] blocks" + where);

      for (String primitive : primitives)
        if (calls(original, primitive) && !calls(translation, primitive) && !(aliases.containsKey(primitive) && calls(translation, aliases.get(primitive))))
          errors.add(primitive + "() is missing, or called under another name than " + aliases.getOrDefault(primitive, primitive) + "()" + where);
    }
    assertTrue(errors.isEmpty(), () -> catalog + ": " + errors.size() + " problems:\n" + String.join("\n", errors));
  }

  private static boolean calls(String text, String name)
  {
    return Pattern.compile("(?<![\\w.])" + Pattern.quote(name) + "\\(", Pattern.UNICODE_CHARACTER_CLASS).matcher(text).find();
  }

  private static String abbreviate(String text) { return text.length() <= 120 ? text : text.substring(0, 120) + "..."; }
}
