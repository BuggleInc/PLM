package lessons.lander.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;
import plm.universe.Point;

/**
 * LanderEntityPrimitives
 */
public interface LanderEntityPrimitives extends EntityPrimitivesBase {

  @Primitive public Point[] getGround();
  @Primitive public double getX();
  @Primitive public double getY();
  @Primitive public double getSpeedX();
  @Primitive public double getSpeedY();
  @Primitive public double getAngle();
  @Primitive public int getThrust();
  @Primitive public int getFuel();

  @Primitive public void setDesiredAngle(double desiredAngle);
  @Primitive public void setDesiredThrust(int desiredThrust);

  @Primitive public boolean isFlying();
  @Primitive public void simulateStep();
}
