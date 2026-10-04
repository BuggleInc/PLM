package plm.core.model.lesson;

import java.util.Date;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;

/**
 * Class representing the result of pressing on the "run" button. Either a compilation error, or a percentage of
 * passed/failed tests + a descriptive message
 */
public class RunOutcome {

  public static enum kind { COMPILE, FAIL, PASS }
  ;
  public kind outcome = kind.PASS;

  public String compilationError;
  public String executionError = "";
  public int passedTests, totalTests = 0;
  public Date date                    = new Date();
  public ProgrammingLanguage language = Game.getInstance().getProgrammingLanguage();

  /* The feedback from the student in the ExecisePassedDialog */
  public String feedbackDifficulty;
  public String feedbackInterest;
  public String feedback;

  public static RunOutcome newCompilationError(String message)
  {
    RunOutcome ep = new RunOutcome();

    ep.compilationError = message;
    ep.passedTests      = -1;
    ep.totalTests       = -1;
    if (ep.compilationError == null)
      ep.compilationError = "Unknown compilation error";
    ep.outcome = RunOutcome.kind.COMPILE;

    return ep;
  }
  public void setCompilationError(String msg)
  {
    outcome          = RunOutcome.kind.COMPILE;
    compilationError = msg;
    passedTests      = -1;
  }
  public void setExecutionError(String msg)
  {
    outcome        = RunOutcome.kind.FAIL;
    executionError = msg;
  }
}
