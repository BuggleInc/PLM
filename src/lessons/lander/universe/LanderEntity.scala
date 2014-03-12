package lessons.lander.universe

import plm.universe.Entity

class LanderEntity extends Entity {

  private def landerWorld = getWorld().asInstanceOf[DelegatingLanderWorld].realWorld 

  override def run() = {
    while (landerWorld.state == LanderWorld.State.FLYING) {
      landerWorld.simulate(0.1)
      stepUI()
    }
  }
}