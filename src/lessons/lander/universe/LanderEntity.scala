package lessons.lander.universe

import plm.universe.Entity
import scala.collection.JavaConversions.asJavaIterable

class LanderEntity extends Entity {

  private def landerWorld = getWorld().asInstanceOf[DelegatingLanderWorld].realWorld 

  override def run() = {
    init()
    while (landerWorld.state == LanderWorld.State.FLYING) {
      step()
      landerWorld.simulate(0.1)
      stepUI()
    }
  }

  // methods to be overriden by the player
  def init(): Unit = ()
  def step(): Unit = ()

  // query terrain
  def getGround(): java.lang.Iterable[Point] = landerWorld.ground

  // query lander state
  def getX(): Double = landerWorld.lander.position.x
  def getY(): Double = landerWorld.lander.position.y
  def getSpeedX(): Double = landerWorld.lander.speed.x
  def getSpeedY(): Double = landerWorld.lander.speed.y
  def getAngle(): Double = landerWorld.lander.gameAngle
  def getThrust(): Int = landerWorld.lander.thrust

  def setDesiredAngle(desiredAngle: Double): Unit = {
    landerWorld.lander.gameAngle = desiredAngle
  }

  def setDesiredThrust(desiredThrust: Int): Unit = {
    landerWorld.lander.thrust = desiredThrust
  }
}
