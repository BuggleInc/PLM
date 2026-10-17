package lessons.lander.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;
import plm.universe.Point;

/**
 * LanderEntityPrimitives
 */
public interface LanderEntityPrimitives extends EntityPrimitivesBase {

  @Primitive(value = "Returns the vertices of the ground's profile, from left to right. "
                     + "[!java|scala]Get their coordinates with <code>p.x()</code> and <code>p.y()</code>.[/!]"
                     + "[!python]Each point is an array of two integers: its X and Y coordinates.[/!]")
  public Point[]
  getGround();

  @Primitive(value = "Returns the X position of the lander.") public double getX();
  @Primitive(value = "Returns the Y position of the lander.") public double getY();
  @Primitive(value = "Returns the horizontal speed of the lander.") public double getSpeedX();
  @Primitive(value = "Returns the vertical speed of the lander.") public double getSpeedY();

  @Primitive(value = "Returns the angle of the lander. At 0°, the head of the lander points up: this is the angle you must land with. "
                     + "A positive angle denotes a left-leaning lander, a negative one a right-leaning lander.")
  public double
  getAngle();

  @Primitive(value = "Returns the thrust of the lander.") public int getThrust();
  @Primitive(value = "Returns the remaining fuel. At each step, the lander consumes as many fuel units as its thrust. Without fuel, you are in free fall!")
  public int getFuel();

  @Primitive(value = "Asks for a new angle. You cannot lean more than ±90°, and the angle varies by at most ±5° per step.")
  public void setDesiredAngle(double desiredAngle);

  @Primitive(
      value = "Asks for a new thrust, between 0 and 4. The thrust varies by at most 1 per step: if it is 2 and you ask for 4, you get 3 at the next step.")
  public void
  setDesiredThrust(int desiredThrust);

  @Primitive public boolean isFlying();
  @Primitive public void simulateStep();
}
