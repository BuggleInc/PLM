package plm.universe.bugglequest;

import java.awt.*;
import plm.core.lang.primitives.Primitive;
import plm.core.utils.ColorMapper;
import plm.core.utils.InvalidColorNameException;
import plm.universe.Direction;
import plm.universe.EntityPrimitivesBase;
import plm.universe.bugglequest.exception.*;

public interface AbstractBugglePrimitives extends EntityPrimitivesBase {

  @Primitive(129) boolean isBrushDown();

  @Primitive(128) void brushDown();

  @Primitive(127) void brushUp();

  @Primitive(132) Color getGroundColor();

  @Primitive(149) default String getGroundColorName() { return ColorMapper.color2name(getGroundColor()); }

  @Primitive(131) Color getBrushColor();

  default void primitiveSetBrushColor(String arg) throws InvalidColorNameException
  {
    if (arg.indexOf('/') >= 0)
      setBrushColor(ColorMapper.name2color(arg));
    else {
      int nb = Integer.parseInt(arg);
      setBrushColor(ColorMapper.int2color(nb));
    }
  }

  @Primitive(130) void setBrushColor(Color c);

  @Primitive(230) default void setBrushColorName(String name) throws InvalidColorNameException { primitiveSetBrushColor(name); }

  @Primitive(120) Color getBodyColor();

  @Primitive(121) void setBodyColor(Color c);

  default int primitiveGetDirection() { return getDirection().ordinal(); }

  @Primitive(value = 124) Direction getDirection();

  default void primitiveSetDirection(int nb) { setDirection(Direction.values()[nb]); }

  @Primitive(value = 125) void setDirection(Direction direction);

  @Primitive(110) void left();

  @Primitive(111) void right();

  @Primitive(112) void back();

  @Primitive(141) int getWorldHeight();

  @Primitive(142) int getWorldWidth();

  @Primitive(115) int getX();

  @Primitive(117) void setX(int x) throws BuggleInOuterSpaceException;

  @Primitive(116) int getY();

  @Primitive(118) void setY(int y) throws BuggleInOuterSpaceException;

  @Primitive(119) void setPos(int x, int y) throws BuggleInOuterSpaceException;

  @Primitive(value = 113, name = "forward") default void primitiveForward(int nb) throws BuggleWallException
  {
    if (nb == 1) {
      stepForward();
    } else {
      forward(nb);
    }
  }

  @Primitive(220) void stepForward() throws BuggleWallException;

  void forward(int count) throws BuggleWallException;

  @Primitive(value = 114, name = "backward") default void primitiveBackward(int nb) throws BuggleWallException
  {
    if (nb == 1) {
      stepBackward();
    } else {
      backward(nb);
    }
  }

  @Primitive(221) void stepBackward() throws BuggleWallException;

  void backward(int count) throws BuggleWallException;

  @Primitive(122) boolean isFacingWall();

  @Primitive(123) boolean isBackingWall();

  @Primitive(150) boolean isWallOnLeft();

  @Primitive(151) boolean isWallOnRight();

  @Primitive(133) boolean isOverBaggle();

  @Primitive(134) boolean isCarryingBaggle();

  @Deprecated @Primitive(135) void pickupBaggle() throws NoBaggleUnderBuggleException, AlreadyHaveBaggleException;

  @Primitive(136) void dropBaggle() throws AlreadyHaveBaggleException, DontHaveBaggleException;

  @Primitive(126) @Override boolean isSelected();

  @Primitive(137) boolean isOverMessage();

  @Primitive(153) void seenError(String msg);

  @Primitive(154) boolean haveSeenError();

  @Primitive(138) void writeMessage(String msg);

  @Primitive(139) String readMessage();

  @Primitive(140) void clearMessage();

  @Primitive(201) @Override int getParamCount();

  @Primitive(148) default char getIndicationBdr() { return isOverMessage() ? readMessage().charAt(0) : ' '; }

  @Primitive(value = 146) boolean hasTopWall(int x, int y);

  @Primitive(value = 147) boolean hasLeftWall(int x, int y);
}
