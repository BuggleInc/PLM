package plm.test.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Semaphore;

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

import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.tracking.GitUtils;

public class GitUtilsTest {

	final private String trackUserProperty = "plm.git.track.user";

	private final File plmTestDir = new File(System.getProperty("user.home") + System.getProperty("file.separator")
			+ ".plm-test");
	private final File remotePlmTestDir = new File(System.getProperty("user.home") + System.getProperty("file.separator")
			+ ".remote-plm-test");
	
	private Git git;
	private GitUtils gitUtils;
	private File repoDirectory;
	private String userBranch;
	private Utils utils;
	private String oldTrackUserProperty;
	private Game game;
	
	public GitUtilsTest() {
		userBranch = UUID.randomUUID().toString();
		game = new Game(userBranch, mock(LogHandler.class), new Locale("en"), "Java", false);
		oldTrackUserProperty = Game.getProperty(trackUserProperty);
		
		repoDirectory = new File(plmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		gitUtils = new GitUtils("");
		gitUtils.setGame(game);
		utils = new Utils();
		
		game.getLogger().log("repoDirectory: "+ repoDirectory.getAbsolutePath());
	}
	
	@Before 
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Game.setProperty(trackUserProperty, "true");
		try {
			game.setTrackUser(false);
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
		Game.setProperty(trackUserProperty, oldTrackUserProperty);
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
		game.setTrackUser(true);
		
		GitUtils remoteGitUtils = generateRemoteGitUtils();
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
		GitUtils remoteGitUtils = generateRemoteGitUtils();
		Git remoteGit = utils.getGit(remoteGitUtils);
	
		String remoteUrl = "file://"+remoteGit.getRepository().getDirectory().getAbsolutePath();
		
		gitUtils.setUpRepoConfig(remoteUrl, userBranch);
		game.getLogger().log("Try to fetch from "+remoteUrl);
		boolean success = gitUtils.fetchBranchFromRemoteBranch(userBranch);
		
		remoteGitUtils.dispose();
		utils.deleteRepo(remotePlmTestDir);
		
		if(success) {
			fail("Should not have been able to fetch the remote branch since it doesn't exist...");
		}
	}
	
	@Test
	public void testMergeRemoteIntoLocalBranchShouldSynchronizeBranches() throws GitAPIException, IOException {
		game.setTrackUser(true);
		
		GitUtils remoteGitUtils = generateRemoteGitUtils();
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
		game.setTrackUser(true);
		
		File localRepoDirectory = new File(plmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		File remoteRepoDirectory = new File(remotePlmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		GitUtils remoteGitUtils = generateRemoteGitUtils();
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
		GitUtils remoteGitUtils = generateRemoteGitUtils();
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
		GitUtils remoteGitUtils = generateRemoteGitUtils();
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

	@Test
	public void testParallelCommitAndPush() throws GitAPIException, IOException, InterruptedException {
		gitUtils.createLocalUserBranch(userBranch);

		final ProgressMonitor progress = NullProgressMonitor.INSTANCE;

		Thread tCommit = new Thread(new Runnable() {
			public void run() {
				generateCommits(git);
				for(int i=0; i<100; i++) {
			    	generateCommits(git);
				}
		     }
		});
		Thread tPush = new Thread(new Runnable() {
			public void run() {
				// Setup remote repository
				GitUtils remoteGitUtils = generateRemoteGitUtils();

				Git remoteGit = utils.getGit(remoteGitUtils);
				String remoteUrl = "file://"+remoteGit.getRepository().getDirectory().getAbsolutePath();

				// Setup new gitUtils on the same local repository
				// Will push the commits generated by the other thread
				GitUtils gitUtilsBis = new GitUtils(userBranch);
		    	gitUtilsBis.setGame(game);
		    	try {
		    		gitUtilsBis.openRepo(repoDirectory);
		    	} catch (IOException e) {
					e.printStackTrace();
		    		fail("Error while opening repository twice");
				}
		    	gitUtilsBis.setUpRepoConfig(remoteUrl, userBranch);
		    	for(int i=0; i<100; i++) {
		    		gitUtils.pushChanges(userBranch, progress, null);
		    	}
		    	remoteGitUtils.dispose();
		     }
		});
		tCommit.start();
		tPush.start();

		tCommit.join();
		tPush.join();

		utils.deleteRepo(remotePlmTestDir);

		assertTrue(true);
	}

	private GitUtils generateRemoteGitUtils() {
		File remoteRepoDirectory = new File(remotePlmTestDir.getAbsolutePath() + System.getProperty("file.separator") + userBranch);
		GitUtils remoteGitUtils = new GitUtils("");
		try {
			remoteGitUtils.initLocalRepository(remoteRepoDirectory);
			remoteGitUtils.createInitialCommit();
			remoteGitUtils.createLocalUserBranch(userBranch);
		} catch (GitAPIException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return remoteGitUtils;
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
