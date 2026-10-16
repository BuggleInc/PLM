package plm.universe.bugglequest;

import java.awt.*;
import plm.core.lang.primitives.Primitive;
import plm.core.lang.primitives.PrimitiveGroups;
import plm.core.model.Game;
import plm.core.utils.ColorMapper;
import plm.core.utils.InvalidColorNameException;
import plm.universe.Direction;
import plm.universe.EntityPrimitivesBase;
import plm.universe.PrimitiveRefusedException;
import plm.universe.bugglequest.exception.*;

@PrimitiveGroups({"Moving", "Information on the buggle", "About the brush", "Interacting with the world"})
public interface AbstractBugglePrimitives extends EntityPrimitivesBase {

  @Primitive(value = "Tells whether the brush is down.", group = "About the brush") boolean isBrushDown();

  @Primitive(value = "Puts the brush down: the buggle paints the cells it walks on.", group = "About the brush") void brushDown();

  @Primitive(value = "Raises the brush: the buggle stops painting.", group = "About the brush") void brushUp();

  /** Primitives that only explain to the students why their call is refused: wrong case or primitive of the turtles. */
  @Primitive default void penDown()
  {
    throw new PrimitiveRefusedException(
        Game.i18n.tr("Sorry Dave, I cannot let you use penDown() here. Buggles have brushes, not pens. Use brushDown() instead."));
  }

  @Primitive default void penUp()
  {
    throw new PrimitiveRefusedException(Game.i18n.tr("Sorry Dave, I cannot let you use penUp() here. Buggles have brushes, not pens. Use brushUp() instead."));
  }

  @Primitive default void Left()
  {
    throw new PrimitiveRefusedException(Game.i18n.tr("Sorry Dave, I cannot let you use Left() with an uppercase. Use left() instead."));
  }

  @Primitive default void Right()
  {
    throw new PrimitiveRefusedException(Game.i18n.tr("Sorry Dave, I cannot let you use Right() with an uppercase. Use right() instead."));
  }

  @Primitive(value = "Returns the color of the ground under the buggle.", group = "Interacting with the world") Color getGroundColor();

  @Primitive default String getGroundColorName() { return ColorMapper.color2name(getGroundColor()); }

  @Primitive(value = "Returns the color of the brush.", group = "About the brush") Color getBrushColor();

  @Primitive(value = "Changes the color of the brush.", group = "About the brush") void setBrushColor(Color c);

  /** Takes either a color name (containing a '/') or the rank of a color */
  @Primitive default void setBrushColorName(String name) throws InvalidColorNameException
  {
    setBrushColor(name.indexOf('/') >= 0 ? ColorMapper.name2color(name) : ColorMapper.int2color(Integer.parseInt(name)));
  }

  @Primitive(value = "Returns the color of the buggle's body.", group = "Information on the buggle") Color getBodyColor();

  @Primitive(value = "Changes the color of the buggle's body.", group = "Information on the buggle") void setBodyColor(Color c);

  @Primitive(value = "Returns the heading of the buggle.", group = "Information on the buggle") Direction getDirection();

  @Primitive(value = "Changes the heading of the buggle.", group = "Information on the buggle") void setDirection(Direction direction);

  @Primitive(value = "Turns a quarter to the left.", group = "Moving") void left();

  @Primitive(value = "Turns a quarter to the right.", group = "Moving") void right();

  @Primitive(value = "Turns back.", group = "Moving") void back();

  @Primitive(value = "Returns the height of the world.", group = "Interacting with the world") int getWorldHeight();

  @Primitive(value = "Returns the width of the world.", group = "Interacting with the world") int getWorldWidth();

  @Primitive(value = "Returns the X coordinate of the buggle. The point (0,0) is on the top left corner.", group = "Moving") int getX();

  @Primitive(value = "Changes the X coordinate of the buggle.", group = "Moving") void setX(int x) throws BuggleInOuterSpaceException;

  @Primitive(value = "Returns the Y coordinate of the buggle. The point (0,0) is on the top left corner.", group = "Moving") int getY();

  @Primitive(value = "Changes the Y coordinate of the buggle.", group = "Moving") void setY(int y) throws BuggleInOuterSpaceException;

  @Primitive(value = "Changes the position of the buggle.", group = "Moving") void setPos(int x, int y) throws BuggleInOuterSpaceException;

  @Primitive(value = "Moves one cell forward.", group = "Moving") void step() throws BuggleWallException;

  @Primitive(value = "Moves the given amount of cells forward.", group = "Moving") void forward(int steps) throws BuggleWallException;

  @Primitive(value = "Moves one cell backward.", group = "Moving") void stepBack() throws BuggleWallException;

  @Primitive(value = "Moves the given amount of cells backward.", group = "Moving") void backward(int steps) throws BuggleWallException;

  @Primitive(value = "Tells whether there is a wall in front of the buggle.", group = "Information on the buggle") boolean isFacingWall();

  @Primitive(value = "Tells whether there is a wall behind the buggle.", group = "Information on the buggle") boolean isBackingWall();

  @Primitive boolean isWallOnLeft();

  @Primitive boolean isWallOnRight();

  @Primitive(value = "Tells whether there is a baggle on the ground under the buggle.", group = "Interacting with the world") boolean isOverBaggle();

  @Primitive(value = "Tells whether the buggle carries a baggle.", group = "Interacting with the world") boolean isCarryingBaggle();

  @Primitive(value = "Picks up the baggle under the buggle. Fails if there is none or if the buggle already carries one.", group = "Interacting with the world")
  void pickupBaggle() throws NoBaggleUnderBuggleException, AlreadyHaveBaggleException;

  @Primitive(value = "Drops the carried baggle on the ground. Fails if the buggle carries none or if there is already one on the ground.",
             group = "Interacting with the world")
  void
  dropBaggle() throws AlreadyHaveBaggleException, DontHaveBaggleException;

  @Primitive(value = "Tells whether the buggle is currently selected in the interface.", group = "Information on the buggle") @Override boolean isSelected();

  @Primitive(value = "Tells whether there is a message on the ground under the buggle.", group = "Interacting with the world") boolean isOverMessage();

  @Primitive void seenError(String msg);

  @Primitive boolean haveSeenError();

  @Primitive(value = "Adds the given text to the message on the ground.", group = "Interacting with the world") void writeMessage(String msg);
  @Primitive void writeIntMessage(int cpt);

  @Primitive(value = "Returns the message on the ground under the buggle.", group = "Interacting with the world") String readMessage();

  @Primitive(value = "Erases the message on the ground under the buggle.", group = "Interacting with the world") void clearMessage();

  @Primitive @Override int getParamCount();

  @Primitive default char getIndicationBdr() { return isOverMessage() ? readMessage().charAt(0) : ' '; }

  @Primitive boolean hasTopWall(int x, int y);

  @Primitive boolean hasLeftWall(int x, int y);
}
