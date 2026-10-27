package plm.core.model.lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import plm.core.lang.ProgrammingLanguage;

/**
 * The pseudo-markup of the mission texts: the [!java|c]...[/!] blocks only shown for some programming languages, which can nest, and
 * the [!thelang], [!primitives/], [!colors/] and [!protocols/] placeholders.
 */
public final class MissionMarkup {
  public static final Set<String> LANGS = Set.of("java", "python", "scala", "c");

  private static final Pattern TAG         = Pattern.compile("\\[!([^\\]]*)\\]|\\[/!\\]");
  private static final String PLACEHOLDERS = "thelang/?|primitives/|colors/|protocols/";
  /** A tag left once the well-formed ones are removed: a [! or [/! without its closing bracket, or a [java|c] without its ! */
  private static final Pattern MALFORMED = Pattern.compile("\\[/?!|\\[(java|python|scala|c)(\\|(java|python|scala|c))*\\]");
  /** A [!langs]...[/!] block that contains no other block */
  private static final Pattern INNERMOST_BLOCK = Pattern.compile("(?s)\\[!([a-z|]+)\\]((?:(?!\\[![a-z|]+\\]).)*?)\\[/!\\]");

  /** Colors of the language-specific blocks, by their languages in alphabetical order */
  private static final Map<String, String> LANG_COLORS =
      Map.ofEntries(Map.entry("c", "00FF00"), Map.entry("java", "FF0000"), Map.entry("python", "008000"), Map.entry("scala", "0000FF"),
                    Map.entry("c|java", "FFFF00"), Map.entry("c|python", "00AF00"), Map.entry("c|scala", "00FFFF"), Map.entry("java|python", "FF8000"),
                    Map.entry("java|scala", "FF00FF"), Map.entry("python|scala", "0080FF"), Map.entry("c|java|python", "333333"),
                    Map.entry("c|java|scala", "EEEEEE"), Map.entry("c|python|scala", "00AFFF"), Map.entry("java|python|scala", "FF80FF"));

  private MissionMarkup() {}

  /**
   * Resolves the blocks of a mission text: a block is replaced by its content if one of its languages is shown, and removed
   * otherwise. The blocks are resolved from the innermost, so that they can be nested. [!thelang] gives the name of the language.
   *
   * @param shown the languages to show, or null to show them all
   * @param colored whether to color the content of the blocks according to their languages
   */
  public static String filter(String in, Collection<ProgrammingLanguage> shown, boolean colored)
  {
    Set<String> names = shown == null ? null : shown.stream().map(lang -> lang.getLang().toLowerCase()).collect(Collectors.toSet());
    String res        = in.replaceAll("\\[!thelang/?\\]", "[!java]Java[/!][!python]python[/!][!scala]Scala[/!][!c]C[/!]");
    String previous;
    do {
      previous = res;
      res      = INNERMOST_BLOCK.matcher(res).replaceAll(block -> {
        List<String> langs = Arrays.stream(block.group(1).split("\\|")).sorted().toList();
        if (names != null && langs.stream().noneMatch(names::contains))
          return "";
        String color = LANG_COLORS.get(String.join("|", langs));
        return Matcher.quoteReplacement(colored && color != null ? "<font color=\"" + color + "\">" + block.group(2) + "</font>" : block.group(2));
           });
    } while (!res.equals(previous));
    return res;
  }

  /** Opened minus closed blocks. A fragment of text (such as a translated paragraph) may open a block that another one closes. */
  public static int depth(String text)
  {
    int depth   = 0;
    Matcher tag = TAG.matcher(text);
    while (tag.find())
      if (tag.group(1) == null)
        depth--;
      else if (!tag.group(1).matches(PLACEHOLDERS))
        depth++;
    return depth;
  }

  /**
   * The mistakes in the markup of the text: unknown languages and malformed tags. The blocks of a whole text must also be balanced,
   * while a fragment is only compared to the original with depth(). Each mistake gives its line.
   */
  public static List<String> mistakes(String text, boolean whole)
  {
    List<String> mistakes = new ArrayList<>();
    int depth             = 0;
    Matcher tag           = TAG.matcher(text);
    while (tag.find()) {
      String line = "line " + text.substring(0, tag.start()).split("\n", -1).length + ": ";
      if (tag.group(1) == null) {
        if (--depth < 0 && whole)
          mistakes.add(line + "[/!] closes no block");
      } else if (!tag.group(1).matches(PLACEHOLDERS)) {
        depth++;
        if (!LANGS.containsAll(List.of(tag.group(1).split("\\|"))))
          mistakes.add(line + "unknown language in " + tag.group());
      }
    }
    if (whole && depth > 0)
      mistakes.add("unclosed block");
    Matcher malformed = MALFORMED.matcher(TAG.matcher(text).replaceAll(""));
    if (malformed.find())
      mistakes.add("malformed tag " + malformed.group() + ", such as [/! without its closing bracket or [java] without its !");
    return mistakes;
  }
}
