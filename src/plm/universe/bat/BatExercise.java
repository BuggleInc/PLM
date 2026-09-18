package plm.universe.bat;

import java.util.List;
import java.util.concurrent.Future;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public abstract class BatExercise extends ExerciseTemplated {
  public static final boolean INVISIBLE = false;
  public static final boolean VISIBLE   = true;

  public BatExercise(Lesson lesson) { super(lesson); }

  @Override public void runDemo(List<Future<?>> runnerVect, ProgrammingLanguage lang) { /* No demo in bat exercises */ }
}
