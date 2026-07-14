package lessons.sort.baseball;

import java.util.Arrays;
import lessons.sort.baseball.universe.BaseballWorld;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;

public class SelectBaseball extends ExerciseTemplated {

  public SelectBaseball(Lesson lesson)
  {
    super(lesson);

    World[] myWorlds = {
        new BaseballWorld("Almost", 4, 2, BaseballWorld.MIX_ALMOST_SORTED),
        new BaseballWorld("5 bases", 5, 2),
        new BaseballWorld("6 bases", 6, 2),
        new BaseballWorld("7 bases", 7, 2),
        new BaseballWorld("8 bases", 8, 2),
        new BaseballWorld("9 bases", 9, 2),
        new BaseballWorld("10 bases", 10, 2),
        new BaseballWorld("15 bases", 15, 2),
    };

    myWorlds = Arrays.stream(myWorlds).map(s -> s.replaceEntities(SelectBaseballEntity::new)).toList().toArray(new World[0]);
    setup(myWorlds);
  }
}
