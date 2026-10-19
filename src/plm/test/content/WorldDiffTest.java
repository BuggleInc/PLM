package plm.test.content;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Lesson;
import plm.universe.World;

/**
 * The messages explaining a failure: when the initial world of an exercise differs from its answer, diffTo() says how. Only the
 * initial worlds are copied: copy() gives a world in its initial state, so the copy of an answer loses what the correction did.
 */
public class WorldDiffTest {

  @ParameterizedTest @MethodSource("plm.test.content.ExoTest#exercises") public void differencesAreExplained(Lesson lesson, Exercise exo)
  {
    Assumptions.assumeFalse(exo.getId().startsWith("lander."), "The lander worlds are judged by winning(), not by equality");
    ProgrammingLanguage java = Game.getInstance().programmingLanguageManager.JAVA;
    List<World> initial      = exo.getWorlds(WorldKind.INITIAL);
    List<World> answer       = exo.getWorlds(WorldKind.ANSWER);
    for (int i = 0; i < answer.size(); i++) {
      World expected = answer.get(i);
      Assertions.assertEquals(initial.get(i), initial.get(i).copy(), exo.getId() + ": a copy of the initial world differs from it");
      if (!initial.get(i).equals(expected))
        Assertions.assertFalse(initial.get(i).diffTo(expected, java).isBlank(),
                               exo.getId() + ": the initial world differs from the answer, but diffTo() does not say how");
    }
  }
}
