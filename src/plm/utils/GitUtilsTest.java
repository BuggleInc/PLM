package plm.utils;

import static java.nio.file.FileVisitResult.CONTINUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import plm.core.lang.ProgrammingLanguage;
import plm.core.log.Logger;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;

public class GitUtilsTest {

	private final File plmTestDir = new File(System.getProperty("user.home") + System.getProperty("file.separator")
			+ ".plm-test");
	private final File remotePlmTestDir = new File(System.getProperty("user.home") + System.getProperty("file.separator")
			+ ".remote-plm-test");

	private Git git;
	private GitUtils gitUtils;
	private File repoDirectory;
	private String userBranch;
	private Utils utils;

	public GitUtilsTest() {
		userBranch = UUID.randomUUID().toString();
		repoDirectory = new File(plmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		gitUtils = new GitUtils(new Locale("en"));
		utils = new Utils();

		Logger.debug("repoDirectory: "+ repoDirectory.getAbsolutePath());
	}

	@Before
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		try {
			gitUtils.initLocalRepository(repoDirectory);
			gitUtils.createInitialCommit();
		} catch (GitAPIException | IOException e) {
			System.err.println("An error occurred while initializing the repository...");
			e.printStackTrace();
		}
		git = utils.getGit(gitUtils);
	}

	@After
	public void tearDown() {
		gitUtils.dispose();
		utils.deleteRepo(repoDirectory);
	}

	@Test
	public void testCreateLocalUserBranchShouldCreateBranch() throws GitAPIException {
		Iterator<Ref> it;
		boolean branchCreated = false;

		it = git.branchList().call().iterator();
		while(it.hasNext()) {
			Ref ref = it.next();
			if(ref.getName().equals(userBranch)) {
				fail("The local branch shouldn't yet exist.");
			}
		}

		gitUtils.createLocalUserBranch(userBranch);

		it = git.branchList().call().iterator();
		while(!branchCreated && it.hasNext()) {
			Ref ref = it.next();
			if(ref.getName().equals("refs/heads/"+userBranch)) {
				branchCreated = true;
			}
		}

		if(!branchCreated) {
			fail("The branch "+userBranch+" should have been created...");
		}
	}

	@Test
	public void testCreateLocalUserBranchShouldCheckoutBranch() throws GitAPIException, IOException {
		String currentBranch;

		currentBranch = git.getRepository().getBranch();
		if(currentBranch.equals(userBranch)) {
			fail("Should not be on the "+userBranch+" yet...");
		}

		gitUtils.createLocalUserBranch(userBranch);

		currentBranch = git.getRepository().getBranch();
		if(!currentBranch.equals(userBranch)) {
			fail("Should be on the "+userBranch+" now...");
		}
	}

	@Test
	public void testCheckoutUserBranchShouldCheckoutBranch() throws IOException, GitAPIException {
		git.branchCreate().setName(userBranch).call();

		String currentBranch;

		currentBranch = git.getRepository().getBranch();
		if(!currentBranch.equals("master")) {
			fail("Should by default be on the master branch...");
		}

		gitUtils.checkoutUserBranch(userBranch);

		currentBranch = git.getRepository().getBranch();
		if(!currentBranch.equals(userBranch)) {
			fail("Should be on the "+userBranch+" now...");
		}
	}

	@Test
	public void testCheckoutUserBranchShouldNotCreateBranch() throws GitAPIException {
		Iterator<Ref> it;

		it = git.branchList().call().iterator();
		while(it.hasNext()) {
			Ref ref = it.next();
			if(ref.getName().equals(userBranch)) {
				fail("The local branch shouldn't yet exist.");
			}
		}

		if(gitUtils.checkoutUserBranch(userBranch)) {
			fail("The "+userBranch+" should not exist...");
		}
	}

	@Test
	public void testFetchBranchFromRemoteBranchShouldReturnTrueIfRemoteBranchExists () throws GitAPIException, IOException {
		File remoteRepoDirectory = new File(remotePlmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		GitUtils remoteGitUtils = new GitUtils(new Locale("en"));
		remoteGitUtils.initLocalRepository(remoteRepoDirectory);
		remoteGitUtils.createInitialCommit();
		remoteGitUtils.createLocalUserBranch(userBranch);
		Git remoteGit = utils.getGit(remoteGitUtils);

		generateCommits(remoteGit);

		remoteGitUtils.checkoutUserBranch("master");

		String remoteUrl = "file://"+remoteGit.getRepository().getDirectory().getAbsolutePath();

		gitUtils.setUpRepoConfig(remoteUrl, userBranch);

		boolean success = gitUtils.fetchBranchFromRemoteBranch(userBranch);

		remoteGitUtils.dispose();
		utils.deleteRepo(remotePlmTestDir);

		if(!success) {
			fail("Should have been able to fetch the remote branch");
		}
	}

	@Test
	public void testFetchBranchFromRemoteBranchShouldReturnFalseIfRemoteBranchNotExists () throws GitAPIException, IOException {
		File remoteRepoDirectory = new File(remotePlmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		GitUtils remoteGitUtils = new GitUtils(new Locale("en"));
		remoteGitUtils.initLocalRepository(remoteRepoDirectory);
		remoteGitUtils.createInitialCommit();
		Git remoteGit = utils.getGit(remoteGitUtils);

		String remoteUrl = "file://"+remoteGit.getRepository().getDirectory().getAbsolutePath();

		gitUtils.setUpRepoConfig(remoteUrl, userBranch);
		Logger.debug("Try to fetch from "+remoteUrl);
		boolean success = gitUtils.fetchBranchFromRemoteBranch(userBranch);

		remoteGitUtils.dispose();
		utils.deleteRepo(remotePlmTestDir);

		if(success) {
			fail("Should not have been able to fetch the remote branch since it doesn't exist...");
		}
	}

	@Test
	public void testMergeRemoteIntoLocalBranchShouldSynchronizeBranches() throws GitAPIException, IOException {
		File remoteRepoDirectory = new File(remotePlmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		GitUtils remoteGitUtils = new GitUtils(new Locale("en"));
		remoteGitUtils.initLocalRepository(remoteRepoDirectory);
		remoteGitUtils.createInitialCommit();
		remoteGitUtils.createLocalUserBranch(userBranch);
		Git remoteGit = utils.getGit(remoteGitUtils);

		generateCommits(remoteGit);
		RevCommit remoteCommit = remoteGit.log().call().iterator().next();

		String remoteUrl = "file://"+remoteGit.getRepository().getDirectory().getAbsolutePath();

		gitUtils.setUpRepoConfig(remoteUrl, userBranch);
		gitUtils.createLocalUserBranch(userBranch);
		if(!gitUtils.fetchBranchFromRemoteBranch(userBranch)) {
			fail("Should have been able to fetch remote branch...");
		}
		try {
			gitUtils.mergeRemoteIntoLocalBranch(userBranch);
		} catch (Exception e) {
			System.err.println("An error occurred while merging the branches...");
			e.printStackTrace();
			fail("No exception should have been thrown by mergeRemoteIntoLocalBranch...");
		}

		RevCommit commit = git.log().call().iterator().next();

		remoteGitUtils.dispose();
		utils.deleteRepo(remotePlmTestDir);

		assertEquals(commit.getId().getName(),
				remoteCommit.getId().getName());
	}

	@Test
	public void testMergeRemoteIntoLocalBranchShouldHandleConflicts() throws GitAPIException, IOException, InterruptedException {
		File localRepoDirectory = new File(plmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		File remoteRepoDirectory = new File(remotePlmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		GitUtils remoteGitUtils = new GitUtils(new Locale("en"));
		remoteGitUtils.initLocalRepository(remoteRepoDirectory);
		remoteGitUtils.createInitialCommit();
		remoteGitUtils.createLocalUserBranch(userBranch);
		Git remoteGit = utils.getGit(remoteGitUtils);

		String expectedContent1 = utils.generateRandomString(32);
		generateGitConflict(remoteGit, git, remoteRepoDirectory, localRepoDirectory, "test1", utils.generateRandomString(32), expectedContent1);

		String expectedContent2 = utils.generateRandomString(32);
		generateGitConflict(git, remoteGit, localRepoDirectory, remoteRepoDirectory, "test2", utils.generateRandomString(32), expectedContent2);

		String remoteUrl = "file://"+remoteGit.getRepository().getDirectory().getAbsolutePath();

		gitUtils.setUpRepoConfig(remoteUrl, userBranch);
		gitUtils.createLocalUserBranch(userBranch);
		if(!gitUtils.fetchBranchFromRemoteBranch(userBranch)) {
			fail("Should have been able to fetch remote branch...");
		}
		try {
			gitUtils.mergeRemoteIntoLocalBranch(userBranch);
		} catch (Exception e) {
			System.err.println("An error occurred while merging the branches...");
			e.printStackTrace();
			fail("No exception should have been thrown by mergeRemoteIntoLocalBranch...");
		}

		String path1 = localRepoDirectory + System.getProperty("file.separator") + "test1";
		String path2 = localRepoDirectory + System.getProperty("file.separator") + "test2";
		assertEquals(expectedContent1, utils.getFileContent(path1));
		assertEquals(expectedContent2, utils.getFileContent(path2));
	}

	@Test
	public void testPushChangesShouldReturnTrueIfNoConflicts() throws GitAPIException, IOException, InterruptedException {
		File remoteRepoDirectory = new File(remotePlmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		GitUtils remoteGitUtils = new GitUtils(new Locale("en"));
		remoteGitUtils.initLocalRepository(remoteRepoDirectory);
		remoteGitUtils.createInitialCommit();
		Git remoteGit = utils.getGit(remoteGitUtils);

		String remoteUrl = "file://"+remoteGit.getRepository().getDirectory().getAbsolutePath();

		gitUtils.setUpRepoConfig(remoteUrl, userBranch);
		gitUtils.createLocalUserBranch(userBranch);

		generateCommits(git);

		ProgressMonitor progress = NullProgressMonitor.INSTANCE;
		boolean success = gitUtils.pushChanges(userBranch, progress, null);

		remoteGitUtils.dispose();
		utils.deleteRepo(remotePlmTestDir);

		assertTrue(success);
	}

	@Test
	public void testPushChangesShouldReturnFalseIfConflictsDetected() throws GitAPIException, IOException, InterruptedException {
		File localRepoDirectory = new File(plmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		File remoteRepoDirectory = new File(remotePlmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		GitUtils remoteGitUtils = new GitUtils(new Locale("en"));
		remoteGitUtils.initLocalRepository(remoteRepoDirectory);
		remoteGitUtils.createInitialCommit();
		remoteGitUtils.createLocalUserBranch(userBranch);
		Git remoteGit = utils.getGit(remoteGitUtils);

		String expectedContent1 = utils.generateRandomString(32);
		generateGitConflict(remoteGit, git, remoteRepoDirectory, localRepoDirectory, "test1", utils.generateRandomString(32), expectedContent1);

		String expectedContent2 = utils.generateRandomString(32);
		generateGitConflict(git, remoteGit, localRepoDirectory, remoteRepoDirectory, "test2", utils.generateRandomString(32), expectedContent2);

		String remoteUrl = "file://"+remoteGit.getRepository().getDirectory().getAbsolutePath();

		gitUtils.setUpRepoConfig(remoteUrl, userBranch);
		gitUtils.createLocalUserBranch(userBranch);

		generateCommits(git);

		ProgressMonitor progress = NullProgressMonitor.INSTANCE;
		boolean success = gitUtils.pushChanges(userBranch, progress, null);

		remoteGitUtils.dispose();
		utils.deleteRepo(remotePlmTestDir);

		assertFalse(success);
	}

	private void generateCommits(Git git) {
		for(int i=0; i<100; i++) {
			try {
				git.commit().setMessage(utils.generateRandomString(32))
						.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
						.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
						.call();
			} catch (GitAPIException e) {
				e.printStackTrace();
			}
		}
	}

	private void generateGitConflict(Git git1, Git git2, File dir1, File dir2, String name, String droppedContent, String expectedContent) throws GitAPIException, InterruptedException {
		utils.generateFile(dir1, name, droppedContent);
		git1.add().addFilepattern(".").call();
		git1.commit().setMessage("Add "+name).call();
		Thread.sleep(1000);
		utils.generateFile(dir2, name, expectedContent);
		git2.add().addFilepattern(".").call();
		git2.commit().setMessage("Add "+name).call();
	}
}

class Utils {
	
	private final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private Random rnd = new Random();
	
	public void deleteRepo(File repoDirectory) {
		try {
			Files.walkFileTree(repoDirectory.toPath(), new SimpleFileVisitor<Path>() {
				 
			      @Override
			      public FileVisitResult visitFile(Path file,
			              BasicFileAttributes attrs) throws IOException {
			 
			          Files.delete(file);
			          return CONTINUE;
			      }
			 
			      @Override
			      public FileVisitResult postVisitDirectory(Path dir,
			              IOException exc) throws IOException {
			 
			          if (exc == null) {
			              Files.delete(dir);
			              return CONTINUE;
			          } else {
			              throw exc;
			          }
			      }
			 
			  });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String generateRandomString(int len) {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<len; i++) {
			sb.append(characters.charAt(rnd.nextInt(characters.length())));
		}
		return sb.toString();
	}
	
	public void generateFile(File repoDir, String name, String content) {
		File file = new File(repoDir.getAbsolutePath() + System.getProperty("file.separator") + name);
		FileWriter fw;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getFileContent(String path) throws IOException {
		File file = new File(path);
		return new String(Files.readAllBytes(file.toPath()));
	}
	
	public String getFileContent(File repoDir, String userUUID, Exercise exo, ExecutionProgress lastResult, String suffix) throws IOException {
		return getFileContent(getFilePath(repoDir, userUUID, exo, lastResult, suffix));
	}
	
	public String getFilePath(File repoDir, String userUUID, Exercise exo, ProgrammingLanguage pl, String suffix) {
		return repoDir.getPath() + System.getProperty("file.separator") + 
				userUUID + System.getProperty("file.separator") + 
				exo.getId() + "." + pl.getExt() + suffix;
	}
	
	public String getFilePath(File repoDir, String userUUID, Exercise exo, ExecutionProgress lastResult, String suffix) {
		return getFilePath(repoDir, userUUID, exo, lastResult.language, suffix);
	}
	
	public Git getGit(GitUtils gitUtils) {
		Field f = null;
		try {
			f = gitUtils.getClass().getDeclaredField("git");
			f.setAccessible(true);
			return (Git) f.get(gitUtils);
		} catch (NoSuchFieldException | SecurityException e) {
			System.err.println("An error occurred while retrieving gitUtils' git field...");
			e.printStackTrace();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			System.err.println("An error occurred while retrieving gitUtils' git field's value...");
			e.printStackTrace();
		}
		return null;
	}
}

