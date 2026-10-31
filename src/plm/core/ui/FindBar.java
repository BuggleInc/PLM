package plm.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import plm.core.model.Game;

/**
 * A bar searching the text where Ctrl-F (Cmd-F on macOS) is pressed, in every window of the PLM: the code editor, the html views
 * (the mission, the documentation of the world, the trace of a world...) and the other texts. The text is the one having the
 * focus, else the one under the mouse, else the largest one of the window, so that Ctrl-F also works in a view that never takes
 * the focus. The bar floats over the top right corner of the text. It highlights all the matches (ignoring the case) and the
 * current one, moved by Enter and Shift-Enter (or F3 and Shift-F3, or its arrows), and Escape closes it. It follows the changes
 * of the text, such as a trace growing while the program runs.
 */
public final class FindBar extends JPanel {
  private static final long serialVersionUID                = 1L;
  private static final Highlighter.HighlightPainter MATCH   = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 236, 110));
  private static final Highlighter.HighlightPainter CURRENT = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 160, 50));

  /** The open bar, if any: a single one at a time */
  private static FindBar open;

  private final JTextComponent target;
  /** What the bar floats over: the viewport showing the text, or the text itself */
  private final JComponent anchor;
  private final JLayeredPane layer;
  private final JTextField field = new JTextField(18);
  private final JLabel count     = new JLabel();
  private final Color background = field.getBackground();

  /** The positions of the matches in the text, the current one (or -1), and their highlights */
  private List<Integer> matches         = new ArrayList<>();
  private int current                   = -1;
  private final List<Object> highlights = new ArrayList<>();

  /** Whether a search is planned after a change of the text, which may come from another thread */
  private boolean planned;
  private Document document;
  private final DocumentListener changes = new DocumentListener() {
    @Override public void insertUpdate(DocumentEvent e)
    {
      plan();
    }
    @Override public void removeUpdate(DocumentEvent e)
    {
      plan();
    }
    @Override public void changedUpdate(DocumentEvent e) {}
  };
  private final PropertyChangeListener newDocument = e -> follow(((JTextComponent)e.getSource()).getDocument());
  private final ComponentAdapter moves             = new ComponentAdapter() {
    @Override public void componentResized(ComponentEvent e)
    {
      place();
    }
    @Override public void componentMoved(ComponentEvent e)
    {
      place();
    }
  };
  private final HierarchyBoundsListener ancestorMoves = new HierarchyBoundsAdapter() {
    @Override public void ancestorMoved(HierarchyEvent e)
    {
      place();
    }
    @Override public void ancestorResized(HierarchyEvent e)
    {
      place();
    }
  };
  private final HierarchyListener hiding = e ->
  {
    if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && !e.getComponent().isShowing())
      close();
  };

  /** Lets Ctrl-F (Cmd-F on macOS) open the bar in every window, as the PLM starts */
  public static void install()
  {
    int shortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    int keys     = InputEvent.CTRL_DOWN_MASK | InputEvent.META_DOWN_MASK | InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK;
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
      if (e.getID() != KeyEvent.KEY_PRESSED || e.getKeyCode() != KeyEvent.VK_F || (e.getModifiersEx() & keys) != shortcut)
        return false;
      JTextComponent target = target(e.getComponent());
      if (target == null)
        return false;
      open(target);
      return true;
    });
  }

  /** Opens the bar on that text, or selects the searched text if the bar is already open on it */
  public static void open(JTextComponent target)
  {
    if (open != null && open.target == target) {
      open.field.selectAll();
      open.field.requestFocusInWindow();
      return;
    }
    if (open != null)
      open.close();
    JRootPane root = SwingUtilities.getRootPane(target);
    if (root != null)
      open = new FindBar(target, root.getLayeredPane());
  }

  /** The text to search: the focused one, else the one under the mouse, else the largest one of the window */
  private static JTextComponent target(Component focused)
  {
    if (focused instanceof JTextComponent text && searchable(text))
      return text;
    if (open != null && focused != null && SwingUtilities.isDescendingFrom(focused, open))
      return open.target;
    Window window = focused instanceof Window w ? w : focused == null ? null : SwingUtilities.getWindowAncestor(focused);
    if (window == null)
      return null;
    PointerInfo pointer = MouseInfo.getPointerInfo();
    if (pointer != null) {
      Point mouse = pointer.getLocation();
      SwingUtilities.convertPointFromScreen(mouse, window);
      JTextComponent under = textOf(SwingUtilities.getDeepestComponentAt(window, mouse.x, mouse.y));
      if (under != null)
        return under;
    }
    return largest(window, null);
  }

  /** The searchable text that this component is, or that it shows in its scroll pane (such as when it is a scrollbar), or null */
  private static JTextComponent textOf(Component component)
  {
    if (component instanceof JTextComponent text && searchable(text))
      return text;
    Container scroll = component == null ? null : SwingUtilities.getAncestorOfClass(JScrollPane.class, component);
    if (scroll != null && ((JScrollPane)scroll).getViewport().getView() instanceof JTextComponent text && searchable(text))
      return text;
    return null;
  }

  /** The searchable text showing the largest area among the components of that container, or best if none is larger */
  private static JTextComponent largest(Container container, JTextComponent best)
  {
    for (Component child : container.getComponents()) {
      if (child instanceof JTextComponent text && searchable(text) && (best == null || area(text) > area(best)))
        best = text;
      if (child instanceof Container inner)
        best = largest(inner, best);
    }
    return best;
  }

  private static long area(JComponent component) { return (long)component.getVisibleRect().width * component.getVisibleRect().height; }

  /** Whether that text is worth searching: shown, and not a single line field (such as the one of this bar) */
  private static boolean searchable(JTextComponent text) { return text.isShowing() && !(text instanceof JTextField); }

  private FindBar(JTextComponent target, JLayeredPane layer)
  {
    super(new FlowLayout(FlowLayout.LEFT, 4, 2));
    this.target = target;
    this.layer  = layer;
    anchor      = target.getParent() instanceof JViewport viewport ? viewport : target;
    setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(2, 4, 2, 2)));
    setBackground(UIManager.getColor("Panel.background"));

    field.setToolTipText(Game.i18n.tr("Find in this text (case insensitive)"));
    add(field);
    add(count);
    add(button("\u2191", Game.i18n.tr("Previous match (Shift+Enter)"), () -> move(-1)));
    add(button("\u2193", Game.i18n.tr("Next match (Enter)"), () -> move(1)));
    add(button("\u00d7", Game.i18n.tr("Close (Escape)"), this::close));
    bind("ENTER", () -> move(1));
    bind("F3", () -> move(1));
    bind("shift ENTER", () -> move(-1));
    bind("shift F3", () -> move(-1));
    bind("ESCAPE", this::close);
    field.getDocument().addDocumentListener(new DocumentListener() {
      @Override public void insertUpdate(DocumentEvent e)
      {
        search(true);
      }
      @Override public void removeUpdate(DocumentEvent e)
      {
        search(true);
      }
      @Override public void changedUpdate(DocumentEvent e) {}
    });

    follow(target.getDocument());
    target.addPropertyChangeListener("document", newDocument);
    anchor.addComponentListener(moves);
    anchor.addHierarchyBoundsListener(ancestorMoves);
    anchor.addHierarchyListener(hiding);

    String selected = target.getSelectedText(); // the selection, as the usual editors do, when it holds a single line
    if (selected != null && !selected.isEmpty() && !selected.contains("\n"))
      field.setText(selected);
    field.selectAll();
    layer.add(this, JLayeredPane.POPUP_LAYER);
    place();
    field.requestFocusInWindow();
  }

  private JButton button(String label, String tooltip, Runnable action)
  {
    JButton res = new JButton(label);
    res.setToolTipText(tooltip);
    res.setMargin(new Insets(0, 4, 0, 4));
    res.setFocusable(false);
    res.addActionListener(e -> action.run());
    return res;
  }

  /** Lets that key of the field run that action */
  private void bind(String key, Runnable action)
  {
    field.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(key), key);
    field.getActionMap().put(key, new AbstractAction() {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e)
      {
        action.run();
      }
    });
  }

  /** Puts the bar in the top right corner of the visible part of the text */
  private void place()
  {
    if (!anchor.isShowing())
      return;
    Rectangle area = SwingUtilities.convertRectangle(anchor, new Rectangle(anchor.getSize()), layer);
    setSize(getPreferredSize());
    setLocation(Math.max(area.x, area.x + area.width - getWidth() - 4), area.y + 4);
    layer.repaint();
  }

  /** Follows the changes of that document of the text, which replaces the previous one if any */
  private void follow(Document next)
  {
    if (document != null)
      document.removeDocumentListener(changes);
    document = next;
    document.addDocumentListener(changes);
    plan();
  }

  /** Plans a new search after a change of the text, coming from any thread, once the change is done */
  private void plan()
  {
    if (planned)
      return;
    planned = true;
    SwingUtilities.invokeLater(() -> {
      planned = false;
      if (open == this)
        search(false);
    });
  }

  /**
   * Finds all the matches of the field in the text. The current match becomes the first one from the previous current one, or
   * from the top of the visible part of the text when the field changed and nothing matched yet. Only a change of the field
   * scrolls to it, so that a text changing under the eyes of the student does not move.
   */
  private void search(boolean scroll)
  {
    String query = field.getText();
    String text;
    try {
      text = document.getText(0, document.getLength());
    } catch (BadLocationException e) {
      text = "";
    }
    int from = current >= 0 ? matches.get(current) : target.viewToModel2D(target.getVisibleRect().getLocation());
    matches  = new ArrayList<>();
    for (int i = 0; !query.isEmpty() && i + query.length() <= text.length(); i++)
      if (text.regionMatches(true, i, query, 0, query.length()))
        matches.add(i);
    current = matches.isEmpty() ? -1 : 0;
    for (int i = 0; i < matches.size(); i++)
      if (matches.get(i) >= from) {
        current = i;
        break;
      }
    highlight(scroll);
  }

  /** Moves the current match forward or backward, going around at the ends */
  private void move(int step)
  {
    if (matches.isEmpty())
      return;
    current = Math.floorMod(current + step, matches.size());
    highlight(true);
  }

  /** Highlights the matches and tells their number, scrolling to the current one if asked */
  private void highlight(boolean scroll)
  {
    Highlighter highlighter = target.getHighlighter();
    for (Object highlight : highlights)
      highlighter.removeHighlight(highlight);
    highlights.clear();
    int length = field.getText().length();
    try {
      for (int i = 0; i < matches.size(); i++)
        highlights.add(highlighter.addHighlight(matches.get(i), matches.get(i) + length, i == current ? CURRENT : MATCH));
      if (scroll && current >= 0) {
        Rectangle match = target.modelToView2D(matches.get(current)).getBounds().union(target.modelToView2D(matches.get(current) + length).getBounds());
        match.add(match.x, match.y - getHeight() - 8); // and the room of the bar above it, so that the bar does not hide it
        target.scrollRectToVisible(match);
      }
    } catch (BadLocationException e) {
      plan(); // the text changed in the meantime
    }
    boolean missing = matches.isEmpty() && !field.getText().isEmpty();
    count.setText(field.getText().isEmpty() ? "" : missing ? Game.i18n.tr("No match") : Game.i18n.tr("{0} of {1}", current + 1, matches.size()));
    field.setBackground(missing ? new Color(255, 200, 200) : background);
    place(); // the label changed the width of the bar
  }

  /** Closes the bar, leaving the current match selected in an editable text, where the focus returns */
  private void close()
  {
    if (open != this)
      return;
    open = null;
    for (Object highlight : highlights)
      target.getHighlighter().removeHighlight(highlight);
    document.removeDocumentListener(changes);
    target.removePropertyChangeListener("document", newDocument);
    anchor.removeComponentListener(moves);
    anchor.removeHierarchyBoundsListener(ancestorMoves);
    anchor.removeHierarchyListener(hiding);
    layer.remove(this);
    layer.repaint();
    if (target.isEditable() && current >= 0)
      target.select(matches.get(current), matches.get(current) + field.getText().length());
    target.requestFocusInWindow();
  }
}
