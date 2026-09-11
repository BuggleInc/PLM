package lessons.lander.universe;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.Point;
import plm.universe.World;

public class LanderWorld extends World {

  /** A ground segment between two consecutive terrain points. */
  private static class Segment {
    Point start;
    Point end;
    public Segment(Point s, Point e)
    {
      start = s;
      end   = e;
    }
    public Point start() { return start; }
    public Point end() { return end; }

    /** Used internally to test whether a point is underground. */
    boolean intersects(LanderWorld.Segment s)
    {
      Point v             = s.end().minus(s.start());
      double cross        = end.cross(v);
      if (cross == 0) {
        return false;
      }
      double f1 = s.start().minus(start).cross(v) / cross;
      double f2 = s.start().minus(start).cross(end) / cross;
      return f1 >= 0 && f2 >= 0 && f2 <= 1;
    }
  }
  /** Small numeric helpers */
  static double clamp(double min, double max, double value) { return value < min ? min : Math.min(value, max); }
  static int clamp(int min, int max, int value) { return value < min ? min : Math.min(value, max); }
  static Point radianToVector(double angle) { return new Point(Math.cos(angle), Math.sin(angle)); }
  static double gameAngleToRadian(double angle) { return (angle + 90) * Math.PI / 180; }
  // End of the helpers

  public enum State { FLYING, LANDED, CRASHED, OUT }

  private static final Point GRAVITY = new Point(0, -1).times(3.711);

  int width;
  int height;
  Point[] ground;
  Point position;
  Point speed;
  /** Angle in degrees, 0 points north, 90 points west. */
  double angle;
  int thrust;
  int fuel;
  State state = State.FLYING;

  double desiredAngle;
  int desiredThrust;

  public LanderWorld(String name, int width, int height, List<Point> ground, Point position, Point speed, double angle, int thrust, int fuel)
  {
    super(name);
    this.width    = width;
    this.height   = height;
    this.ground   = new Point[ground.size()];
    for (int i = 0; i < ground.size(); i++)
      this.ground[i] = ground.get(i);
    this.position = position;
    this.speed    = speed;
    this.angle    = angle;
    this.thrust   = thrust;
    this.fuel     = fuel;
    setDelay(10);
    addEntity(new LanderEntity());
  }

  /** Copy constructor, required by World.copy() (found by reflection). */
  public LanderWorld(LanderWorld world)
  {
    super(world.getName());
    reset(world);
  }

  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon("img/world_lander.png"); }

  /** Returns true if the lander landed successfully. */
  @Override public boolean winning(World target) { return state == State.LANDED; }

  @Override public String diffTo(World world) { return null; }

  @Override public void reset(World initialWorld)
  {
    LanderWorld iw = (LanderWorld)initialWorld;
    width          = iw.width;
    height         = iw.height;
    ground         = iw.ground;
    position       = iw.position;
    speed          = iw.speed;
    angle          = iw.angle;
    thrust         = iw.thrust;
    fuel           = iw.fuel;
    state          = iw.state;
    desiredAngle   = angle;
    desiredThrust  = thrust;
    super.reset(initialWorld);
  }

  @Override public WorldView getView() { return new LanderWorldView(this); }

  @Override public String toString() { return "java lander world"; }

  // simulation
  double angleRadian() { return gameAngleToRadian(angle); }

  private List<Segment> groundSegments()
  {
    List<Segment> segments = new ArrayList<>();
    for (int i = 0; i + 1 < ground.length; i++) {
      segments.add(new Segment(ground[i], ground[i + 1]));
    }
    return segments;
  }

  private List<Segment> flatSegments()
  {
    List<Segment> flat = new ArrayList<>();
    for (Segment s : groundSegments()) {
      if (s.start().y() == s.end().y()) {
        flat.add(s);
      }
    }
    return flat;
  }

  private boolean touchesSomeFlatSegment(Point p)
  {
    for (Segment s : flatSegments()) {
      if (p.x() > s.start().x() && p.x() < s.end().x() && p.y() - s.start().y() < 1) {
        return true;
      }
    }
    return false;
  }

  private boolean isUnderground(Point p)
  {
    Segment ray       = new Segment(p, new Point(0, 1));
    int crossingCount = 0;
    for (Segment s : groundSegments()) {
      if (ray.intersects(s)) {
        crossingCount++;
      }
    }
    return crossingCount % 2 == 1;
  }

  void simulate(double dt)
  {
    if (state != State.FLYING) {
      return;
    }

    angle       = clamp(Math.max(-90.0, angle - 5), Math.min(90.0, angle + 5), desiredAngle);
    thrust      = Math.min(clamp(Math.max(0, thrust - 1), Math.min(5, thrust + 1), desiredThrust), fuel);
    Point force = radianToVector(angleRadian()).times(thrust).plus(GRAVITY);
    position    = position.plus(speed.times(dt));
    speed       = speed.plus(force.times(dt));
    fuel        = Math.max(fuel - thrust, 0);

    boolean underground = isUnderground(position);
    boolean goodConfig  = Math.abs(speed.y()) <= 10 && Math.abs(speed.x()) <= 5 && (angleRadian() - Math.PI / 2) < 1e-2;
    boolean touchesFlat = touchesSomeFlatSegment(position);
    boolean outOfWorldX = position.x() < 0 || position.x() > width;
    boolean outOfWorldY = position.y() < 0 || position.y() > height;
    boolean outOfWorld  = outOfWorldX || outOfWorldY;

    if (underground) {
      state = (goodConfig && touchesFlat) ? State.LANDED : State.CRASHED;
    } else {
      state = outOfWorld ? State.OUT : State.FLYING;
    }
  }
}
