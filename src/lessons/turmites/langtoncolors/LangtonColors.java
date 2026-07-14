package lessons.turmites.langtoncolors;

import java.util.Arrays;
import lessons.turmites.universe.TurmiteWorld;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class LangtonColors extends ExerciseTemplated {
  BuggleWorld createWorld(String rule, int nbSteps, int width, int height, int buggleX, int buggleY)
  {
    return new TurmiteWorld(rule + " (" + nbSteps + " steps)", nbSteps, rule.toCharArray(), width, height, buggleX, buggleY);
  }

  public LangtonColors(Lesson lesson)
  {
    super(lesson);
    tabName = "LangtonsAnt";

    World[] myWorlds = new BuggleWorld[] {
        createWorld("RL", 12001, 100, 70, 66, 23),          createWorld("LLRR", 20001, 30, 30, 15, 15),
        createWorld("LRRRRRLLR", 9001, 25, 24, 11, 12),     createWorld("LLRRRLRLRLLR", 36001, 120, 60, 100, 30),
        createWorld("RRLLLRLLLRRR", 30001, 80, 90, 60, 28),
    };

    myWorlds = Arrays.stream(myWorlds).map(s -> s.replaceEntities(LangtonColorsEntity::new)).toList().toArray(new World[0]);
    setup(myWorlds);
  }
}
