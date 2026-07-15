package lessons.lander.lvl3_fly_the_lander;

import static lessons.lander.universe.Configurations.CHALLENGING_TERRAIN_SIMPLE_CONFIG;
import static lessons.lander.universe.Configurations.SIMPLE_TERRAIN_CHALLENGING_CONFIG;
import static lessons.lander.universe.Configurations.SIMPLE_TERRAIN_TRIVIAL_CONFIG;

import lessons.lander.universe.LanderWorld;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class FlyTheLander extends ExerciseTemplated {
  public FlyTheLander(Lesson lesson)
  {
    super(lesson, null);
    tabName = "Lander";
    setup(new LanderWorld[] {SIMPLE_TERRAIN_CHALLENGING_CONFIG, CHALLENGING_TERRAIN_SIMPLE_CONFIG, SIMPLE_TERRAIN_TRIVIAL_CONFIG});
  }
}
