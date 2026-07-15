package lessons.lander.universe;

import java.util.List;
import lessons.lander.universe.LanderWorld.Point;
import plm.universe.Entity;

public class LanderEntity extends Entity {

  private LanderWorld landerWorld() { return (LanderWorld)getWorld(); }

  @Override public void run()
  {
    initialize();
    while (isFlying()) {
      step();
      simulateStep();
    }
  }

  // methods to be overridden by the player
  public void initialize() {}
  public void step() {}

  // query terrain
  public List<Point> getGround() { return landerWorld().ground; }

  // query lander state
  public double getX() { return landerWorld().position.x(); }
  public double getY() { return landerWorld().position.y(); }
  public double getSpeedX() { return landerWorld().speed.x(); }
  public double getSpeedY() { return landerWorld().speed.y(); }
  public double getAngle() { return landerWorld().angle; }
  public int getThrust() { return landerWorld().thrust; }
  public int getFuel() { return landerWorld().fuel; }

  public void setDesiredAngle(double desiredAngle) { landerWorld().desiredAngle = desiredAngle; }
  public void setDesiredThrust(int desiredThrust) { landerWorld().desiredThrust = desiredThrust; }

  /* Internal commands used by the python entities to simulate the above run method */
  public boolean isFlying() { return landerWorld().state == LanderWorld.State.FLYING; }
  public void simulateStep()
  {
    landerWorld().simulate(0.1);
    stepUI();
  }

  /* BINDINGS TRANSLATION: French */
  public List<Point> getSol() { return getGround(); }
  public double getVitesseX() { return getSpeedX(); }
  public double getVitesseY() { return getSpeedY(); }
  public int getPoussee() { return getThrust(); }
  public void setAngleDesire(double desiredAngle) { setDesiredAngle(desiredAngle); }
  public void setPousseeDesiree(int desiredThrust) { setDesiredThrust(desiredThrust); }
}
