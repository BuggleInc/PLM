package plm.core.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A message of a compiler (or a refusal of Exercise.verifySource()), with the notes that follow it but not the code that it
 * quotes. Its line and its column (in characters) count from 1 in the editor, and are 0 when unknown: the line when the message does
 * not surely designate a line of the editor, and the column then too, or when the compiler gives none.
 */
public record CompilerMessage(int line, int column, boolean warning, String text) {

  /** The first line of a message, as "file:line:column: kind: text", whose location (or column) may be missing */
  private static final Pattern START =
      Pattern.compile("(?:(?<file>.+?):(?<line>\\d+):(?:(?<column>\\d+):)? )?(?<kind>fatal error|error|warning|note|info): (?<text>.*)");
  /** The lines of no message: the code quoted by the compiler and its marks (after a |), and the context lines of gcc and of its linker */
  private static final Pattern NOISE =
      Pattern.compile("\\s*\\d*\\s*\\|.*|In file included from .*|\\s+from .*|.*: (?:[Ii]n (?:member )?function|At top level).*");

  /**
   * The messages of a compiler. Each one starts with a line matching START, whose kind is "error" (or "fatal error") for the
   * errors and anything else for the warnings, and goes on with the indented lines that follow it. A note is added to the previous
   * message, if any. Any other line starts an error of its own. The code of the editor starts after the lineShift first lines of
   * editorFile: a message is sure of its line if it is located there, within the editorLines lines of the editor.
   */
  public static List<CompilerMessage> parse(String text, String editorFile, int lineShift, int editorLines)
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
        messages.set(messages.size() - 1, new CompilerMessage(last.line, last.column, last.warning, last.text + "\n" + more.strip()));
      } else if (found) {
        int number   = start.group("line") == null ? 0 : Integer.parseInt(start.group("line")) - lineShift;
        boolean sure = editorFile.equals(start.group("file")) && number >= 1 && number <= editorLines;
        int column   = sure && start.group("column") != null ? Integer.parseInt(start.group("column")) : 0;
        messages.add(new CompilerMessage(sure ? number : 0, column, !start.group("kind").endsWith("error"), start.group("text").strip()));
      } else {
        messages.add(new CompilerMessage(0, 0, false, line.strip()));
      }
    }
    return messages;
  }
}
