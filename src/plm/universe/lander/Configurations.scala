package plm.universe.lander

import plm.universe.World
import plm.core.model.Game

object Configurations {
  import NumUtil._

  // helper creation functions

  private case class Terrain(width: Int, height: Int, ground: List[Point])

  private def makeWorld(game: Game, name: String, terrain: Terrain, position: Point, speed: Point,
      angle: Double, thrust: Int, fuel: Int) =
    new DelegatingLanderWorld(game, name, terrain.width, terrain.height, terrain.ground, position, speed,
        angle, thrust, fuel)

  // helper geometric functions

  private def angleToSpeed(angle: Double, speed: Double) =
    radianToVector(gameAngleToRadian(angle)) * speed

  // terrains

  private val SIMPLE_TERRAIN = Terrain(
      width = 2000,
      height = 1000,
      ground = List(Point(0, 100), Point(125, 414), Point(205, 271),
          Point(348, 597), Point(460, 257), Point(534, 438), Point(637, 160),
          Point(760, 371), Point(854, 200), Point(1468, 200), Point(1585, 440),
          Point(1682, 280), Point(1845, 668), Point(2000, 294)))

  private val CHALLENGING_TERRAIN = Terrain(
      width = 2000,
      height = 1000,
      ground =  List(Point(0,260), Point(37,160), Point(160,371), Point(254,200),
          Point(430,200), Point(535,394), Point(639,300), Point(780,300), Point(890,440),
          Point(1082,280), Point(1245,668), Point(1400,294), Point(1580, 410), Point(1730, 360),
          Point(1870, 560), Point(2000, 400)))

  // worlds

  def makeWorld(game: Game, config: String): DelegatingLanderWorld = {
    config match {
      case "SIMPLE_TERRAIN_TRIVIAL_CONFIG" =>
        makeWorld(
          game = game,
          name = "Simple Terrain, Simple Configuration",
          terrain = SIMPLE_TERRAIN,
          position = Point(1200, 700),
          speed = Point(0, 0),
          angle = 0.0,
          thrust = 0,
          fuel = 3000)
      case "CHALLENGING_TERRAIN_SIMPLE_CONFIG" =>
        makeWorld(
          game = game,
          name = "Challenging Terrain, Simple Configuration",
          terrain = CHALLENGING_TERRAIN,
          position = Point(530, 600),
          speed = Point(0, 10),
          angle = 0,
          thrust = 4,
          fuel = 3000)
      case "SIMPLE_TERRAIN_CHALLENGING_CONFIG" =>
        makeWorld(
          game = game,
          name = "Simple Terrain, Challenging Configuration",
          terrain = SIMPLE_TERRAIN,
          position = Point(500, 500),
          speed = angleToSpeed(-20, 20),
          angle = -20,
          thrust = 3,
          fuel = 3000)
      case "SIMPLE_TERRAIN_HARD_CONFIG" =>
        makeWorld(
          game = game,
          name = "Simple Terrain, Hard Configuration",
          terrain = SIMPLE_TERRAIN,
          position = Point(1900, 900),
          speed = angleToSpeed(90, 80),
          angle = 90,
          thrust = 4,
          fuel = 3000)
    }
  }
          
  /*
  val SIMPLE_TERRAIN_TRIVIAL_CONFIG = makeWorld(
      name = "Simple Terrain, Simple Configuration",
      terrain = SIMPLE_TERRAIN,
      position = Point(1200, 700),
      speed = Point(0, 0),
      angle = 0.0,
      thrust = 0,
      fuel = 3000)

  val CHALLENGING_TERRAIN_SIMPLE_CONFIG = makeWorld(
      name = "Challenging Terrain, Simple Configuration",
      terrain = CHALLENGING_TERRAIN,
      position = Point(530, 600),
      speed = Point(0, 10),
      angle = 0,
      thrust = 4,
      fuel = 3000)

  val SIMPLE_TERRAIN_CHALLENGING_CONFIG = makeWorld(
      name = "Simple Terrain, Challenging Configuration",
      terrain = SIMPLE_TERRAIN,
      position = Point(500, 500),
      speed = angleToSpeed(-20, 20),
      angle = -20,
      thrust = 3,
      fuel = 3000)

  val SIMPLE_TERRAIN_HARD_CONFIG = makeWorld(
      name = "Simple Terrain, Hard Configuration",
      terrain = SIMPLE_TERRAIN,
      position = Point(1900, 900),
      speed = angleToSpeed(90, 80),
      angle = 90,
      thrust = 4,
      fuel = 3000)
   */
}