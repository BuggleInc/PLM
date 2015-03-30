package lessons.lander

import plm.core.model.Game
import plm.core.model.lesson.Lesson
import lessons.lander.lvl1_lander_101.Lander101
import lessons.lander.lvl2_locate_landing_zone.LocateLandingZone
import lessons.lander.lvl3_fly_the_lander.FlyTheLander

class Main(game: Game) extends Lesson(game) {
  override def loadExercises = {
    addExercise(new Lander101(game, this));
    addExercise(new LocateLandingZone(game, this));
    addExercise(new FlyTheLander(game, this));
  }
}
