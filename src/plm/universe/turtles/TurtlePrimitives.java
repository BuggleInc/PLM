package plm.universe.turtles;

import plm.core.lang.primitives.Primitive;
import plm.core.utils.ColorMapper;
import plm.universe.EntityPrimitivesBase;

import java.awt.*;

public interface TurtlePrimitives extends EntityPrimitivesBase {

    @Primitive(112)
    void forward(double dist);

    @Primitive(113)
    void backward(double dist);

    @Primitive(120)
    void circle(double radius);

    @Primitive(119)
    void moveTo(double newX, double newY);

    @Primitive(110)
    void left(double angle);

    @Primitive(111)
    void right(double angle);

    @Primitive(129)
    boolean isPenDown();

    @Primitive(128)
    void penDown();

    @Primitive(127)
    void penUp();

    @Primitive(121)
    void hide();

    @Primitive(122)
    void show();

    @Primitive(123)
    boolean isVisible();

    @Primitive(124)
    void clear();

    @Primitive(125)
    double getHeading();

    @Primitive(126)
    void setHeading(double heading);


    @Primitive(114)
    double getX();

    @Primitive(116)
    void setX(double x);

    @Primitive(115)
    double getY();

    @Primitive(117)
    void setY(double y);

    @Primitive(118)
    void setPos(double x, double y);

    @Primitive(130)
    default int color2int(Color c) {
        return ColorMapper.color2int(c);
    }

    @Primitive(131)
    void setColor(Color c);

    @Primitive(132)
    @Override
    boolean isSelected();
}
