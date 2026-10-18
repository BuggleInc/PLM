package plm.test.git;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xnap.commons.i18n.I18nFactory;
import plm.core.lang.ProgrammingLanguage;
import plm.core.lang.ProgrammingLanguageManager;
import plm.core.model.Game;
import plm.core.model.User;
import plm.core.model.Users;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.lesson.RunOutcome.kind;
import plm.core.model.tracking.GitTracker;
import plm.core.utils.FileUtils;

@ExtendWith(MockitoExtension.class)
public class GitTrackerTest {

  private GitTracker gitTracker;
  private File repoDir;
  private String userUUID;
  private Utils utils;

  public GitTrackerTest() throws IOException, GitAPIException
  {
    repoDir     = new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".plm-test");
    Users users = Mockito.mock(Users.class);
    User user   = Mockito.mock(User.class);

    Game.i18n = I18nFactory.getI18n(getClass(), "org.plm.i18n.Messages", FileUtils.getLocale(), I18nFactory.FALLBACK);

    userUUID = UUID.randomUUID().toString();

    Mockito.when(user.getUserUUIDasString()).thenReturn(userUUID);
    Mockito.when(users.getCurrentUser()).thenReturn(user);
    gitTracker = new GitTracker(repoDir, users);

    utils = new Utils();

    System.out.println("repoDir: " + repoDir.getAbsolutePath());
  }

  @BeforeEach public void setUp() {}

  @AfterEach public void tearDown() throws IOException
  {
    utils.deleteRepo(repoDir);
    Files.deleteIfExists(ENTITY_FILE);
  }

  // GitTracker reads the correction from the entity file of the exercise
  private static final String ENTITY    = "target.gittrackertest.ExoTestEntity";
  private static final Path ENTITY_FILE = Path.of("target/gittrackertest/ExoTestEntity.java");

  private void writeEntity(String content) throws IOException
  {
    Files.createDirectories(ENTITY_FILE.getParent());
    Files.writeString(ENTITY_FILE, content + System.lineSeparator());
  }

  @Test
  public void testCreateFilesWithoutPreviouslyCreatedFiles()
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
  {
    String code       = utils.generateRandomString(32);
    String correction = utils.generateRandomString(32);
    String error      = utils.generateRandomString(32);
    String mission    = utils.generateRandomString(32);

    RunOutcome lastResult       = new RunOutcome(Game.getInstance().programmingLanguageManager.JAVA);
    lastResult.compilationError = error;

    Exercise exo = Mockito.mock(Exercise.class);
    Mockito.when(exo.nameOfCorrectionEntity()).thenReturn(ENTITY);
    writeEntity(correction);
    Mockito.when(exo.getId()).thenReturn("exoTest");

    Mockito.when(exo.getMission(lastResult.language)).thenReturn(mission);
    Mockito.when(exo.getEditorContent(lastResult.language)).thenReturn(code);

    ArrayList<String> suffixes = new ArrayList<String>();
    suffixes.add(".code");
    suffixes.add(".correction");
    suffixes.add(".error");
    suffixes.add(".mission");

    for (String suffix : suffixes) {
      String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, suffix);
      if (new File(fp).exists()) {
        Assertions.fail(fp + " should not exist previously...");
      }
    }

    Method method = GitTracker.class.getDeclaredMethod("createFiles", Exercise.class, RunOutcome.class);
    method.setAccessible(true);
    method.invoke(gitTracker, exo, lastResult);

    HashMap<String, String> hm = new HashMap<>();
    for (String suffix : suffixes) {
      hm.put(suffix, utils.getFileContent(repoDir, userUUID, exo, lastResult, suffix));
    }

    if (!hm.get(".code").equals(code)) {
      Assertions.fail("Code file's content is different from code:\nexpected: " + code + "\nresult: " + hm.get(".code"));
    }
    if (!hm.get(".correction").equals(correction + System.lineSeparator())) {
      Assertions.fail("Correction file's content is different from correction:\nexpected: " + correction + "\nresult: " + hm.get(".correction"));
    }
    if (!hm.get(".error").equals(error)) {
      Assertions.fail("Error file's content is different from error:\nexpected: " + error + "\nresult: " + hm.get(".error"));
    }
    if (!hm.get(".mission").equals(mission)) {
      Assertions.fail("Mission file's content is different from mission:\nexpected: " + mission + "\nresult: " + hm.get(".mission"));
    }
  }

  @Test
  public void testCreateFilesWithPreviouslyCreatedFiles()
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
  {
    String code       = utils.generateRandomString(32);
    String correction = utils.generateRandomString(32);
    String error      = utils.generateRandomString(32);
    String mission    = utils.generateRandomString(32);

    RunOutcome lastResult       = new RunOutcome(Game.getInstance().programmingLanguageManager.JAVA);
    lastResult.compilationError = null;
    lastResult.executionError   = error;

    Exercise exo = Mockito.mock(Exercise.class);
    Mockito.when(exo.nameOfCorrectionEntity()).thenReturn(ENTITY);
    writeEntity(correction);
    Mockito.when(exo.getId()).thenReturn("exoTest");

    Mockito.when(exo.getMission(lastResult.language)).thenReturn(mission);
    Mockito.when(exo.getEditorContent(lastResult.language)).thenReturn(code);

    ArrayList<String> suffixes = new ArrayList<String>();
    suffixes.add(".code");
    suffixes.add(".correction");
    suffixes.add(".error");
    suffixes.add(".mission");

    for (String suffix : suffixes) {
      String fp         = utils.getFilePath(repoDir, userUUID, exo, lastResult, suffix);
      File file         = new File(fp);
      FileWriter fw     = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(utils.generateRandomString(32));
      bw.close();
    }

    for (String suffix : suffixes) {
      String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, suffix);
      if (!(new File(fp).exists())) {
        Assertions.fail(fp + " should have been created...");
      }
    }

    Method method = GitTracker.class.getDeclaredMethod("createFiles", Exercise.class, RunOutcome.class);
    method.setAccessible(true);
    method.invoke(gitTracker, exo, lastResult);

    HashMap<String, String> hm = new HashMap<>();
    for (String suffix : suffixes) {
      hm.put(suffix, utils.getFileContent(repoDir, userUUID, exo, lastResult, suffix));
    }

    if (!hm.get(".code").equals(code)) {
      Assertions.fail("Code file's content is different from code:\nexpected: " + code + "\nresult: " + hm.get(".code"));
    }
    if (!hm.get(".correction").equals(correction + System.lineSeparator())) {
      Assertions.fail("Correction file's content is different from correction:\nexpected: " + correction + "\nresult: " + hm.get(".correction"));
    }
    if (!hm.get(".error").equals(error)) {
      Assertions.fail("Error file's content is different from error:\nexpected: " + error + "\nresult: " + hm.get(".error"));
    }
    if (!hm.get(".mission").equals(mission)) {
      Assertions.fail("Mission file's content is different from mission:\nexpected: " + mission + "\nresult: " + hm.get(".mission"));
    }
  }

  @Test
  public void testSuccessfulCheckSuccessWithoutPreviousSuccessShouldCreateDoneFile()
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    RunOutcome lastResult = new RunOutcome(Game.getInstance().programmingLanguageManager.JAVA);
    lastResult.outcome    = kind.PASS;

    Exercise exo = Mockito.mock(Exercise.class);
    Mockito.when(exo.getId()).thenReturn("exoTest");

    String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, ".DONE");
    if (new File(fp).exists()) {
      Assertions.fail(fp + " should not exist previously...");
    }

    Method method = GitTracker.class.getDeclaredMethod("checkSuccess", Exercise.class, RunOutcome.class);
    method.setAccessible(true);
    method.invoke(gitTracker, exo, lastResult);

    if (!(new File(fp).exists())) {
      Assertions.fail(fp + " should exist now...");
    }
  }

  @Test
  public void testSuccessfulCheckSuccessWithPreviousSuccessShouldKeepDoneFile()
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
  {
    RunOutcome lastResult = new RunOutcome(Game.getInstance().programmingLanguageManager.JAVA);
    lastResult.outcome    = kind.PASS;

    Exercise exo = Mockito.mock(Exercise.class);
    Mockito.when(exo.getId()).thenReturn("exoTest");

    String fp         = utils.getFilePath(repoDir, userUUID, exo, lastResult, ".DONE");
    File file         = new File(fp);
    FileWriter fw     = new FileWriter(file.getAbsoluteFile());
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write("");
    bw.close();

    if (!(new File(fp).exists())) {
      Assertions.fail(fp + " should exist previously...");
    }

    Method method = GitTracker.class.getDeclaredMethod("checkSuccess", Exercise.class, RunOutcome.class);
    method.setAccessible(true);
    method.invoke(gitTracker, exo, lastResult);

    if (!(new File(fp).exists())) {
      Assertions.fail(fp + " should still exist now...");
    }
  }

  @Test
  public void testFailedCheckSuccessWithoutPreviousSuccessShouldDoNothing()
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
  {
    RunOutcome lastResult = new RunOutcome(Game.getInstance().programmingLanguageManager.JAVA);
    lastResult.outcome    = kind.FAIL;

    Exercise exo = Mockito.mock(Exercise.class);
    Mockito.when(exo.getId()).thenReturn("exoTest");

    String fp = utils.getFilePath(repoDir, userUUID, exo, lastResult, ".DONE");
    if (new File(fp).exists()) {
      Assertions.fail(fp + " should not exist previously...");
    }

    Method method = GitTracker.class.getDeclaredMethod("checkSuccess", Exercise.class, RunOutcome.class);
    method.setAccessible(true);
    method.invoke(gitTracker, exo, lastResult);

    if (new File(fp).exists()) {
      Assertions.fail(fp + " should not have been created...");
    }
  }

  @Test
  public void testFailedCheckSuccessWithPreviousSuccessShouldDeleteDoneFile()
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
  {
    RunOutcome lastResult = new RunOutcome(Game.getInstance().programmingLanguageManager.JAVA);
    lastResult.outcome    = kind.FAIL;

    Exercise exo = Mockito.mock(Exercise.class);
    Mockito.when(exo.getId()).thenReturn("exoTest");

    String fp         = utils.getFilePath(repoDir, userUUID, exo, lastResult, ".DONE");
    File file         = new File(fp);
    FileWriter fw     = new FileWriter(file.getAbsoluteFile());
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write("");
    bw.close();

    if (!(new File(fp).exists())) {
      Assertions.fail(fp + " should exist previously...");
    }

    Method method = GitTracker.class.getDeclaredMethod("checkSuccess", Exercise.class, RunOutcome.class);
    method.setAccessible(true);
    method.invoke(gitTracker, exo, lastResult);

    if (new File(fp).exists()) {
      Assertions.fail(fp + " should have been deleted...");
    }
  }

  @Test
  public void testDeleteFilesWithoutExistingFiles()
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    Exercise exo = Mockito.mock(Exercise.class);
    Mockito.when(exo.getId()).thenReturn("exoTest");

    List<String> suffixes = new ArrayList<String>();
    suffixes.add(".code");
    suffixes.add(".error");
    suffixes.add(".correction");
    suffixes.add(".mission");
    suffixes.add(".DONE");

    for (ProgrammingLanguage pl : Game.getInstance().programmingLanguageManager.langs) {
      String ext = "." + pl.getExt();
      for (String suffix : suffixes) {
        File file = new File(repoDir, exo.getId() + ext + suffix);
        if (file.exists()) {
          Assertions.fail(file.getAbsolutePath() + " should not exist...");
        }
      }
    }

    Method method = GitTracker.class.getDeclaredMethod("deleteFiles", Exercise.class);
    method.setAccessible(true);
    method.invoke(gitTracker, exo);

    for (ProgrammingLanguage pl : Game.getInstance().programmingLanguageManager.langs) {
      String ext = "." + pl.getExt();
      for (String suffix : suffixes) {
        File file = new File(repoDir, exo.getId() + ext + suffix);
        if (file.exists()) {
          Assertions.fail(file.getAbsolutePath() + " should still not exist...");
        }
      }
    }
  }

  @Test
  public void testDeleteFilesWithExistingFiles()
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
  {
    Exercise exo = Mockito.mock(Exercise.class);
    Mockito.when(exo.getId()).thenReturn("exoTest");

    List<String> suffixes = new ArrayList<String>();
    suffixes.add(".code");
    suffixes.add(".error");
    suffixes.add(".correction");
    suffixes.add(".mission");
    suffixes.add(".DONE");

    for (ProgrammingLanguage pl : Game.getInstance().programmingLanguageManager.langs) {
      for (String suffix : suffixes) {
        String fp = utils.getFilePath(repoDir, userUUID, exo, pl, suffix);
        File file = new File(fp);
        if (file.exists()) {
          Assertions.fail(file.getAbsolutePath() + " should not yet exist...");
        }
        FileWriter fw     = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("");
        bw.close();
      }
    }

    Method method = GitTracker.class.getDeclaredMethod("deleteFiles", Exercise.class);
    method.setAccessible(true);
    method.invoke(gitTracker, exo);

    for (ProgrammingLanguage pl : Game.getInstance().programmingLanguageManager.langs) {
      String ext = "." + pl.getExt();
      for (String suffix : suffixes) {
        File file = new File(repoDir, exo.getId() + ext + suffix);
        if (file.exists()) {
          Assertions.fail(file.getAbsolutePath() + " should have been deleted...");
        }
      }
    }
  }

  @Test public void testStartedAndLeavedMessagesTellTheGitCommitOfThePLM() throws Exception
  {
    Method method = GitTracker.class.getDeclaredMethod("writePLMStartedOrLeavedCommitMessage", String.class);
    method.setAccessible(true);

    Assertions.assertTrue(Game.getGitCommit().matches("[0-9a-f]{40}(-dirty)?|unknown"), Game.getGitCommit());
    for (String kind : new String[] {"started", "leaved"}) {
      JSONObject message = (JSONObject) new JSONParser().parse((String)method.invoke(gitTracker, kind));
      Assertions.assertEquals(kind, message.get("kind"));
      Assertions.assertTrue(((String)message.get("plm")).endsWith(", commit " + Game.getGitCommit()), (String)message.get("plm"));
    }
  }

  /** The outcome that the tracker reports for the events that are not executions (switched, readTip) */
  private RunOutcome lastOutcome(Exercise exo) throws ReflectiveOperationException
  {
    Method method = GitTracker.class.getDeclaredMethod("lastOutcome", Exercise.class);
    method.setAccessible(true);
    return (RunOutcome)method.invoke(gitTracker, exo);
  }

  @SuppressWarnings("unchecked") private void remember(Exercise exo, RunOutcome outcome) throws ReflectiveOperationException
  {
    Field field = GitTracker.class.getDeclaredField("lastOutcomes");
    field.setAccessible(true);
    ((Map<Exercise, RunOutcome>)field.get(gitTracker)).put(exo, outcome);
  }

  @Test public void testLastOutcomeIsTheOneOfTheLastExecutionInTheCurrentLanguage() throws Exception
  {
    ProgrammingLanguageManager langs = Game.getInstance().programmingLanguageManager;
    ProgrammingLanguage previous     = Game.getInstance().getProgrammingLanguage();
    try {
      Game.getInstance().setProgrammingLanguage(langs.JAVA);
      Exercise exo       = Mockito.mock(Exercise.class);
      RunOutcome outcome = new RunOutcome(langs.JAVA);
      outcome.outcome    = kind.FAIL;
      remember(exo, outcome);

      Assertions.assertSame(outcome, lastOutcome(exo));

      // The student changed the language since: that execution does not tell anything about the new one
      Game.getInstance().setProgrammingLanguage(langs.PYTHON);
      RunOutcome other = lastOutcome(exo);
      Assertions.assertNotSame(outcome, other);
      Assertions.assertSame(langs.PYTHON, other.language);
      Assertions.assertEquals(0, other.totalTests);
    } finally {
      Game.getInstance().setProgrammingLanguage(previous);
    }
  }

  @Test public void testLastOutcomeOfAnExerciseThatWasNotExecutedIsEmptyInTheCurrentLanguage() throws Exception
  {
    Exercise exo     = Mockito.mock(Exercise.class);
    RunOutcome empty = lastOutcome(exo);

    Assertions.assertSame(Game.getInstance().getProgrammingLanguage(), empty.language);
    Assertions.assertNull(empty.compilationError);
    Assertions.assertEquals(0, empty.totalTests);
  }
}
