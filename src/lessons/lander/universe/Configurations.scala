package lessons.lander.universe

import plm.universe.World

object Configurations {
  import NumUtil._
  
  // helper creation functions
  
  private case class Terrain(width: Int, height: Int, ground: List[Point])
  private case class Lander(position: Point, speed: Point, angle: Double, thurst: Int)
   
  private def makeWorld(name: String, terrain: Terrain, lander: Lander) =
    new DelegatingLanderWorld(name, terrain.width, terrain.height, terrain.ground, lander.position,
        lander.speed, lander.angle, lander.thurst)
  
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
  
  // worlds
          
  val SIMPLE_TERRAIN_SIMPLE_CONFIG : World = makeWorld(
      name = "Simple Terrain, Simple Configuration",
      terrain = SIMPLE_TERRAIN,
      lander = Lander(Point(1200, 700), Point(0, 0), 0.0, 0))
      
  val SIMPLE_TERRAIN_CHALLENGING_CONFIG : World = makeWorld(
      name = "Simple Terrain, Challlenging Configuration",
      terrain = SIMPLE_TERRAIN,
      lander = Lander(Point(500, 500), angleToSpeed(-20, 20), -20, 3))
      
  val SIMPLE_TERRAIN_HARD_CONFIG : World = makeWorld(
      name = "Simple Terrain, Hard Configuration",
      terrain = SIMPLE_TERRAIN,
      lander = Lander(Point(1900, 900), angleToSpeed(90, 80), 90, 4))
}