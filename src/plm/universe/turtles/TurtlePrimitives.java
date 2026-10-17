package plm.universe.turtles;

import java.awt.*;
import plm.core.lang.primitives.Primitive;
import plm.core.lang.primitives.PrimitiveGroups;
import plm.core.model.Game;
import plm.core.utils.ColorMapper;
import plm.universe.EntityPrimitivesBase;
import plm.universe.PrimitiveRefusedException;

@PrimitiveGroups({"Moving the turtle", "About the pen", "Other functions"})
public interface TurtlePrimitives extends EntityPrimitivesBase {

  @Primitive(value = "Moves forward of the given amount of steps.", group = "Moving the turtle") void forward(double dist);

  @Primitive(value = "Moves backward of the given amount of steps.", group = "Moving the turtle") void backward(double dist);

  @Primitive(value = "Draws a circle of the given radius, centered on the turtle.", group = "Moving the turtle") void circle(double radius);

  @Primitive(value = "Moves the turtle to the given position.", group = "Moving the turtle") void moveTo(double newX, double newY);

  @Primitive(value = "Turns left of the given angle (in degrees).", group = "Moving the turtle") void left(double angle);

  @Primitive(value = "Turns right of the given angle (in degrees).", group = "Moving the turtle") void right(double angle);

  @Primitive(value = "Tells whether the pen is down.", group = "About the pen") boolean isPenDown();

  @Primitive(value = "Moves the pen down: the turtle leaves a trace when it moves.", group = "About the pen") void penDown();

  @Primitive(value = "Moves the pen up: the turtle stops leaving a trace when it moves.", group = "About the pen") void penUp();

  /** Primitives that only explain to the students why their call is refused: wrong case or primitive of the buggles. */
  @Primitive default void Left(double angle)
  {
    throw new PrimitiveRefusedException(Game.i18n.tr("Sorry Dave, I cannot let you use Left() with an uppercase. Use left() instead."));
  }

  @Primitive default void Right(double angle)
  {
    throw new PrimitiveRefusedException(Game.i18n.tr("Sorry Dave, I cannot let you use Right() with an uppercase. Use right() instead."));
  }

  @Primitive default void brushDown()
  {
    throw new PrimitiveRefusedException(
        Game.i18n.tr("Sorry Dave, I cannot let you use brushDown() here. Turtles have pens, not brushes. Use penDown() instead."));
  }

  @Primitive default void brushUp()
  {
    throw new PrimitiveRefusedException(Game.i18n.tr("Sorry Dave, I cannot let you use brushUp() here. Turtles have pens, not brushes. Use penUp() instead."));
  }

  @Primitive(value = "Hides the turtle.", group = "Other functions") void hide();

  @Primitive(value = "Shows the turtle back.", group = "Other functions") void show();

  @Primitive(value = "Tells whether the turtle is visible.", group = "Other functions") boolean isVisible();

  @Primitive(value = "Removes every trail from the sheet.", group = "Other functions") void clearAll();

  @Primitive(value = "Returns the heading of the turtle (in degrees).", group = "Moving the turtle") double getHeading();

  @Primitive(value = "Changes the heading of the turtle (in degrees).", group = "Moving the turtle") void setHeading(double heading);

  @Primitive(value = "Returns the X coordinate of the turtle. The point (0,0) is on the top left corner.", group = "Moving the turtle") double getX();

  @Primitive(value = "Teleports the turtle to the given X coordinate, without leaving any trace.", group = "Moving the turtle") void setX(double x);

  @Primitive(value = "Returns the Y coordinate of the turtle. The point (0,0) is on the top left corner.", group = "Moving the turtle") double getY();

  @Primitive(value = "Teleports the turtle to the given Y coordinate, without leaving any trace.", group = "Moving the turtle") void setY(double y);

  @Primitive(value = "Teleports the turtle to the given position, without leaving any trace.", group = "Moving the turtle") void setPos(double x, double y);

  @Primitive default int color2int(Color c) { return ColorMapper.color2int(c); }

  @Primitive(value = "Changes the color of the pen.", group = "About the pen") void setColor(Color c);

  @Primitive(value = "Tells whether the turtle is currently selected in the interface.", group = "Other functions") @Override boolean isSelected();

  @Primitive public void addSizeHint(int x1, int y1, int x2, int y2);
}
