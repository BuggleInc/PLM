package lessons.lander.universe

import javax.script.ScriptEngine
import javax.swing.ImageIcon

import plm.universe.World
import plm.core.model.ProgrammingLanguage
import plm.core.ui.ResourcesCache

import Math.PI
import Math.cos
import Math.sin

case class Point(x: Double, y: Double) {
  def +(p: Point): Point = new Point(x + p.x, y + p.y)
  def -(p: Point): Point = new Point(x - p.x, y - p.y)
  def *(l: Double): Point = new Point(x * l, y * l)
  def /(l: Double): Point = new Point(x / l, y / l)
  def unary_- = this * -1

  def length: Double = Math.sqrt(x * x + y * y)
  def normed = this / length
  def dot(p: Point) = x * p.x + y * p.y
  def cross(p: Point) = x * p.y - y * p.x
}

case class Segment(p1: Point, p2: Point) {
  def intersects(s: Segment): Boolean = {
    val v1 = p2 - p1
    val v2 = s.p2 - s.p1
    val v1v2 = v1.cross(v2);
    if (v1v2 == 0) {
      false
    } else {
      val f1 = (s.p1 - p1).cross(v2) / v1v2
      val f2 = (s.p1 - p1).cross(v1) / v1v2
      f1 >= 0 && f1 <= 1 && f2 >= 0 && f2 <= 1
    }
  }
}

case class Ray(origin: Point, direction: Point) {
  def intersects(s: Segment): Boolean = {
    val v = s.p2 - s.p1
    val cross = direction.cross(v);
    if (cross == 0) {
      false
    } else {
      val f1 = (s.p1 - origin).cross(v) / cross
      val f2 = (s.p1 - origin).cross(direction) / cross
      f1 >= 0 && f2 >= 0 && f2 <= 1
    }
  }
}

/**
 * @param angle angle in radians
 */
case class Lander(position: Point, speed: Point, angle: Double, thrust: Int) {
  require(thrust >= 0 && thrust < 5)
}

object LanderWorld {
  object State extends Enumeration {
    val FLYING, LANDED, CRASHED, OUT = Value
  }

  private val GRAVITY = Point(0, -1) * 3.711;
  private def angleToVector(angle: Double) = Point(cos(angle), sin(angle))
}

class LanderWorld(val parent: DelegatingLanderWorld) {

  import LanderWorld.State._

  var width: Int = 0
  var height: Int = 0
  var ground: List[Point] = null
  var lander: Lander = null
  var state = FLYING

  parent.setDelay(5)
  parent.addEntity(new LanderEntity())

  // "inherited" methods

  def getIcon = ResourcesCache.getIcon("img/world_lander.png");

  def setupBindings(lang: ProgrammingLanguage, engine: ScriptEngine): Unit = ()

  def diffTo(world: World): String = null

  def reset(initialWorld: LanderWorld): Unit = {
    width = initialWorld.width
    height = initialWorld.height
    ground = initialWorld.ground
    lander = initialWorld.lander
    state = initialWorld.state
  }

  def getView() = new LanderWorldView(LanderWorld.this)

  override def toString() = "scala lander world"

  // simulation

  private def groundSegments = {
    if (ground.isEmpty) List()
    else (ground, ground.tail).zipped map (Segment(_, _))
  }

  private def flatSegments = groundSegments.filter((s) => s.p1.y == s.p2.y)

  private def touchesSomeFlatSegment(p: Point) = flatSegments
      .find((s) => p.x > s.p1.x && p.x < s.p2.x && p.y - s.p1.y < 1)
      .isDefined

  private def isUnderground(p: Point) =
    groundSegments.filter(new Ray(p, Point(0, 1)).intersects(_)).length % 2 == 1

  def simulate(dt: Double) {
    if (state == FLYING) {
      val force = LanderWorld.angleToVector(lander.angle) * lander.thrust + LanderWorld.GRAVITY
      lander = Lander(
          lander.position + lander.speed * dt,
          lander.speed + force * dt,
          lander.angle,
          lander.thrust)

      lazy val underground = isUnderground(lander.position)
      lazy val goodConfig = lander.speed.length < 10 && (lander.angle - PI/2) < 1e-2
      lazy val touchesFlat = touchesSomeFlatSegment(lander.position)
      lazy val outOfWorldX = lander.position.x < 0 || lander.position.x > width
      lazy val outOfWorldY = lander.position.y < 0 || lander.position.y > height
      lazy val outOfWorld = outOfWorldX || outOfWorldY

      state =
        if (underground) {
          if (goodConfig && touchesFlat) LANDED
          else CRASHED
        } else {
          if (outOfWorld) OUT
          else FLYING
        }
    }
  }
}