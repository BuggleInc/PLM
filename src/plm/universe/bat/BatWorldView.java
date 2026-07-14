package plm.universe.bat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.List;
import plm.core.model.Game;
import plm.core.ui.WorldView;
import plm.universe.World;

public class BatWorldView extends WorldView {

  private static final long serialVersionUID = 1L;

  public BatWorldView(World w) { super(w); }

  @Override public boolean isWorldCompatible(World world) { return world instanceof BatWorld; }

  @Override public void paintComponent(Graphics g)
  {

    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(Color.white);
    g2.fill(new Rectangle2D.Double(0., 0., (double)getWidth(), (double)getHeight()));

    List<BatTest> tests  = ((BatWorld)world).getTests();
    boolean foundError   = false;
    BatWorld answerWorld = (BatWorld)Game.getInstance().getAnswerOfSelectedWorld();
    if (answerWorld == null)
      return; // Play safe when the exercise is not completely setup

    for (int i = 0; i < tests.size(); i++) {
      BatTest currTest = tests.get(i);
      Object expected  = answerWorld.tests.get(i).result;
      Object actual    = currTest.result;
      boolean answered = actual != null;
      boolean correct  = ValueFormatter.equals(actual, expected);

      if (!currTest.isVisible() && foundError)
        break;

      if (world.isAnswerWorld()) {
        if (currTest.isVisible())
          g2.setColor(Color.black);
        else
          g2.setColor(Color.white);
      } else {
        if (answered) {
          if (correct)
            g2.setColor(Color.blue);
          else {
            g2.setColor(Color.red);
            foundError = true;
          }
        } else {
          if (currTest.isVisible())
            g2.setColor(Color.black);
          else
            g2.setColor(Color.white);
        }
      }
      g2.drawString(currTest.getName() + (answered ? " gives " + actual : "") +
                        (answered && !correct ? " instead of " + currTest.stringParameter(expected) : ""),
                    0, (i + 1) * 20);
    }
  }
}
