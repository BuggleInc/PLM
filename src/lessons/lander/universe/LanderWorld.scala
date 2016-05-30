package lessons.lander.universe

import java.lang.Math.PI

import NumUtil.clamp
import NumUtil.gameAngleToRadian
import NumUtil.radianToVector
import javax.script.ScriptEngine
import plm.core.lang.ProgrammingLanguage
import plm.core.model.Game
import plm.universe.World

object LanderWorld {
  object State extends Enumeration {
    val FLYING, LANDED, CRASHED, OUT = Value
  }

  private val GRAVITY = Point(0, -1) * 3.711;
}

class LanderWorld(val parent: DelegatingLanderWorld) {

  import LanderWorld.State._
  import NumUtil._

  var width: Int = 0
  var height: Int = 0
  var ground: List[Point] = null
  var position: Point = null
  var speed: Point = null
  /** Angle in degrees, 0 points north, 90 points west. */
  var angle: Double = 0
  var thrust: Int = 0
  var fuel: Int = 0
  var state = FLYING

  var desiredAngle: Double = 0
  var desiredThrust: Int = 0

  parent.setDelay(10)
  parent.addEntity(new LanderEntity())

  // "inherited" methods

  def setupBindings(lang: ProgrammingLanguage, engine: ScriptEngine):Unit= {
  		if (lang.equals(Game.PYTHON)) {
  			engine.put("Segment", Segment.getClass())
			engine.eval(
			    "def isFlying():\n"+
			    "  return entity.isFlying()\n"+
			    "def simulateStep():\n"+
			    "  entity.simulateStep()\n"+
			    "def getGround():\n"+
			    "  return [ (elm.x(), elm.y()) for elm in entity.getGround() ]\n"+
			    "def getX():\n"+
			    "  return entity.getX()\n"+
			    "def getY():\n"+
			    "  return entity.getY()\n"+
			    "def getSpeedX():\n"+
			    "  return entity.getSpeedX()\n"+
			    "def getSpeedY():\n"+
			    "  return entity.getSpeedY()\n"+
			    "def getAngle():\n"+
			    "  return entity.getAngle()\n"+
			    "def setDesiredAngle(a):\n"+
			    "  entity.setDesiredAngle(a)\n"+
			    "def getThrust():\n"+
			    "  return entity.getThrust()\n"+
			    "def setDesiredThrust(t):\n"+
			    "  entity.setDesiredThrust(t)\n"+
			    "def getFuel():\n"+
			    "  return entity.getFuel()\n"+
			    "")
  		} else {
  			throw new RuntimeException("No binding of LanderWorld for "+lang)
  		}
  }

  /** Returns true if both worlds have same name and same state. */
  def winning(target: World): Boolean = state == LANDED

  def diffTo(world: World): String = null

  def reset(initialWorld: LanderWorld): Unit = {
    width = initialWorld.width
    height = initialWorld.height
    ground = initialWorld.ground
    position = initialWorld.position
    speed = initialWorld.speed
    angle = initialWorld.angle
    thrust = initialWorld.thrust
    fuel = initialWorld.fuel
    state = initialWorld.state
    desiredAngle = angle
    desiredThrust = thrust
  }

  override def toString() = "scala lander world"

  // simulation

  def angleRadian = gameAngleToRadian(angle)

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

  def simulate(dt: Double):Unit = {
    if (state == FLYING) {
      angle = clamp(-90.0 max (angle - 5), 90.0 min (angle + 5), desiredAngle)
      thrust = clamp(0 max (thrust - 1), 5 min (thrust + 1), desiredThrust) min fuel
      val force = radianToVector(angleRadian) * thrust + LanderWorld.GRAVITY
      position = position + speed * dt
      speed = speed + force * dt
      fuel = (fuel - thrust) max 0

      lazy val underground = isUnderground(position)
      lazy val goodConfig = speed.y.abs <= 10 && speed.x.abs <= 5 && (angleRadian - PI/2) < 1e-2
      lazy val touchesFlat = touchesSomeFlatSegment(position)
      lazy val outOfWorldX = position.x < 0 || position.x > width
      lazy val outOfWorldY = position.y < 0 || position.y > height
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
