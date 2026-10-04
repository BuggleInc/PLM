package plm.core.utils;

/** Helpers to deal with the indentation of source code. */
public class Indentation {

  private Indentation() {} // not instantiable, only static helpers

  /**
   * Replaces the tabs found in the leading whitespace of every line by spaces, using tab stops every 8 columns as python does. The
   * indentation levels are the ones python sees, and tabs and spaces cannot be mixed up anymore. The number of lines is unchanged.
   */
  public static String expandLeadingTabs(String code)
  {
    String[] lines   = code.split("\n", -1);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < lines.length; i++) {
      if (i > 0)
        sb.append('\n');
      String line = lines[i];
      int end     = 0;
      int column  = 0;
      while (end < line.length() && (line.charAt(end) == ' ' || line.charAt(end) == '\t')) {
        column = line.charAt(end) == ' ' ? column + 1 : (column / 8 + 1) * 8;
        end++;
      }
      sb.append(" ".repeat(column)).append(line.substring(end));
    }
    return sb.toString();
  }
}
