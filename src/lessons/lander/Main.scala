package lessons.lander

import plm.core.model.lesson.Lesson
import lessons.lander.full.FullLander

class Main extends Lesson {
  override def loadExercises = {
    addExercise(new FullLander(this));
  }
}