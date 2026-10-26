package plm.core.ui;

import java.awt.Font;
import java.util.function.Supplier;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import plm.universe.IWorldView;
import plm.universe.World;

/**
 * A tab of the editor showing a text of the world that the student cannot edit, such as the content of a file written by the
 * program. It follows the changes of the world while it is open (see MissionEditorTabs.showReadOnly()).
 */
public class ReadOnlyTab extends JScrollPane implements IWorldView {
  private static final long serialVersionUID = 1L;

  final World world;
  private Supplier<String> content;
  private final JTextArea text = new JTextArea();

  ReadOnlyTab(World world, Supplier<String> content)
  {
    this.world = world;
    text.setEditable(false);
    text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    setViewportView(text);
    world.addWorldUpdatesListener(this);
    show(content);
  }

  /** Shows that content of the world from now on, such as the one of a socket created by a new execution */
  void show(Supplier<String> content)
  {
    this.content = content;
    refresh();
  }

  @Override public void worldHasMoved() { SwingUtilities.invokeLater(this::refresh); } // the execution threads move the world

  @Override public void worldHasChanged() { worldHasMoved(); }

  private void refresh()
  {
    String now = content.get();
    if (!now.equals(text.getText()))
      text.setText(now);
  }

  /** Stops following the world, when the tab is closed */
  void dispose() { world.removeWorldUpdatesListener(this); }
}
