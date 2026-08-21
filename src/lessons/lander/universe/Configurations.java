package lessons.lander.universe;

import java.util.List;
import plm.universe.Point;

public final class Configurations {

  private Configurations() {}

  private record Terrain(int width, int height, List<Point> ground) {}

  // helper creation functions

  private static LanderWorld makeWorld(String name, Terrain terrain, Point position, Point speed, double angle, int thrust, int fuel)
  {
    return new LanderWorld(name, terrain.width(), terrain.height(), terrain.ground(), position, speed, angle, thrust, fuel);
  }

  // helper geometric functions

  private static Point angleToSpeed(double angle, double speed) { return LanderWorld.radianToVector(LanderWorld.gameAngleToRadian(angle)).times(speed); }

  // terrains

  private static final Terrain SIMPLE_TERRAIN =
      new Terrain(2000, 1000,
                  List.of(new Point(0, 100), new Point(125, 414), new Point(205, 271), new Point(348, 597), new Point(460, 257), new Point(534, 438),
                          new Point(637, 160), new Point(760, 371), new Point(854, 200), new Point(1468, 200), new Point(1585, 440), new Point(1682, 280),
                          new Point(1845, 668), new Point(2000, 294)));

  private static final Terrain CHALLENGING_TERRAIN =
      new Terrain(2000, 1000,
                  List.of(new Point(0, 260), new Point(37, 160), new Point(160, 371), new Point(254, 200), new Point(430, 200), new Point(535, 394),
                          new Point(639, 300), new Point(780, 300), new Point(890, 440), new Point(1082, 280), new Point(1245, 668), new Point(1400, 294),
                          new Point(1580, 410), new Point(1730, 360), new Point(1870, 560), new Point(2000, 400)));

  // worlds

  public static final LanderWorld SIMPLE_TERRAIN_TRIVIAL_CONFIG =
      makeWorld("Simple Terrain, Simple Configuration", SIMPLE_TERRAIN, new Point(1200, 700), new Point(0, 0), 0.0, 0, 3000);

  public static final LanderWorld CHALLENGING_TERRAIN_SIMPLE_CONFIG =
      makeWorld("Challenging Terrain, Simple Configuration", CHALLENGING_TERRAIN, new Point(530, 600), new Point(0, 10), 0, 4, 3000);

  public static final LanderWorld SIMPLE_TERRAIN_CHALLENGING_CONFIG =
      makeWorld("Simple Terrain, Challenging Configuration", SIMPLE_TERRAIN, new Point(500, 500), angleToSpeed(-20, 20), -20, 3, 3000);

  public static final LanderWorld SIMPLE_TERRAIN_HARD_CONFIG =
      makeWorld("Simple Terrain, Hard Configuration", SIMPLE_TERRAIN, new Point(1900, 900), angleToSpeed(90, 80), 90, 4, 3000);
}
