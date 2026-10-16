package plm.universe.bugglequest;

import java.awt.Color;
import plm.core.model.Game;
import plm.universe.Direction;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;
import plm.universe.bugglequest.exception.BuggleInOuterSpaceException;
import plm.universe.bugglequest.exception.BuggleWallException;
import plm.universe.bugglequest.exception.DontHaveBaggleException;
import plm.universe.bugglequest.exception.NoBaggleUnderBuggleException;

public class SimpleBuggle extends AbstractBuggle {
  /* Make it possible to instantiate SimpleBuggles from exercises so that Python also gets graphical window showing */
  public SimpleBuggle() { super(); }
  public SimpleBuggle(BuggleWorld world, String name, int i, int j, Direction north, Color color, Color brush)
  {
    super(world, name, i, j, north, color, brush);
  }
  @Override public void run()
  {
    // Overridden by children
  }

  /* Raise a window error when an exception is encountered */
  @Override public void step()
  {
    try {
      super.step();
    } catch (BuggleWallException e) {
      if (Game.getInstance().isBatchExecution()) // Unit tests ongoing
        throw new RuntimeException(e);
      if (!haveSeenError())
        javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
      seenError();
    }
  }

  /* Unlike the parent, does not throw BuggleWallException: step() already reports the walls */
  @Override public void forward(int steps)
  {
    for (int i = 0; i < steps; i++)
      step();
  }

  @Override public void stepBack()
  {
    try {
      super.stepBack();
    } catch (BuggleWallException e) {
      if (Game.getInstance().isBatchExecution()) // Unit tests ongoing
        throw new RuntimeException(e);
      if (!haveSeenError())
        javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
      seenError();
    }
  }

  /* Unlike the parent, does not throw BuggleWallException: stepBack() already reports the walls */
  @Override public void backward(int steps)
  {
    for (int i = 0; i < steps; i++)
      stepBack();
  }

  @Override public void pickupBaggle()
  {
    try {
      super.pickupBaggle();
    } catch (NoBaggleUnderBuggleException e) {
      if (Game.getInstance().isBatchExecution()) // Unit tests ongoing
        throw new RuntimeException(e);
      if (!haveSeenError())
        javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
      seenError();
    } catch (AlreadyHaveBaggleException e) {
      if (Game.getInstance().isBatchExecution()) // Unit tests ongoing
        throw new RuntimeException(e);
      if (!haveSeenError())
        javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
      seenError();
    }
  }

  @Override public void dropBaggle()
  {
    try {
      super.dropBaggle();
    } catch (AlreadyHaveBaggleException e) {
      if (Game.getInstance().isBatchExecution()) // Unit tests ongoing
        throw new RuntimeException(e);
      if (!haveSeenError())
        javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
      seenError();
    } catch (DontHaveBaggleException e) {
      if (Game.getInstance().isBatchExecution()) // Unit tests ongoing
        throw new RuntimeException(e);
      if (!haveSeenError())
        javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
      seenError();
    }
  }
  @Override public void setX(int x)
  {
    try {
      super.setX(x);
    } catch (BuggleInOuterSpaceException e) {
      if (Game.getInstance().isBatchExecution()) // Unit tests ongoing
        throw new RuntimeException(e);
      if (!haveSeenError())
        javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
      seenError();
    }
  }
  @Override public void setY(int y)
  {
    try {
      super.setY(y);
    } catch (BuggleInOuterSpaceException e) {
      if (Game.getInstance().isBatchExecution()) // Unit tests ongoing
        throw new RuntimeException(e);
      if (!haveSeenError())
        javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
      seenError();
    }
  }
  @Override public void setPos(int x, int y)
  {
    try {
      super.setPos(x, y);
    } catch (BuggleInOuterSpaceException e) {
      if (Game.getInstance().isBatchExecution()) // Unit tests ongoing
        throw new RuntimeException(e);
      if (!haveSeenError())
        javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
      seenError();
    }
  }
}
