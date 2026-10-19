package plm.core.utils;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xnap.commons.i18n.I18n;
import plm.core.model.Game;

public class ColorMapper {
  static String[] choices  = {"white", "black", "blue", "cyan", "darkGray", "gray", "green", "lightGray", "magenta", "orange", "pink", "red", "yellow"};
  static Color[] colors    = {Color.white,     Color.black,   Color.blue,   Color.cyan, Color.darkGray, Color.gray,  Color.green,
                              Color.lightGray, Color.magenta, Color.orange, Color.pink, Color.red,      Color.yellow};
  /** The names shown to the students, in the order of colors */
  private static final String[] translatedNames = {I18n.marktr("white"),     I18n.marktr("black"),  I18n.marktr("blue"),  I18n.marktr("cyan"),
                                                   I18n.marktr("dark grey"), I18n.marktr("grey"),   I18n.marktr("green"), I18n.marktr("light grey"),
                                                   I18n.marktr("magenta"),   I18n.marktr("orange"), I18n.marktr("pink"),  I18n.marktr("red"),
                                                   I18n.marktr("yellow")};
  static Pattern colorName = Pattern.compile("(\\d+)/(\\d+)/(\\d+)");

  public static Color name2color(String name) throws InvalidColorNameException
  {
    for (int i = 0; i < choices.length; i++)
      if (choices[i].equalsIgnoreCase(name))
        return colors[i];
    Matcher m = colorName.matcher(name);
    if (m.matches()) {
      try {
        int r = Integer.parseInt(m.group(1));
        int g = Integer.parseInt(m.group(2));
        int b = Integer.parseInt(m.group(3));

        if (r < 0 || r > 255)
          throw new InvalidColorNameException("Name " + name + " is not a valid color name: Red value is not between 0 and 255");
        if (g < 0 || g > 255)
          throw new InvalidColorNameException("Name " + name + " is not a valid color name: Green value is not between 0 and 255");
        if (b < 0 || b > 255)
          throw new InvalidColorNameException("Name " + name + " is not a valid color name: Blue value is not between 0 and 255");

        return new Color(r, g, b);
      } catch (NumberFormatException nfe) {
        throw new InvalidColorNameException("Name " + name + " is not a valid color name since one of its component is not a number", nfe);
      }
    } else {
      throw new InvalidColorNameException("Name " + name + " is not a valid color name");
    }
  }

  public static String color2name(Color c)
  {
    for (int i = 0; i < colors.length; i++)
      if (c.equals(colors[i]))
        return choices[i];
    return c.getRed() + "/" + c.getGreen() + "/" + c.getBlue();
  }

  public static int color2int(Color c)
  {
    for (int i = 0; i < choices.length; i++)
      if (colors[i].equals(c))
        return i;
    return -1;
  }

  public static String color2translated(Color c)
  {
    int rank = color2int(c);
    return rank < 0 ? c.toString() : Game.i18n.tr(translatedNames[rank]);
  }

  public static Color int2color(int c) throws InvalidColorNameException
  {
    if (c >= 0 && c < colors.length) {
      return colors[c];
    } else {
      throw new InvalidColorNameException("Number " + c + " is not a valid color number");
    }
  }
}
