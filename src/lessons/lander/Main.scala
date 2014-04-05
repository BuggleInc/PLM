package lessons.lander

import plm.core.model.lesson.Lesson
import lessons.lander.lvl1_lander_101.Lander101
import lessons.lander.lvl2_locate_landing_zone.LocateLandingZone
import lessons.lander.lvl3_fly_the_lander.FlyTheLander

class Main extends Lesson {
  override def loadExercises = {
    addExercise(new Lander101(this));
    addExercise(new LocateLandingZone(this));
    addExercise(new FlyTheLander(this));
  }
}
