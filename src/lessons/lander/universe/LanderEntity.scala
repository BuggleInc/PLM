package lessons.lander.universe

import plm.universe.Entity
import scala.collection.JavaConversions.asJavaIterable

class LanderEntity extends Entity {

  private def landerWorld = getWorld().asInstanceOf[DelegatingLanderWorld].realWorld

  override def command(command: String, out: java.io.BufferedWriter){
    
  }
  
  override def run() = {
    init()
    while (landerWorld.state == LanderWorld.State.FLYING) {
      step()
      landerWorld.simulate(0.1)
      stepUI()
    }
  }

  // methods to be overridden by the player
  def init(): Unit = ()
  def step(): Unit = ()

  // query terrain2
  def getGround(): java.lang.Iterable[Point] = landerWorld.ground

  // query lander state
  def getX(): Double = landerWorld.position.x
  def getY(): Double = landerWorld.position.y
  def getSpeedX(): Double = landerWorld.speed.x
  def getSpeedY(): Double = landerWorld.speed.y
  def getAngle(): Double = landerWorld.angle
  def getThrust(): Int = landerWorld.thrust
  def getFuel(): Int = landerWorld.fuel

  def setDesiredAngle(desiredAngle: Double) {
    landerWorld.desiredAngle = desiredAngle
  }
  def setDesiredThrust(desiredThrust: Int) {
    landerWorld.desiredThrust = desiredThrust
  }
}
