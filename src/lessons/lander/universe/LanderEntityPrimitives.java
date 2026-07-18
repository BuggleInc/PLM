package lessons.lander.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

/**
 * LanderEntityPrimitives
 */
public interface LanderEntityPrimitives extends EntityPrimitivesBase {

  @Primitive(300) public double getX();
  @Primitive(301) public double getY();
  @Primitive(302) public double getSpeedX();
  @Primitive(303) public double getSpeedY();
  @Primitive(304) public double getAngle();
  @Primitive(305) public int getThrust();
  @Primitive(306) public int getFuel();

  @Primitive(307) public void setDesiredAngle(double desiredAngle);
  @Primitive(308) public void setDesiredThrust(int desiredThrust);

  @Primitive(309) public boolean isFlying();
  @Primitive(310) public void simulateStep();
}
