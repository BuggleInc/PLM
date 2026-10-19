package plm.core.model.tracking;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import plm.core.model.session.SessionZip;
import plm.core.utils.FileUtils;

public class GitTracker implements UserSwitchesListener {

  private File plmDir;
  private GitUtils gitUtils;

  private ProgressMonitor progress = NullProgressMonitor.INSTANCE; // new TextProgressMonitor(new PrintWriter(System.out));

  private String repoUrl = Game.getProperty("plm.git.server.url"); // https://github.com/mquinson/PLM-data.git
  private File repoDir;
  private final Users users;

  /** The outcome of the last execution of each exercise, reported by the events that are not executions */
  private final Map<Exercise, RunOutcome> lastOutcomes = new HashMap<>();

  public GitTracker(File path, Users users)
  {
    this.plmDir = path;
    this.users  = users;

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
        gitUtils.mergeIntoLocalBranch("refs/remotes/origin/" + userBranch);
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

  public void executed(Exercise exo, RunOutcome outcome)
  {
    lastOutcomes.put(exo, outcome);
    createFiles(exo, outcome);
    checkSuccess(exo, outcome);
    commitAndPush(writeCommitMessage(exo, outcome.language, outcome, "executed", new JSONObject()));
  }

  @SuppressWarnings("unchecked") public void switched(Exercise exo)
  {
    Exercise lastExo = (Exercise)Game.getInstance().getLastExercise();
    if (lastExo == null)
      return;

    createFiles(lastExo, lastOutcome(lastExo));

    /* The message tells about the navigation only, and so does the one of reverted() */
    JSONObject msg = new JSONObject();
    msg.put("switchto", exo.getId());
    commitAndPush(writeCommitMessage(lastExo, Game.getInstance().getProgrammingLanguage(), null, "switched", msg));
  }

  public void reverted(Exercise exo)
  {
    deleteFiles(exo);
    commitAndPush(writeCommitMessage(exo, Game.getInstance().getProgrammingLanguage(), null, "reverted", new JSONObject()));
  }

  /** Releases the repository, whose files cannot be deleted on Windows while it is open */
  public void close() { gitUtils.dispose(); }

  public void leave()
  {
    System.out.println(Game.i18n.tr("Pushing to the remote repository before exiting."));

    // push to the remote repository
    String commitMsg = writePLMStartedOrLeavedCommitMessage("leaved");

    gitUtils.cancelPendingPush();
    try {
      gitUtils.addFiles();
      gitUtils.commit(commitMsg);
      gitUtils.forcefullyPushToUserBranch(currentUserBranch(), progress);

    } catch (GitAPIException e) {
      System.err.println("An error occurred while quitting the program, please report the following issue:");
      e.printStackTrace();
    }

    close();
  }

  /** Commits the pending changes, then writes the whole local repository of the current user into that zip file */
  public void exportSession(File zip) throws IOException, GitAPIException
  {
    gitUtils.addFiles();
    gitUtils.commit(writePLMStartedOrLeavedCommitMessage("exported"));
    SessionZip.zip(repoDir, zip);
  }

  /** True if the current session holds no code yet */
  public boolean isSessionEmpty() { return repoDir.list((dir, name) -> name.endsWith(".code")).length == 0; }

  /**
   * Merges the session of a zip file written by exportSession() into the current one.
   *
   * @param adoptIdentity if the user of the zip becomes the current user, replacing the previous one. Otherwise, the
   *                      session of the zip is merged into the one of the current user.
   */
  public void importSession(File zip, boolean adoptIdentity) throws Exception
  {
    Game game = Game.getInstance();
    game.saveSession();
    gitUtils.addFiles();
    gitUtils.commit(writePLMStartedOrLeavedCommitMessage("imported"));

    String zipUUID   = SessionZip.userUUID(zip);
    String zipBranch = "PLM" + GitUtils.sha1(zipUUID);
    File tmpDir      = Files.createTempDirectory(plmDir.toPath(), "import-").toFile(); // on the same file system as the sessions, to move from it
    try {
      SessionZip.unzip(zip, tmpDir);
      File zipRepoDir = new File(tmpDir, zipUUID);
      if (!new File(zipRepoDir, ".git").isDirectory())
        throw new IOException(zip + " is not a PLM session: it holds no git repository");

      if (!adoptIdentity) {
        gitUtils.mergeBranchFromRepository(zipRepoDir, zipBranch);
      } else {
        User previous         = users.getCurrentUser();
        File previousDir      = repoDir;
        String previousBranch = currentUserBranch();
        boolean keepPrevious  = !isSessionEmpty();
        File adoptedDir       = new File(plmDir, zipUUID);

        Files.move(zipRepoDir.toPath(), adoptedDir.toPath()); // fails if that session already exists here
        try {
          users.addUser(previous.getUsername(), zipUUID); // switches to it: repoDir and gitUtils now are the ones of the zip
          if (keepPrevious)
            gitUtils.mergeBranchFromRepository(previousDir, previousBranch);
          users.removeUser(previous);
        } catch (Exception e) { // back to the previous user, without any trace of the zip
          User adopted = users.getCurrentUser();
          if (adopted != previous) {
            users.switchToUser(previous);
            users.removeUser(adopted);
          }
          FileUtils.deleteRecursive(adoptedDir);
          throw e;
        }
      }
    } finally {
      FileUtils.deleteRecursive(tmpDir);
    }
    game.loadSession();
  }

  /**
   * Helper methods
   */
  private String currentUserBranch() { return "PLM" + GitUtils.sha1(users.getCurrentUser().getUserUUIDasString()); }

  /** Records an event: commits the files that were written or deleted, with that message, and asks for a push */
  private void commitAndPush(String commitMsg)
  {
    try {
      gitUtils.removeFiles();
      gitUtils.seqAddFilesToPush(commitMsg, currentUserBranch(), progress);
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }

  /** The outcome is null for the events that are not about an execution (switched, reverted): the message then carries no outcome */
  @SuppressWarnings("unchecked")
  private String writeCommitMessage(Exercise exoFrom, ProgrammingLanguage lang, RunOutcome outcome, String evt_type, JSONObject logmsg)
  {
    // Retrieve appropriate parameters regarding the current exercise
    logmsg.put("exo", exoFrom.getId());
    logmsg.put("lang", lang.toString());

    if (outcome != null) {
      logmsg.put("outcome", outcome.outcome.name().toLowerCase(Locale.ROOT));

      if (outcome.totalTests > 0) {
        logmsg.put("passedtests", outcome.passedTests + "");
        logmsg.put("totaltests", outcome.totalTests + "");
      }

      if (outcome.feedbackDifficulty != null)
        logmsg.put("exoDifficulty", outcome.feedbackDifficulty);
      if (outcome.feedbackInterest != null)
        logmsg.put("exoInterest", outcome.feedbackInterest);
      if (outcome.feedback != null)
        logmsg.put("exoComment", outcome.feedback);
    }

    // The commit must tell on its own the inputs that the student's code ran on
    Map<Integer, String> randomized = exoFrom.serializeRandomizedWorlds();
    if (!randomized.isEmpty())
      logmsg.put("randomizedworlds", new JSONObject(randomized));

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
    String exoCode  = exo.getEditorContent(outcome.language);                                         // retrieve the code from the student
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
  private void writeFile(File file, String content) throws IOException { Files.writeString(file.toPath(), content == null ? "" : content); }

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
        if (file.exists() && !file.delete())
          System.err.println("Cannot delete " + file);
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
    if (outcome.isPassed()) {
      try {
        writeFile(doneFile, "");
      } catch (IOException ex) {
        System.out.println("Failed to write on disk that the exercise is passed: " + ex.getLocalizedMessage());
      }
    } else {
      if (doneFile.exists() && !doneFile.delete()) // not passed anymore. Sad thing.
        System.err.println("Cannot delete " + doneFile);
    }
  }

  /** The outcome of the last execution of exo in the current language, or an empty one if it was not executed since */
  private RunOutcome lastOutcome(Exercise exo)
  {
    ProgrammingLanguage lang = Game.getInstance().getProgrammingLanguage();
    RunOutcome outcome       = lastOutcomes.get(exo);
    return outcome != null && outcome.language == lang ? outcome : new RunOutcome(lang);
  }

  @SuppressWarnings("unchecked") public void readTip(String id, String mission, ProgrammingLanguage lang)
  {
    Exercise lastExo = (Exercise)Game.getInstance().getCurrentLesson().getCurrentExercise();
    try {
      writeFile(new File(repoDir, lastExo.getId() + "." + lang.getExt() + ".mission"), mission);
    } catch (IOException e) {
      e.printStackTrace();
    }

    JSONObject msg = new JSONObject();
    msg.put("id", id);
    commitAndPush(writeCommitMessage(lastExo, lang, lastOutcome(lastExo), "readTip", msg));
  }
}
