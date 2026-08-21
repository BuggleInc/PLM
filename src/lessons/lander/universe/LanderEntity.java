package lessons.lander.universe;

import plm.core.lang.primitives.EntityPrimitives;
import plm.universe.Entity;
import plm.universe.Point;

@EntityPrimitives(LanderEntityPrimitives.class)
public class LanderEntity extends Entity implements LanderEntityPrimitives {

  private LanderWorld landerWorld() { return (LanderWorld)getWorld(); }

  @Override public void run() { /* To be overwritten */ }

  // methods to be overridden by the player
  public void initialize() {}
  public void step() {}

  // query terrain
  @Override public Point[] getGround() { return landerWorld().ground; }

  // query lander state
  @Override public double getX() { return landerWorld().position.x(); }
  @Override public double getY() { return landerWorld().position.y(); }
  @Override public double getSpeedX() { return landerWorld().speed.x(); }
  @Override public double getSpeedY() { return landerWorld().speed.y(); }
  @Override public double getAngle() { return landerWorld().angle; }
  @Override public int getThrust() { return landerWorld().thrust; }
  @Override public int getFuel() { return landerWorld().fuel; }

  @Override public void setDesiredAngle(double desiredAngle) { landerWorld().desiredAngle = desiredAngle; }
  @Override public void setDesiredThrust(int desiredThrust) { landerWorld().desiredThrust = desiredThrust; }

  @Override public boolean isFlying() { return landerWorld().state == LanderWorld.State.FLYING; }
  @Override public void simulateStep()
  {
    landerWorld().simulate(0.1);
    stepUI();
  }

  /* BINDINGS TRANSLATION: French */
  public Point[] getSol() { return getGround(); }
  public double getVitesseX() { return getSpeedX(); }
  public double getVitesseY() { return getSpeedY(); }
  public int getPoussee() { return getThrust(); }
  public void setAngleDesire(double desiredAngle) { setDesiredAngle(desiredAngle); }
  public void setPousseeDesiree(int desiredThrust) { setDesiredThrust(desiredThrust); }
}
