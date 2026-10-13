package plm.core.model.tracking;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.RunOutcome;

public interface ProgressTrackerListener {
  public void executed(Exercise exo, RunOutcome outcome);

  public void switched(Exercise exo);

  public void reverted(Exercise exo);

  public void heartbeat();

  public String join();

  public void leave();

  public void readTip(String id, String mission, ProgrammingLanguage lang);
}
