package plm.core.model.lesson;

import java.util.Date;
import java.util.List;
import plm.core.lang.CompilerMessage;
import plm.core.lang.ProgrammingLanguage;

/**
 * Class representing the result of pressing on the "run" button. Either a compilation error, or a percentage of
 * passed/failed tests + a descriptive message
 */
public class RunOutcome {

  public static enum kind { COMPILE, FAIL, PASS }
  /** FAIL until an execution says otherwise, so that an outcome of something that did not run is never taken for a success */
  public kind outcome = kind.FAIL;

  public String compilationError;
  /** The errors or warnings of the compiler, located in the editor of the student (empty for the correction) */
  public List<CompilerMessage> compilerMessages = List.of();
  public String executionError = "";
  public int passedTests, totalTests = 0;
  public Date date                    = new Date();
  public ProgrammingLanguage language;

  public RunOutcome(ProgrammingLanguage language) { this.language = language; }

  public boolean isPassed() { return outcome == kind.PASS; }
  public boolean isCompilationError() { return outcome == kind.COMPILE; }

  /* The feedback from the student in the ExecisePassedDialog */
  public String feedbackDifficulty;
  public String feedbackInterest;
  public String feedback;

  /** The outcome of a compilation that failed with that message, whose errors are also located in the editor as compilerMessages */
  public static RunOutcome newCompilationError(ProgrammingLanguage language, String message, List<CompilerMessage> compilerMessages)
  {
    RunOutcome ep = new RunOutcome(language);

    ep.compilationError = message;
    ep.compilerMessages = compilerMessages;
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
