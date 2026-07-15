package lessons.lander.lvl2_locate_landing_zone;

import static lessons.lander.universe.Configurations.CHALLENGING_TERRAIN_SIMPLE_CONFIG;
import static lessons.lander.universe.Configurations.SIMPLE_TERRAIN_TRIVIAL_CONFIG;

import lessons.lander.universe.LanderWorld;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class LocateLandingZone extends ExerciseTemplated {
  public LocateLandingZone(Lesson lesson)
  {
    super(lesson, null);
    tabName = "Lander";
    setup(new LanderWorld[] {CHALLENGING_TERRAIN_SIMPLE_CONFIG, SIMPLE_TERRAIN_TRIVIAL_CONFIG});
  }
}
