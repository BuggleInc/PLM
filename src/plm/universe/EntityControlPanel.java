package plm.universe;

import javax.swing.JPanel;
import plm.core.model.Game;

/** Interactive controls of the selected entity, under the world view. */
public abstract class EntityControlPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  public abstract void setEnabledControl(boolean enabled);

  /** Do a textual output corresponding to the fact that a button was pressed. This is a direct help */
  public static void echo(String name) { System.out.println(name + (Game.getInstance().getProgrammingLanguage().isJava() ? ";" : "")); }

  public void dispose() {}
}
