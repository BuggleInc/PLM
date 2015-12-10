package lessons.lander.universe

import plm.universe.Entity
import scala.collection.JavaConversions._

class LanderEntity extends Entity {

  private def landerWorld = getWorld().asInstanceOf[DelegatingLanderWorld].realWorld

  override def command(command: String, out: java.io.BufferedWriter):Unit ={
    
  }
  
  override def run():Unit = {
    initialize()
    while (isFlying()) {
      step()
      simulateStep()
    }
  }

  // methods to be overridden by the player
  def initialize(): Unit = ()
  def step(): Unit = ()

  // query terrain2
  def getGround(): java.util.List[Point] = landerWorld.ground

  // query lander state
  def getX(): Double = landerWorld.position.x
  def getY(): Double = landerWorld.position.y
  def getSpeedX(): Double = landerWorld.speed.x
  def getSpeedY(): Double = landerWorld.speed.y
  def getAngle(): Double = landerWorld.angle
  def getThrust(): Int = landerWorld.thrust
  def getFuel(): Int = landerWorld.fuel

  def setDesiredAngle(desiredAngle: Double):Unit = {
    landerWorld.desiredAngle = desiredAngle
  }
  def setDesiredThrust(desiredThrust: Int):Unit = {
    landerWorld.desiredThrust = desiredThrust
  }
  
  /* Internal commands used by the python entities to simulate the above run method */
  def isFlying(): Boolean = (landerWorld.state == LanderWorld.State.FLYING)
  def simulateStep():Unit = {
    landerWorld.simulate(0.1)
    stepUI()
  }
  
  /* BINDINGS TRANSLATION: French */
  def getSol() = getGround()
  def getVitesseX(): Double = getSpeedX()
  def getVitesseY(): Double = getSpeedY()
  def getPoussee(): Int = getThrust()
  def setAngleDesire(desiredAngle: Double) = setDesiredAngle(desiredAngle) 
  def setPousseeDesiree(desiredThrust: Int) = setDesiredThrust(desiredThrust)
}
