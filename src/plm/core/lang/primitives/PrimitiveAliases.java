package plm.core.lang.primitives;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Translated names of the primitives, given by the translators in the entries of context {@link PrimitivePot#NAME_CONTEXT} of the
 * engine catalogs. The glue of every language exposes the aliases of all locales, so that a code keeps working when the student
 * switches the human language.
 */
public class PrimitiveAliases {
  public static final Path PO_DIRECTORY = Path.of("l10n/engine");

  /** Keywords of the languages, and names that the glue must not shadow. */
  private static final Set<String> KEYWORDS = Set.of(
      // Java
      "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum",
      "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package",
      "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient",
      "try", "void", "volatile", "while", "true", "false", "null", "var", "record", "yield",
      // Scala
      "def", "forSome", "implicit", "lazy", "match", "object", "override", "sealed", "trait", "type", "val", "with", "given", "then", "export", "extension",
      // Python
      "and", "as", "async", "await", "del", "elif", "except", "from", "global", "in", "is", "lambda", "nonlocal", "not", "or", "pass", "raise", "None", "True",
      "False", "print",
      // C
      "auto", "extern", "inline", "register", "restrict", "signed", "sizeof", "struct", "typedef", "union", "unsigned", "main");

  /** The aliases of each locale (named after its catalog), from primitive name to translated name. Fuzzy and untranslated names are ignored. */
  public static Map<String, Map<String, String>> load(Path poDirectory) throws IOException
  {
    Map<String, Map<String, String>> result = new TreeMap<>();
    try (Stream<Path> files = Files.list(poDirectory)) {
      for (Path po : files.filter(path -> path.toString().endsWith(".po")).sorted().toList()) {
        Map<String, String> aliases = new TreeMap<>();
        boolean fuzzy               = false;
        String context = null, id = null;
        for (String line : Files.readAllLines(po, StandardCharsets.UTF_8)) {
          if (line.startsWith("#,"))
            fuzzy = line.contains("fuzzy");
          else if (line.startsWith("msgctxt "))
            context = unquote(line.substring("msgctxt ".length()));
          else if (line.startsWith("msgid "))
            id = unquote(line.substring("msgid ".length()));
          else if (line.startsWith("msgstr ")) {
            String alias = unquote(line.substring("msgstr ".length()));
            if (PrimitivePot.NAME_CONTEXT.equals(context) && !fuzzy && !alias.isEmpty() && !alias.equals(id))
              aliases.put(id, alias);
            fuzzy   = false;
            context = null;
          }
        }
        result.put(po.getFileName().toString().replace(".po", ""), aliases);
      }
    }
    return result;
  }

  private static String unquote(String quoted) { return quoted.substring(1, quoted.length() - 1); }

  /**
   * The primitives of a glue file followed by the aliases of every locale. Fails if an alias is not a portable identifier, or if a name
   * would designate two different primitives in the glue.
   */
  public static List<PrimitiveMethod> withAliases(String remote, Map<String, PrimitiveMethod> primitives, Map<String, Map<String, String>> aliases)
  {
    List<PrimitiveMethod> result = new ArrayList<>(primitives.values());
    List<String> errors          = new ArrayList<>();
    aliases.forEach((locale, names) -> names.forEach((name, alias) -> {
      PrimitiveMethod primitive = primitives.get(name);
      if (primitive == null)
        return; // a primitive of another universe, or a stale translation
      // Letters may be accented (C needs gcc 10 or clang), but not decomposed: Python normalizes identifiers, unlike the other languages
      if (!alias.matches("[\\p{L}_][\\p{L}\\p{Nd}_]*") || KEYWORDS.contains(alias))
        errors.add(locale + ": " + name + "() cannot be translated to " + alias + ", which is not a valid identifier in every language");
      else
        result.add(primitive.alias(alias));
    }));

    Map<String, Set<String>> commandsPerName = result.stream().collect(
        Collectors.groupingBy(PrimitiveMethod::name, TreeMap::new, Collectors.mapping(PrimitiveMethod::command, Collectors.toCollection(TreeSet::new))));
    commandsPerName.forEach((name, commands) -> {
      if (commands.size() > 1)
        errors.add(name + " designates several primitives: " + commands);
    });

    if (!errors.isEmpty())
      throw new IllegalStateException("Invalid translations of the primitives of " + remote + " in " + PO_DIRECTORY + ":\n\t" + String.join("\n\t", errors));
    return result.stream().distinct().toList(); // the same alias in several locales
  }
}
