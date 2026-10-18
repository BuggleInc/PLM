package plm.test.git;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.xnap.commons.i18n.I18nFactory;
import plm.core.model.Game;
import plm.core.model.User;
import plm.core.model.Users;
import plm.core.model.tracking.GitTracker;
import plm.core.utils.FileUtils;

/** Importing the session of a zip file merges its history into the local one, without losing anything */
public class GitTrackerImportTest {
  @TempDir Path path;
  private final List<Machine> machines = new ArrayList<>();

  private static final String LOCAL = "11111111-1111-1111-1111-111111111111";
  private static final String OTHER = "22222222-2222-2222-2222-222222222222";

  /** Windows cannot delete the temporary directory while the repositories in there are open */
  @AfterEach public void closeTrackers() { machines.forEach(machine -> machine.tracker.close()); }

  @BeforeAll public static void setUpGame()
  {
    Game.getInstance().removeSessionKit(); // importSession() saves and reloads the session of the game: keep it away from the real one
    Game.i18n = I18nFactory.getI18n(GitTrackerImportTest.class, "org.plm.i18n.Messages", FileUtils.getLocale(), I18nFactory.FALLBACK);
  }

  private static User user(String uuid)
  {
    User user = Mockito.mock(User.class);
    Mockito.when(user.getUserUUIDasString()).thenReturn(uuid);
    Mockito.when(user.getUsername()).thenReturn("user " + uuid);
    return user;
  }

  /** A machine: its own sessions directory, and the tracker working in it for the user that is current there */
  private class Machine {
    final Path sessions;
    final User user;
    final Users users = Mockito.mock(Users.class);
    final AtomicReference<User> current;
    final GitTracker tracker;

    Machine(String name, String uuid) throws IOException, GitAPIException
    {
      sessions = Files.createDirectories(path.resolve(name));
      user     = user(uuid);
      current  = new AtomicReference<>(user);
      Mockito.when(users.getCurrentUser()).thenAnswer(invocation -> current.get());
      tracker = new GitTracker(sessions.toFile(), users);
      machines.add(this);
    }

    /** Makes addUser() behave like the real one: the new user becomes the current one, and the tracker switches to it */
    void stubAddUser()
    {
      Mockito
          .doAnswer(invocation -> {
            User added = user(invocation.getArgument(1));
            current.set(added);
            tracker.userHasChanged(added);
            return null;
          })
          .when(users)
          .addUser(Mockito.any(), Mockito.anyString());
    }

    /** Makes switchToUser() behave like the real one: the tracker switches to that user */
    void stubSwitchToUser() throws Exception
    {
      Mockito
          .doAnswer(invocation -> {
            current.set(invocation.getArgument(0));
            tracker.userHasChanged(invocation.getArgument(0));
            return true;
          })
          .when(users)
          .switchToUser(Mockito.any());
    }

    void write(String uuid, String file, String content) throws IOException { Files.writeString(sessions.resolve(uuid).resolve(file), content); }

    /** Deletes that file as a revert does: from the working tree and from the index */
    void delete(String uuid, String file)throws IOException, GitAPIException
    {
      try (Git git = Git.open(sessions.resolve(uuid).toFile())) {
        git.rm().addFilepattern(file).call();
      }
    }

    boolean exists(String uuid, String file) { return Files.exists(sessions.resolve(uuid).resolve(file)); }

    String read(String uuid, String file) throws IOException { return Files.readString(sessions.resolve(uuid).resolve(file)); }

    /** Commits the pending changes and returns the zip file of the session */
    Path export() throws IOException, GitAPIException
    {
      Path zip = sessions.resolveSibling(sessions.getFileName() + ".zip");
      tracker.exportSession(zip.toFile());
      return zip;
    }
  }

  private static ObjectId rootCommit(Path sessions, String uuid) throws IOException, GitAPIException
  {
    try (Git git = Git.open(sessions.resolve(uuid).toFile())) {
      RevCommit root = null;
      for (RevCommit commit : git.log().call())
        root = commit; // the last listed is the oldest
      return root.getId();
    }
  }

  @Test public void testInitialCommitIsTheSameOnAllMachines() throws Exception
  {
    Machine one = new Machine("one", LOCAL);
    Machine two = new Machine("two", OTHER);

    Assertions.assertEquals(rootCommit(one.sessions, LOCAL), rootCommit(two.sessions, OTHER));
  }

  @Test public void testImportMergesTheSessionsOfASameUser() throws Exception
  {
    Machine local  = new Machine("local", LOCAL);
    Machine remote = new Machine("remote", LOCAL);
    remote.write(LOCAL, "a.java.code", "from the zip");
    local.write(LOCAL, "b.java.code", "from local");

    local.tracker.importSession(remote.export().toFile(), false);

    Assertions.assertEquals("from the zip", local.read(LOCAL, "a.java.code"));
    Assertions.assertEquals("from local", local.read(LOCAL, "b.java.code"));
  }

  @Test public void testConflictKeepsTheLocalFileWhenItIsMoreRecent() throws Exception
  {
    Machine local  = new Machine("local", LOCAL);
    Machine remote = new Machine("remote", LOCAL);
    remote.write(LOCAL, "a.java.code", "from the zip");
    Path zip = remote.export();
    Thread.sleep(1100); // the time of the commits has a resolution of one second
    local.write(LOCAL, "a.java.code", "from local");

    local.tracker.importSession(zip.toFile(), false);

    Assertions.assertEquals("from local", local.read(LOCAL, "a.java.code"));
  }

  @Test public void testConflictKeepsTheFileOfTheZipWhenItIsMoreRecent() throws Exception
  {
    Machine local  = new Machine("local", LOCAL);
    Machine remote = new Machine("remote", LOCAL);
    local.write(LOCAL, "a.java.code", "from local");
    local.export(); // commits the file now
    Thread.sleep(1100);
    remote.write(LOCAL, "a.java.code", "from the zip");

    local.tracker.importSession(remote.export().toFile(), false);

    Assertions.assertEquals("from the zip", local.read(LOCAL, "a.java.code"));
  }

  @Test public void testImportCanKeepTheLocalIdentity() throws Exception
  {
    Machine local  = new Machine("local", LOCAL);
    Machine remote = new Machine("remote", OTHER);
    remote.write(OTHER, "a.java.code", "from the zip");

    local.tracker.importSession(remote.export().toFile(), false);

    Mockito.verify(local.users, Mockito.never()).addUser(Mockito.any(), Mockito.anyString());
    Assertions.assertEquals("from the zip", local.read(LOCAL, "a.java.code"));
  }

  @Test public void testImportAdoptsTheIdentityOfTheZipWhenTheLocalSessionIsEmpty() throws Exception
  {
    Machine local  = new Machine("local", LOCAL);
    Machine remote = new Machine("remote", OTHER);
    local.stubAddUser();
    remote.write(OTHER, "a.java.code", "from the zip");
    Assertions.assertTrue(local.tracker.isSessionEmpty());

    local.tracker.importSession(remote.export().toFile(), true);

    Mockito.verify(local.users).removeUser(local.user);
    Assertions.assertEquals("from the zip", local.read(OTHER, "a.java.code"));
    Assertions.assertFalse(local.tracker.isSessionEmpty()); // the tracker now works on the session of the zip
  }

  @Test public void testImportAdoptsTheIdentityOfTheZipAndKeepsTheLocalWork() throws Exception
  {
    Machine local  = new Machine("local", LOCAL);
    Machine remote = new Machine("remote", OTHER);
    local.stubAddUser();
    remote.write(OTHER, "a.java.code", "from the zip");
    local.write(LOCAL, "b.java.code", "from local");
    Assertions.assertFalse(local.tracker.isSessionEmpty());

    local.tracker.importSession(remote.export().toFile(), true);

    Mockito.verify(local.users).removeUser(local.user);
    Assertions.assertEquals("from the zip", local.read(OTHER, "a.java.code"));
    Assertions.assertEquals("from local", local.read(OTHER, "b.java.code"));
  }

  @Test public void testImportRefusesToReplaceAnExistingSession() throws Exception
  {
    Machine local  = new Machine("local", LOCAL);
    Machine remote = new Machine("remote", OTHER);
    Files.createDirectories(local.sessions.resolve(OTHER)); // the zip user already exists on this machine
    Path zip = remote.export();

    Assertions.assertThrows(FileAlreadyExistsException.class, () -> local.tracker.importSession(zip.toFile(), true));
    Assertions.assertEquals(0, local.sessions.toFile().list((dir, name) -> name.startsWith("import-")).length, "the temporary directory is removed");
  }

  /** After a failed import, the local session is intact and nothing remains of the zip */
  private static void assertNothingImported(Machine local) throws IOException
  {
    Assertions.assertEquals("from local", local.read(LOCAL, "b.java.code"));
    Assertions.assertFalse(local.tracker.isSessionEmpty(), "the tracker still works on the local session");
    Assertions.assertSame(local.user, local.users.getCurrentUser());
    Assertions.assertFalse(Files.exists(local.sessions.resolve(OTHER)), "no session is created for the user of the zip");
    Assertions.assertEquals(0, local.sessions.toFile().list((dir, name) -> name.startsWith("import-")).length, "the temporary directory is removed");
  }

  @ParameterizedTest @ValueSource(booleans = {false, true}) public void testCorruptZipIsRefused(boolean adoptIdentity) throws Exception
  {
    Machine local = new Machine("local", LOCAL);
    local.write(LOCAL, "b.java.code", "from local");
    Path zip = Files.write(path.resolve("corrupt.zip"), new byte[] {1, 2, 3, 4, 5, 6, 7, 8});

    Assertions.assertThrows(IOException.class, () -> local.tracker.importSession(zip.toFile(), adoptIdentity));
    assertNothingImported(local);
    Mockito.verify(local.users, Mockito.never()).addUser(Mockito.any(), Mockito.anyString());
  }

  @ParameterizedTest @ValueSource(booleans = {false, true}) public void testZipWithoutGitRepositoryIsRefused(boolean adoptIdentity) throws Exception
  {
    Machine local = new Machine("local", LOCAL);
    local.stubAddUser();
    local.write(LOCAL, "b.java.code", "from local");
    Path zip = path.resolve("nogit.zip");
    try (OutputStream out = Files.newOutputStream(zip); ZipOutputStream zos = new ZipOutputStream(out)) {
      zos.putNextEntry(new ZipEntry(OTHER + "/a.java.code"));
      zos.write("from the zip".getBytes());
      zos.closeEntry();
    }

    Assertions.assertThrows(IOException.class, () -> local.tracker.importSession(zip.toFile(), adoptIdentity));
    assertNothingImported(local);
    Mockito.verify(local.users, Mockito.never()).addUser(Mockito.any(), Mockito.anyString());
  }

  @Test public void testFailureAfterAdoptingTheIdentityRestoresThePreviousUser() throws Exception
  {
    Machine local  = new Machine("local", LOCAL);
    Machine remote = new Machine("remote", OTHER);
    local.stubAddUser();
    local.stubSwitchToUser();
    Mockito.doThrow(new IllegalStateException("disk full")).when(local.users).removeUser(local.user);
    remote.write(OTHER, "a.java.code", "from the zip");
    local.write(LOCAL, "b.java.code", "from local");

    Assertions.assertThrows(IllegalStateException.class, () -> local.tracker.importSession(remote.export().toFile(), true));
    assertNothingImported(local);
    Mockito.verify(local.users).removeUser(Mockito.argThat(user -> OTHER.equals(user.getUserUUIDasString())));
  }

  /** The file exists on both machines, the local one being where it was written */
  private Machine[] machinesSharingAFile() throws Exception
  {
    Machine local  = new Machine("local", LOCAL);
    Machine remote = new Machine("remote", LOCAL);
    local.write(LOCAL, "a.java.code", "shared");
    remote.tracker.importSession(local.export().toFile(), false);
    return new Machine[] {local, remote};
  }

  @Test public void testFileDeletedOnOneSideOnlyStaysDeleted() throws Exception
  {
    Machine[] machines = machinesSharingAFile();
    Machine local = machines[0], remote = machines[1];
    local.delete(LOCAL, "a.java.code");

    local.tracker.importSession(remote.export().toFile(), false);

    Assertions.assertFalse(local.exists(LOCAL, "a.java.code"), "the zip did not touch the file since the common history: the deletion wins");
  }

  @Test public void testModifyDeleteConflictKeepsTheDeletionWhenItIsMoreRecent() throws Exception
  {
    Machine[] machines = machinesSharingAFile();
    Machine local = machines[0], remote = machines[1];
    remote.write(LOCAL, "a.java.code", "from the zip");
    Path zip = remote.export();
    Thread.sleep(1100); // the time of the commits has a resolution of one second
    local.delete(LOCAL, "a.java.code");

    local.tracker.importSession(zip.toFile(), false);

    Assertions.assertFalse(local.exists(LOCAL, "a.java.code"));
  }

  @Test public void testModifyDeleteConflictKeepsTheModificationWhenItIsMoreRecent() throws Exception
  {
    Machine[] machines = machinesSharingAFile();
    Machine local = machines[0], remote = machines[1];
    local.delete(LOCAL, "a.java.code");
    local.export(); // commits the deletion now
    Thread.sleep(1100);
    remote.write(LOCAL, "a.java.code", "from the zip");

    local.tracker.importSession(remote.export().toFile(), false);

    Assertions.assertEquals("from the zip", local.read(LOCAL, "a.java.code"));
  }

  @Test public void testModifyDeleteConflictKeepsTheDeletionOfTheZipWhenItIsMoreRecent() throws Exception
  {
    Machine[] machines = machinesSharingAFile();
    Machine local = machines[0], remote = machines[1];
    local.write(LOCAL, "a.java.code", "local change");
    local.export(); // commits the modification now
    Thread.sleep(1100);
    remote.delete(LOCAL, "a.java.code");

    local.tracker.importSession(remote.export().toFile(), false);

    Assertions.assertFalse(local.exists(LOCAL, "a.java.code"));
  }
}
