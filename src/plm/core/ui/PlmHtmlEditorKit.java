package plm.core.ui;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.Position;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Lecture;

public class PlmHtmlEditorKit extends HTMLEditorKit {
  private static final long serialVersionUID = 1L;

  /** Colors of the language-specific blocks, by their languages in alphabetical order */
  private static final Map<String, String> LANG_COLORS =
      Map.ofEntries(Map.entry("c", "00FF00"), Map.entry("java", "FF0000"), Map.entry("python", "008000"), Map.entry("scala", "0000FF"),
                    Map.entry("c|java", "FFFF00"), Map.entry("c|python", "00AF00"), Map.entry("c|scala", "00FFFF"), Map.entry("java|python", "FF8000"),
                    Map.entry("java|scala", "FF00FF"), Map.entry("python|scala", "0080FF"), Map.entry("c|java|python", "333333"),
                    Map.entry("c|java|scala", "EEEEEE"), Map.entry("c|python|scala", "00AFFF"), Map.entry("java|python|scala", "FF80FF"));

  /** A [!langs]...[/!] block that contains no other block */
  private static final Pattern INNERMOST_BLOCK = Pattern.compile("(?s)\\[!([a-z|]+)\\]((?:(?!\\[![a-z|]+\\]).)*?)\\[/!\\]");

  /**
   * Keeps the parts of the mission text meant for the current programming language, or every part with a color per language in
   * debug mode.
   */
  public static String filterHTML(String in, boolean debug)
  {
    return debug ? filterHTML(in, null, true) : filterHTML(in, List.of(Game.getInstance().getProgrammingLanguage()), false);
  }

  /**
   * Resolves the [!langs]...[/!] blocks of a mission text: a block is replaced by its content if one of its languages is shown, and
   * removed otherwise. The blocks are resolved from the innermost, so that they can be nested. [!thelang] gives the name of the
   * language.
   *
   * @param shown the languages to show, or null to show them all
   * @param colored whether to color the content of the blocks according to their languages
   */
  public static String filterHTML(String in, Collection<ProgrammingLanguage> shown, boolean colored)
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

  private static boolean hideLang(String cssClass)
  {
    if (cssClass == null)
      return false;
    if (cssClass.toLowerCase().equals(Game.getInstance().getProgrammingLanguage().getLang().toLowerCase()))
      return false;
    return true;
  }

  public static class HTMLFactoryX extends HTMLEditorKit.HTMLFactory implements ViewFactory {
    boolean visible = false;
    @Override public View create(Element element)
    {
      Element iterElem   = element;
      String theCSSClass = (String)iterElem.getAttributes().getAttribute(HTML.Attribute.CLASS);
      /*
      while (theCSSClass == null && iterElem != null) {
              iterElem = iterElem.getParentElement();
              if (iterElem != null)
                      theCSSClass = (String) iterElem.getAttributes().getAttribute(HTML.Attribute.CLASS);
      }
       */
      if (!Game.getInstance().isDebugEnabled() // Display everything when in debug mode
          && hideLang(theCSSClass)) {
        return new EmptyView(element);
      }

      Object tagName = element.getAttributes().getAttribute(StyleConstants.NameAttribute);
      if (tagName instanceof HTML.Tag) {
        HTML.Tag tag = (HTML.Tag)tagName;
        if (tag == HTML.Tag.IMG)
          return new MyIconView(element, baseExercise);
      }
      return super.create(element);
    }
  }

  protected static Lecture baseExercise = null;
  public PlmHtmlEditorKit() { baseExercise = null; }

  public PlmHtmlEditorKit(Lecture _baseExercise) { baseExercise = _baseExercise; }

  @Override public ViewFactory getViewFactory() { return new HTMLFactoryX(); }
  public static String getCSS()
  {
    String header = "  <style type=\"text/css\">\n"
                    + "    body { font-family: tahoma, \"Times New Roman\", serif; font-size:" + Game.getProperty(Game.PROP_FONT_SIZE, "10px", true) +
                    "; margin:10px; }\n"
                    + "    code { background:#EEEEEE; }\n"
                    + "    pre { background: #EEEEEE;\n"
                    + "          margin: 5px;\n"
                    + "          padding: 6px;\n"
                    + "          border: 1px inset;\n"
                    + "          width: 640px;\n"
                    + "          overflow: auto;\n"
                    + "          text-align: left;\n"
                    + "          font-family: \"Courrier New\", \"Courrier\", monospace; }\n"
                    + "   .comment { background:#EEEEEE;\n"
                    + "              font-family: \"Times New Roman\", serif;\n"
                    + "              color:#00AA00;\n"
                    + "              font-style: italic; }\n";
    String res;
    if (Game.getInstance().isDebugEnabled()) {
      /* In debugging mode, all languages are displayed, with differing colors */
      res = header;
      res += ".Java   {visibility: visible; color:#FF0000}\n";
      res += ".java   {visibility: visible; color:#FF0000}\n";
      res += ".python {visibility: visible; color:#008000}\n";
      res += ".Python {visibility: visible; color:#008000}\n";
      res += ".scala  {visibility: visible; color:#0000FF}\n";
      res += ".Scala  {visibility: visible; color:#0000FF}\n";
      res += "  </style>\n";
      return res;
    }
    return header + "  </style>\n";
  }
}

class EmptyView extends IconView {

  public EmptyView(Element elem) { super(elem); }
  @Override public float getPreferredSpan(int axis) { return 0; }
  @Override public void paint(Graphics g, Shape a) {}
}

/* This is a crude copy/paste of IconView where all I changed is the constructor.
 * The day where swing authors won't fight against subclassing by for example putting the c field as
 * private here, I will stop doing such nasty thing.
 *
 * But I have the feeling that we will move to SWT before that day happens :-/ [Mt] */

class MyIconView extends View {
  /**
   * Creates a new icon view that represents an element.
   *
   * @param elem the element to create a view for
   * @param baseExercise
   * @throws FileNotFoundException
   */
  public MyIconView(Element elem, Lecture baseExercise)
  {
    super(elem);
    String filename = (String)elem.getAttributes().getAttribute(HTML.Attribute.SRC);
    if (filename == null) {
      if (baseExercise == null)
        System.err.println(Game.i18n.tr("<img> tag without src attribute at offset {0}.", elem.getStartOffset()));
      else
        System.err.println(Game.i18n.tr("<img> tag without src attribute in exercise {0} at offset {1}", baseExercise.getName(), elem.getStartOffset()));

      c = (Icon)UIManager.getLookAndFeelDefaults().get("html.missingImage");
    } else {
      c = ResourcesCache.getIcon(filename, true);
      if (c != null)
        return;
      if (baseExercise != null)
        c = ResourcesCache.getIcon(baseExercise, filename);
      if (c != null)
        return;

      String resourceName = "/" + filename.replace('.', '/');
      resourceName        = resourceName.replaceAll("/png$", ".png").replaceAll("/jpg$", ".jpg");
      resourceName        = resourceName.replaceAll("/jpeg$", ".jpeg").replaceAll("/gif$", ".gif");

      InputStream s = getClass().getResourceAsStream(resourceName);

      try {
        if (s == null) {
          c = (Icon)UIManager.getLookAndFeelDefaults().get("html.missingImage");
          System.out.println("Broken image link: " + resourceName);
        } else
          c = new ImageIcon(ImageIO.read(s));
      } catch (IOException e) {
        e.printStackTrace();
        c = (Icon)UIManager.getLookAndFeelDefaults().get("html.missingImage");
      }
    }
  }

  // --- View methods ---------------------------------------------

  /**
   * Paints the icon.
   * The real paint behavior occurs naturally from the association
   * that the icon has with its parent container (the same
   * container hosting this view), so this simply allows us to
   * position the icon properly relative to the view.  Since
   * the coordinate system for the view is simply the parent
   * containers, positioning the child icon is easy.
   *
   * @param g the rendering surface to use
   * @param a the allocated region to render into
   * @see View#paint
   */
  @Override public void paint(Graphics g, Shape a)
  {
    Rectangle alloc = a.getBounds();
    c.paintIcon(getContainer(), g, alloc.x, alloc.y);
  }

  /**
   * Determines the preferred span for this view along an
   * axis.
   *
   * @param axis may be either View.X_AXIS or View.Y_AXIS
   * @return  the span the view would like to be rendered into
   *           Typically the view is told to render into the span
   *           that is returned, although there is no guarantee.
   *           The parent may choose to resize or break the view.
   * @exception IllegalArgumentException for an invalid axis
   */
  @Override public float getPreferredSpan(int axis)
  {
    switch (axis) {
      case View.X_AXIS:
        return c.getIconWidth();
      case View.Y_AXIS:
        return c.getIconHeight();
      default:
        throw new IllegalArgumentException("Invalid axis: " + axis);
    }
  }

  /**
   * Determines the desired alignment for this view along an
   * axis.  This is implemented to give the alignment to the
   * bottom of the icon along the y axis, and the default
   * along the x axis.
   *
   * @param axis may be either View.X_AXIS or View.Y_AXIS
   * @return the desired alignment >= 0.0f && <= 1.0f.  This should be
   *   a value between 0.0 and 1.0 where 0 indicates alignment at the
   *   origin and 1.0 indicates alignment to the full span
   *   away from the origin.  An alignment of 0.5 would be the
   *   center of the view.
   */
  @Override public float getAlignment(int axis)
  {
    switch (axis) {
      case View.Y_AXIS:
        return 1;
      default:
        return super.getAlignment(axis);
    }
  }

  /**
   * Provides a mapping from the document model coordinate space
   * to the coordinate space of the view mapped to it.
   *
   * @param pos the position to convert >= 0
   * @param a the allocated region to render into
   * @return the bounding box of the given position
   * @exception BadLocationException  if the given position does not
   *   represent a valid location in the associated document
   * @see View#modelToView
   */
  @Override public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException
  {
    int p0 = getStartOffset();
    int p1 = getEndOffset();
    if ((pos >= p0) && (pos <= p1)) {
      Rectangle r = a.getBounds();
      if (pos == p1) {
        r.x += r.width;
      }
      r.width = 0;
      return r;
    }
    throw new BadLocationException(pos + " not in range " + p0 + "," + p1, pos);
  }

  /**
   * Provides a mapping from the view coordinate space to the logical
   * coordinate space of the model.
   *
   * @param x the X coordinate >= 0
   * @param y the Y coordinate >= 0
   * @param a the allocated region to render into
   * @return the location within the model that best represents the
   *  given point of view >= 0
   * @see View#viewToModel
   */
  @Override public int viewToModel(float x, float y, Shape a, Position.Bias[] bias)
  {
    Rectangle alloc = (Rectangle)a;
    if (x < alloc.x + (alloc.width / 2)) {
      bias[0] = Position.Bias.Forward;
      return getStartOffset();
    }
    bias[0] = Position.Bias.Backward;
    return getEndOffset();
  }

  // --- member variables ------------------------------------------------

  private Icon c;
}
