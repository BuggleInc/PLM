package plm.core.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A message of a compiler (or a refusal of Exercise.verifySource()), with the notes that follow it but not the code that it
 * quotes. Its line is the one of the editor when it surely designates one; otherwise, it is the first line, and not sure.
 */
public record CompilerMessage(int line, boolean sure, boolean warning, String text) {

  /** The first line of a message, as "file:line:column: kind: text", whose location (or column) may be missing */
  private static final Pattern START = Pattern.compile("(?:(?<file>.+?):(?<line>\\d+):(?:\\d+:)? )?(?<kind>fatal error|error|warning|note|info): (?<text>.*)");
  /** The lines of no message: the code quoted by the compiler and its marks (after a |), and the context lines of gcc and of its linker */
  private static final Pattern NOISE =
      Pattern.compile("\\s*\\d*\\s*\\|.*|In file included from .*|\\s+from .*|.*: (?:[Ii]n (?:member )?function|At top level).*");

  /**
   * The messages of a compiler. Each one starts with a line matching START, whose kind is "error" (or "fatal error") for the
   * errors and anything else for the warnings, and goes on with the indented lines that follow it. A note is added to the previous
   * message, if any. Any other line starts an error of its own. A message is sure of its line if it is located in editorFile and
   * within its editorLines lines.
   */
  public static List<CompilerMessage> parse(String text, String editorFile, int editorLines)
  {
    List<CompilerMessage> messages = new ArrayList<>();
    for (String line : text.split("\\R")) {
      if (line.isBlank() || NOISE.matcher(line).matches())
        continue;
      Matcher start = START.matcher(line);
      boolean found = start.matches();
      boolean note  = found && start.group("kind").equals("note");
      if (!messages.isEmpty() && (note || !found && Character.isWhitespace(line.charAt(0)))) {
        CompilerMessage last = messages.get(messages.size() - 1);
        String more          = note ? "note: " + start.group("text") : line;
        messages.set(messages.size() - 1, new CompilerMessage(last.line, last.sure, last.warning, last.text + "\n" + more.strip()));
      } else if (found) {
        int number   = start.group("line") == null ? 0 : Integer.parseInt(start.group("line"));
        boolean sure = editorFile.equals(start.group("file")) && number >= 1 && number <= editorLines;
        messages.add(new CompilerMessage(sure ? number : 1, sure, !start.group("kind").endsWith("error"), start.group("text").strip()));
      } else {
        messages.add(new CompilerMessage(1, false, false, line.strip()));
      }
    }
    return messages;
  }
}
