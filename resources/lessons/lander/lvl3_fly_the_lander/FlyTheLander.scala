package lessons.lander.lvl3_fly_the_lander

import plm.core.model.lesson.ExerciseTemplated
import lessons.lander.universe.DelegatingLanderWorld
import lessons.lander.universe.Point
import lessons.lander.universe.LanderEntity
import lessons.lander.universe.Configurations._
import Math.PI
import scala.collection.JavaConversions._
import lessons.lander.universe.LanderWorld
import plm.universe.World

class FlyTheLander() extends ExerciseTemplated("FlyTheLander") {
  tabName = "Lander"
  setup(Array(
      SIMPLE_TERRAIN_CHALLENGING_CONFIG,
      CHALLENGING_TERRAIN_SIMPLE_CONFIG,
      SIMPLE_TERRAIN_TRIVIAL_CONFIG))
}
