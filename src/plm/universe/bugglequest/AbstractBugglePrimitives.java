package plm.universe.bugglequest;

import java.awt.*;
import plm.core.lang.primitives.Primitive;
import plm.core.model.Game;
import plm.core.utils.ColorMapper;
import plm.core.utils.InvalidColorNameException;
import plm.universe.Direction;
import plm.universe.EntityPrimitivesBase;
import plm.universe.bugglequest.exception.*;

public interface AbstractBugglePrimitives extends EntityPrimitivesBase {

  @Primitive boolean isBrushDown();

  @Primitive void brushDown();

  @Primitive void brushUp();

  /** Primitives that only explain to the students why their call is refused: wrong case or primitive of the turtles. */
  @Primitive default void penDown()
  {
    throw new UnsupportedOperationException(
        Game.i18n.tr("Sorry Dave, I cannot let you use penDown() here. Buggles have brushes, not pens. Use brushDown() instead."));
  }

  @Primitive default void penUp()
  {
    throw new UnsupportedOperationException(
        Game.i18n.tr("Sorry Dave, I cannot let you use penUp() here. Buggles have brushes, not pens. Use brushUp() instead."));
  }

  @Primitive default void Left()
  {
    throw new UnsupportedOperationException(Game.i18n.tr("Sorry Dave, I cannot let you use Left() with an uppercase. Use left() instead."));
  }

  @Primitive default void Right()
  {
    throw new UnsupportedOperationException(Game.i18n.tr("Sorry Dave, I cannot let you use Right() with an uppercase. Use right() instead."));
  }

  @Primitive Color getGroundColor();

  @Primitive default String getGroundColorName() { return ColorMapper.color2name(getGroundColor()); }

  @Primitive Color getBrushColor();

  default void primitiveSetBrushColor(String arg) throws InvalidColorNameException
  {
    if (arg.indexOf('/') >= 0)
      setBrushColor(ColorMapper.name2color(arg));
    else {
      int nb = Integer.parseInt(arg);
      setBrushColor(ColorMapper.int2color(nb));
    }
  }

  @Primitive void setBrushColor(Color c);

  @Primitive default void setBrushColorName(String name) throws InvalidColorNameException { primitiveSetBrushColor(name); }

  @Primitive Color getBodyColor();

  @Primitive void setBodyColor(Color c);

  default int primitiveGetDirection() { return getDirection().ordinal(); }

  @Primitive Direction getDirection();

  default void primitiveSetDirection(int nb) { setDirection(Direction.values()[nb]); }

  @Primitive void setDirection(Direction direction);

  @Primitive void left();

  @Primitive void right();

  @Primitive void back();

  @Primitive int getWorldHeight();

  @Primitive int getWorldWidth();

  @Primitive int getX();

  @Primitive void setX(int x) throws BuggleInOuterSpaceException;

  @Primitive int getY();

  @Primitive void setY(int y) throws BuggleInOuterSpaceException;

  @Primitive void setPos(int x, int y) throws BuggleInOuterSpaceException;

  @Primitive(name = "forward") default void primitiveForward(int nb) throws BuggleWallException
  {
    if (nb == 1) {
      stepForward();
    } else {
      forward(nb);
    }
  }

  @Primitive void stepForward() throws BuggleWallException;

  void forward(int count) throws BuggleWallException;

  @Primitive(name = "backward") default void primitiveBackward(int nb) throws BuggleWallException
  {
    if (nb == 1) {
      stepBackward();
    } else {
      backward(nb);
    }
  }

  @Primitive void stepBackward() throws BuggleWallException;

  void backward(int count) throws BuggleWallException;

  @Primitive boolean isFacingWall();

  @Primitive boolean isBackingWall();

  @Primitive boolean isWallOnLeft();

  @Primitive boolean isWallOnRight();

  @Primitive boolean isOverBaggle();

  @Primitive boolean isCarryingBaggle();

  @Deprecated @Primitive void pickupBaggle() throws NoBaggleUnderBuggleException, AlreadyHaveBaggleException;

  @Primitive void dropBaggle() throws AlreadyHaveBaggleException, DontHaveBaggleException;

  @Primitive @Override boolean isSelected();

  @Primitive boolean isOverMessage();

  @Primitive void seenError(String msg);

  @Primitive boolean haveSeenError();

  @Primitive void writeMessage(String msg);

  @Primitive String readMessage();

  @Primitive void clearMessage();

  @Primitive @Override int getParamCount();

  @Primitive default char getIndicationBdr() { return isOverMessage() ? readMessage().charAt(0) : ' '; }

  @Primitive boolean hasTopWall(int x, int y);

  @Primitive boolean hasLeftWall(int x, int y);
}
