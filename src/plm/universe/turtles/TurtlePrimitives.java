package plm.universe.turtles;

import java.awt.*;
import plm.core.lang.primitives.Primitive;
import plm.core.model.Game;
import plm.core.utils.ColorMapper;
import plm.universe.EntityPrimitivesBase;

public interface TurtlePrimitives extends EntityPrimitivesBase {

  @Primitive void forward(double dist);

  @Primitive void backward(double dist);

  @Primitive void circle(double radius);

  @Primitive void moveTo(double newX, double newY);

  @Primitive void left(double angle);

  @Primitive void right(double angle);

  @Primitive boolean isPenDown();

  @Primitive void penDown();

  @Primitive void penUp();

  /** Primitives that only explain to the students why their call is refused: wrong case or primitive of the buggles. */
  @Primitive default void Left(double angle)
  {
    throw new UnsupportedOperationException(Game.i18n.tr("Sorry Dave, I cannot let you use Left() with an uppercase. Use left() instead."));
  }

  @Primitive default void Right(double angle)
  {
    throw new UnsupportedOperationException(Game.i18n.tr("Sorry Dave, I cannot let you use Right() with an uppercase. Use right() instead."));
  }

  @Primitive default void brushDown()
  {
    throw new UnsupportedOperationException(
        Game.i18n.tr("Sorry Dave, I cannot let you use brushDown() here. Turtles have pens, not brushes. Use penDown() instead."));
  }

  @Primitive default void brushUp()
  {
    throw new UnsupportedOperationException(
        Game.i18n.tr("Sorry Dave, I cannot let you use brushUp() here. Turtles have pens, not brushes. Use penUp() instead."));
  }

  @Primitive void hide();

  @Primitive void show();

  @Primitive boolean isVisible();

  @Primitive void clear();

  @Primitive double getHeading();

  @Primitive void setHeading(double heading);

  @Primitive double getX();

  @Primitive void setX(double x);

  @Primitive double getY();

  @Primitive void setY(double y);

  @Primitive void setPos(double x, double y);

  @Primitive default int color2int(Color c) { return ColorMapper.color2int(c); }

  @Primitive void setColor(Color c);

  @Primitive @Override boolean isSelected();

  @Primitive public void addSizeHint(int x1, int y1, int x2, int y2);
}
