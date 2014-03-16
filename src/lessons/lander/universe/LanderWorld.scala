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

case class Segment(start: Point, end: Point)

case class Ray(origin: Point, direction: Point) {
  def intersects(s: Segment): Boolean = {
    val v = s.end - s.start
    val cross = direction.cross(v);
    if (cross == 0) {
      false
    } else {
      val f1 = (s.start - origin).cross(v) / cross
      val f2 = (s.start - origin).cross(direction) / cross
      f1 >= 0 && f2 >= 0 && f2 <= 1
    }
  }
}

/**
 * @param angle angle in radians
 */
class Lander(var position: Point, var speed: Point, private var _angle: Double,
    private var _thrust: Int) {

  clampAngle()
  clampThrust()

  def thrust = _thrust
  def thrust_=(value: Int) = {
    _thrust = value
    clampThrust()
  }

  def angle = _angle
  def angle_=(value: Double) = {
    _angle = value
    clampAngle()
  }

  def gameAngle = (angle * 180 / PI) - 90
  def gameAngle_=(value: Double) = {
    angle = (value + 90) * PI / 180
  }

  def copy = new Lander(position, speed, angle, thrust)

  private def clampAngle() = {
    if (_angle > 90) _angle = 90
    if (_angle < -90) _angle = -90
  }

  private def clampThrust() = {
    if (_thrust > 5) _thrust = 5
    if (_thrust < 0) _thrust = 0
  }
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

  parent.setDelay(10)
  parent.addEntity(new LanderEntity())

  // "inherited" methods

  def getIcon = ResourcesCache.getIcon("img/world_lander.png");

  def setupBindings(lang: ProgrammingLanguage, engine: ScriptEngine): Unit = ()

  /** Returns true if both worlds have same state. */
  override def equals(obj: Any): Boolean = {
    obj match {
      case world: DelegatingLanderWorld => state == world.realWorld.state
      case _ => false
    }
  }

  def diffTo(world: World): String = null

  def reset(initialWorld: LanderWorld): Unit = {
    width = initialWorld.width
    height = initialWorld.height
    ground = initialWorld.ground
    lander = initialWorld.lander.copy
    state = initialWorld.state
  }

  def getView() = new LanderWorldView(LanderWorld.this)

  override def toString() = "scala lander world"

  // simulation

  private def groundSegments = {
    if (ground.isEmpty) List()
    else (ground, ground.tail).zipped map (Segment(_, _))
  }

  private def flatSegments = groundSegments.filter((s) => s.start.y == s.end.y)

  private def touchesSomeFlatSegment(p: Point) = flatSegments
      .find((s) => p.x > s.start.x && p.x < s.end.x && p.y - s.start.y < 1)
      .isDefined

  private def isUnderground(p: Point) =
    groundSegments.filter(new Ray(p, Point(0, 1)).intersects(_)).length % 2 == 1

  def simulate(dt: Double) {
    if (state == FLYING) {
      val force = LanderWorld.angleToVector(lander.angle) * lander.thrust + LanderWorld.GRAVITY
      lander.position = lander.position + lander.speed * dt
      lander.speed = lander.speed + force * dt

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