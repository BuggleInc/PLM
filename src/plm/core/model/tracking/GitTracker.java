package plm.core.model.tracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.json.simple.JSONObject;
import plm.core.UserSwitchesListener;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.User;
import plm.core.model.Users;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.RunOutcome;
import plm.core.utils.FileUtils;

public class GitTracker implements ProgressTrackerListener, UserSwitchesListener {

  private File plmDir;
  private GitUtils gitUtils;

  private ProgressMonitor progress = NullProgressMonitor.INSTANCE; // new TextProgressMonitor(new PrintWriter(System.out));

  private String repoUrl = Game.getProperty("plm.git.server.url"); // https://github.com/mquinson/PLM-data.git
  private File repoDir;

  /** The outcome of the last execution of each exercise, reported by the events that are not executions */
  private final Map<Exercise, RunOutcome> lastOutcomes = new HashMap<>();

  public GitTracker(File path, Users users) throws IOException, GitAPIException
  {
    this.plmDir = path;

    gitUtils = new GitUtils();

    users.addUserSwitchesListener(this);
    userHasChanged(users.getCurrentUser());
  }

  /**
   * Initialize locally the git repo for that user
   */
  @Override public void userHasChanged(User newUser)
  {
    String userUUID   = newUser.getUserUUIDasString();
    String userBranch = "PLM" + GitUtils.sha1(userUUID);

    try {
      repoDir     = new File(plmDir.getAbsolutePath() + System.getProperty("file.separator") + userUUID);
      File gitDir = new File(repoDir.getAbsolutePath() + System.getProperty("file.separator") + ".git");

      if (gitDir.exists()) { // Testing repoDir is not enough because the directory may have been created by another tracker but not initialized for git
        System.out.println(Game.i18n.tr("Reusing the disk session located at {0}.", repoDir));
      } else {
        System.out.println(Game.i18n.tr("Initialize a new disk session at {0}.", repoDir));
        gitUtils.initLocalRepository(repoDir);
        gitUtils.setUpRepoConfig(repoUrl, userBranch);
        // We must create an initial commit before creating a specific branch for the user
        gitUtils.createInitialCommit();
      }

      gitUtils.openRepo(repoDir);
      if (gitUtils.getRepoRef(userBranch) != null) {
        gitUtils.checkoutUserBranch(userBranch);
      } else {
        gitUtils.createLocalUserBranch(userBranch);
      }

      // try to get the branch as stored remotely
      if (gitUtils.fetchBranchFromRemoteBranch(userBranch)) {
        gitUtils.mergeRemoteIntoLocalBranch(userBranch);
        System.out.println(Game.i18n.tr("Your session {0} was automatically retrieved from the servers.", userBranch));
      } else {
        // If no branch can be found remotely, create a new one.
        // System.out.println(Game.i18n.tr("Creating a new session locally, as no corresponding session could be retrieved from the servers.",userBranch));
        System.out.println(Game.i18n.tr("Couldn't retrieve a corresponding session from the servers..."));
      }

      // Log into the git that the PLM just started
      gitUtils.commit(writePLMStartedOrLeavedCommitMessage("started"));

      // and push to ensure that everything remains in sync
      gitUtils.maybePushToUserBranch(userBranch, progress);
    } catch (Exception e) {
      System.err.println(Game.i18n.tr("You found a bug in the PLM. Please report it with all possible details (including the stacktrace below)."));
      e.printStackTrace();
    }
  }

  @Override public void executed(Exercise exo, RunOutcome outcome)
  {
    lastOutcomes.put(exo, outcome);
    try {

      // checkout the branch of the current user (just in case it changed in between)
      // if this case might happen, then you should also checkout back the current user branch...
      // GitUtils gitUtils = new GitUtils(git);
      // gitUtils.checkoutUserBranch(Game.getInstance().getUsers().getCurrentUser(), progress);

      // Change the files locally
      createFiles(exo, outcome);
      checkSuccess(exo, outcome);

      String commitMsg  = writeCommitMessage(exo, outcome, null, "executed", new JSONObject());
      String userUUID   = Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString();
      String userBranch = "PLM" + GitUtils.sha1(userUUID);

      gitUtils.removeFiles();
      gitUtils.seqAddFilesToPush(commitMsg, userBranch, progress);
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }

  @Override public void switched(Exercise exo)
  {
    Exercise lastExo = (Exercise)Game.getInstance().getLastExercise();
    if (lastExo == null)
      return;

    RunOutcome outcome = lastOutcome(lastExo);
    createFiles(lastExo, outcome);

    try {
      String commitMsg  = writeCommitMessage(lastExo, outcome, exo, "switched", new JSONObject());
      String userUUID   = Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString();
      String userBranch = "PLM" + GitUtils.sha1(userUUID);

      gitUtils.seqAddFilesToPush(commitMsg, userBranch, progress);
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }

  @Override public void reverted(Exercise exo)
  {
    try {
      deleteFiles(exo);

      String commitMsg  = writeCommitMessage(exo, lastOutcome(exo), null, "reverted", new JSONObject());
      String userUUID   = Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString();
      String userBranch = "PLM" + GitUtils.sha1(userUUID);

      gitUtils.removeFiles();
      gitUtils.commit(commitMsg);
      gitUtils.maybePushToUserBranch(userBranch, progress);
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }

  @Override public void leave()
  {
    System.out.println(Game.i18n.tr("Pushing to the remote repository before exiting."));

    // push to the remote repository
    String commitMsg  = writePLMStartedOrLeavedCommitMessage("leaved");
    String userUUID   = Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString();
    String userBranch = "PLM" + GitUtils.sha1(userUUID);

    try {
      gitUtils.addFiles();
      gitUtils.commit(commitMsg);
      gitUtils.forcefullyPushToUserBranch(userBranch, progress);

    } catch (GitAPIException e) {
      System.err.println("An error occurred while quitting the program, please report the following issue:");
      e.printStackTrace();
    }

    gitUtils.dispose();
  }

  /**
   * Helper methods
   */
  @SuppressWarnings("unchecked") private String writeCommitMessage(Exercise exoFrom, RunOutcome outcome, Exercise exoTo, String evt_type, JSONObject logmsg)
  {
    // Retrieve appropriate parameters regarding the current exercise
    logmsg.put("exo", exoFrom.getId());
    logmsg.put("lang", outcome.language.toString());

    if (outcome.outcome != null) {
      switch (outcome.outcome) {
        case COMPILE:
          logmsg.put("outcome", "compile");
          break;
        case FAIL:
          logmsg.put("outcome", "fail");
          break;
        case PASS:
          logmsg.put("outcome", "pass");
          break;
        default:
          logmsg.put("outcome", "UNKNOWN");
          break;
      }
    }

    if (outcome.totalTests > 0) {
      logmsg.put("passedtests", outcome.passedTests + "");
      logmsg.put("totaltests", outcome.totalTests + "");
    }

    // The commit must tell on its own the inputs that the student's code ran on
    Map<Integer, String> randomized = exoFrom.serializeRandomizedWorlds();
    if (!randomized.isEmpty())
      logmsg.put("randomizedworlds", new JSONObject(randomized));

    if (outcome.feedbackDifficulty != null)
      logmsg.put("exoDifficulty", outcome.feedbackDifficulty);
    if (outcome.feedbackInterest != null)
      logmsg.put("exoInterest", outcome.feedbackInterest);
    if (outcome.feedback != null)
      logmsg.put("exoComment", outcome.feedback);

    if (exoTo != null)
      logmsg.put("switchto", exoTo.getId());

    // Misuses JSON to ensure that the kind is always written first so that we can read github commit lists
    return "{\"kind\":\"" + evt_type + "\"," + logmsg.toString().substring(1);
  }

  /**
   * This method creates a String which contains debug informations. This String will be used as the commit
   * message set when the PLM is started by the user.
   *
   * @return the JSON String that will be used as the commit message
   */
  @SuppressWarnings("unchecked") private String writePLMStartedOrLeavedCommitMessage(String kind)
  {
    JSONObject jsonObject = new JSONObject();

    // Retrieve the feedback informations
    jsonObject.put("java", System.getProperty("java.version") + " (VM: " + System.getProperty("java.vm.name") +
                               "; version: " + System.getProperty("java.vm.version") + ")");
    jsonObject.put("os", System.getProperty("os.name") + " (version: " + System.getProperty("os.version") + "; arch: " + System.getProperty("os.arch") + ")");
    jsonObject.put("plm", Game.getProperty("plm.major.version", "internal", false) + " (" + Game.getProperty("plm.minor.version", "internal", false) +
                              "), commit " + Game.getGitCommit()); // the commit is what it takes to replay the templating of the session

    // Misuses JSON to ensure that the kind is always written first so that we can read github commit lists
    return "{\"kind\":\"" + kind + "\"," + jsonObject.toString().substring(1);
  }

  private void createFiles(Exercise exo, RunOutcome outcome)
  {
    String exoCode  = exo.getEditorContent(outcome.language, exo.getSourceFile(outcome.language, 0)); // retrieve the code from the student
    String exoError = outcome.compilationError;                                                       // retrieve the compilation error
    if (outcome.compilationError == null)
      exoError = outcome.executionError;
    String exoCorrection = ""; // retrieve the correction: the whole entity file, if there is one in this language
    try {
      exoCorrection = FileUtils.readContentAsText(exo.nameOfCorrectionEntity(), outcome.language.getExt(), false).toString();
    } catch (IOException e) {
      // no entity in this language
    }
    String exoMission = exo.getMission(outcome.language); // retrieve the mission

    // create the different files
    String ext = "." + outcome.language.getExt();

    try {
      writeFile(new File(repoDir, exo.getId() + ext + ".code"), exoCode);
      writeFile(new File(repoDir, exo.getId() + ext + ".error"), exoError);
      writeFile(new File(repoDir, exo.getId() + ext + ".correction"), exoCorrection);
      writeFile(new File(repoDir, exo.getId() + ext + ".mission"), exoMission);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Writes the content (empty if null) into the file, replacing what it contained */
  private void writeFile(File file, String content) throws IOException
  {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
      bw.write(content == null ? "" : content);
    }
  }

  private void deleteFiles(Exercise exo)
  {

    List<String> suffixes = new ArrayList<String>();
    suffixes.add(".code");
    suffixes.add(".error");
    suffixes.add(".correction");
    suffixes.add(".mission");
    suffixes.add(".DONE");

    for (ProgrammingLanguage pl : Game.getInstance().getProgrammingLanguageManager().langs) {
      String ext = "." + pl.getExt();
      for (String suffix : suffixes) {
        File file = new File(repoDir, exo.getId() + ext + suffix);
        if (file.exists()) {
          file.delete();
        }
      }
    }
  }

  /**
   * Create some files to know how many exercises there is by programming languages for this lesson. Also add a
   * file to know if the exercise has been done correctly.
   *
   * @param exo
   */
  private void checkSuccess(Exercise exo, RunOutcome outcome)
  {
    String ext = "." + outcome.language.getExt();

    // if exercise is done correctly
    File doneFile = new File(repoDir, exo.getId() + ext + ".DONE");
    if (outcome.outcome == RunOutcome.kind.PASS) {
      try {
        FileWriter fwExo     = new FileWriter(doneFile.getAbsoluteFile());
        BufferedWriter bwExo = new BufferedWriter(fwExo);
        bwExo.write("");
        bwExo.close();
      } catch (IOException ex) {
        System.out.println("Failed to write on disk that the exercise is passed: " + ex.getLocalizedMessage());
      }
    } else {
      if (doneFile.exists())
        doneFile.delete(); // not passed anymore. Sad thing.
    }
  }

  /** The outcome of the last execution of exo in the current language, or an empty one if it was not executed since */
  private RunOutcome lastOutcome(Exercise exo)
  {
    ProgrammingLanguage lang = Game.getInstance().getProgrammingLanguage();
    RunOutcome outcome       = lastOutcomes.get(exo);
    return outcome != null && outcome.language == lang ? outcome : new RunOutcome(lang);
  }

  @Override @SuppressWarnings("unchecked") public void readTip(String id, String mission, ProgrammingLanguage lang)
  {
    Exercise lastExo = (Exercise)Game.getInstance().getCurrentLesson().getCurrentExercise();
    String ext       = "." + lang.getExt();
    File missionFile = new File(repoDir, lastExo.getId() + ext + ".mission");

    try {
      writeFile(missionFile, mission);
    } catch (IOException e) {
      e.printStackTrace();
    }

    JSONObject msg = new JSONObject();
    msg.put("id", id);
    String commitMsg  = writeCommitMessage(lastExo, lastOutcome(lastExo), null, "readTip", msg);
    String userUUID   = Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString();
    String userBranch = "PLM" + GitUtils.sha1(userUUID);

    try {
      gitUtils.seqAddFilesToPush(commitMsg, userBranch, progress);
    } catch (GitAPIException e) {
      System.err.println("An error occurred while pushing the fact that you took a look at the hint...\n"
                         + "Please send a bug report with the following trace:");
      e.printStackTrace();
    }
  }
}
