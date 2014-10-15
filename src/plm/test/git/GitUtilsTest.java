package plm.test.git;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.model.Game;
import plm.core.model.User;
import plm.core.model.tracking.GitUtils;
import plm.core.utils.FileUtils;

public class GitUtilsTest {

	private Git git;
	private GitUtils gitUtils;
	private File repoDirectory;
	private User testUser;
	private String localBranchName;
	private Utils utils;
	
	public GitUtilsTest() {
		Game.loadProperties();
		Game.i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",FileUtils.getLocale(), I18nFactory.FALLBACK);
		
		testUser = new User("testUser");
		localBranchName = testUser.getUserUUIDasString();
		repoDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator")
				+ ".plm-test" + System.getProperty("file.separator") + localBranchName);
		gitUtils = new GitUtils();
		utils = new Utils();
		
		System.out.println("repoDirectory: "+ repoDirectory.getAbsolutePath());
	}
	
	@Before 
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		try {
			gitUtils.initLocalRepository(repoDirectory);
		} catch (GitAPIException | IOException e) {
			System.err.println("An error occurred while initializing the repository...");
			e.printStackTrace();
		}
		Field f = gitUtils.getClass().getDeclaredField("git"); //NoSuchFieldException
		f.setAccessible(true);
		git = (Git) f.get(gitUtils); //IllegalAccessException
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
			if(ref.getName().equals(localBranchName)) {
				fail("The local branch shouldn't yet exist.");
			}
		}
		
		try {
			gitUtils.createLocalUserBranch(localBranchName);
		} catch (IOException e) {
			System.err.println("An error occurred while creating the local branch...");
			e.printStackTrace();
			fail("Should be able to create the local branch...");
		}
		
		it = git.branchList().call().iterator();
		while(!branchCreated && it.hasNext()) {
			Ref ref = it.next();
			if(ref.getName().equals("refs/heads/"+localBranchName)) {
				branchCreated = true;
			}
		}
		
		if(!branchCreated) {
			fail("The branch "+localBranchName+" should have been created...");
		}
	}
	
	@Test
	public void testCreateLocalUserBranchShouldCheckoutBranch() throws GitAPIException, IOException {		
		String currentBranch;
		
		currentBranch = git.getRepository().getBranch();
		if(currentBranch.equals(localBranchName)) {
			fail("Should not be on the "+localBranchName+" yet...");
		}
		
		try {
			gitUtils.createLocalUserBranch(localBranchName);
		} catch (IOException e) {
			System.err.println("An error occurred while creating the local branch...");
			e.printStackTrace();
			fail("Should be able to create the local branch...");
		}
		
		currentBranch = git.getRepository().getBranch();
		if(!currentBranch.equals(localBranchName)) {
			fail("Should be on the "+localBranchName+" now...");
		}
	}
	
	@Test
	public void testCheckoutUserBranchShouldCheckoutBranch() throws IOException, GitAPIException {		
		git.commit().setMessage("Empty initial commit")
		.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
		.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
		.call();
		
		git.branchCreate().setName(localBranchName).call();
		
		String currentBranch;
		
		currentBranch = git.getRepository().getBranch();
		if(!currentBranch.equals("master")) {
			fail("Should by default be on the master branch...");
		}
		
		gitUtils.checkoutUserBranch(localBranchName);
		
		currentBranch = git.getRepository().getBranch();
		if(!currentBranch.equals(localBranchName)) {
			fail("Should be on the "+localBranchName+" now...");
		}
	}
	
	public void testCheckoutUserBranchShouldNotCreateBranch() throws GitAPIException {
		Iterator<Ref> it;
		
		it = git.branchList().call().iterator();
		while(it.hasNext()) {
			Ref ref = it.next();
			if(ref.getName().equals(localBranchName)) {
				fail("The local branch shouldn't yet exist.");
			}
		}
		
		if(gitUtils.checkoutUserBranch(localBranchName)) {
			fail("The "+localBranchName+" should not exist...");
		}
	}
	/*
	@Test
	public void testFetchBranchFromRemoteBranchShouldReturnTrueIfExisting() throws IOException, GitAPIException {
		String userBranchHash = "PLM6dd35951d44843079580ea78409560139e7e4503"; // Existing branch
		boolean successfulFetch = gitUtils.fetchBranchFromRemoteBranch(repoUrl, userBranchHash);
		assertTrue("Fetching a existing remote branch should be OK...", successfulFetch);
	}
	
	@Test
	public void testFetchBranchFromRemoteBranchShouldReturnFalseIfNotExisting() throws IOException, GitAPIException {
		String userBranchHash = utils.generateRandomString(32);
		boolean successfulFetch = gitUtils.fetchBranchFromRemoteBranch(repoUrl, userBranchHash);
		assertFalse("Fetching a non-existing remote branch should NOT be OK...", successfulFetch);
	}
	
	@Test
	public void testPullNonExistingBranchShouldNotCrash() throws IOException, GitAPIException {
		String userBranchHash = utils.generateRandomString(32);
		gitUtils.createLocalUserBranch(userBranchHash);
		gitUtils.fetchBranchFromRemoteBranch(repoUrl, userBranchHash);
	}
	
	@Test
	public void testPullExistingBranchShouldSynchronizeBranches() throws IOException, GitAPIException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// FIXME: should look for a better way to initialize repo
		
		String userBranchHash = "PLM6dd35951d44843079580ea78409560139e7e4503"; // Existing branch
		gitUtils.fetchBranchFromRemoteBranch(repoDirectory, repoUrl, userBranchHash);
		gitUtils.pullExistingBranch(repoDirectory, userBranchHash);
		
		ObjectId remote = git.getRepository().resolve("origin/"+userBranchHash);
		
		// Should only skip 1 commit but since we're creating a local branch here,
		// Have an additional commit to skip
		RevCommit lastCommitLocal = git.log().setSkip(1).setMaxCount(1).call().iterator().next();
		System.err.println(lastCommitLocal.getShortMessage()); // TODO: remove it
		RevCommit lastCommitRemote = git.log().add(remote).setMaxCount(1).call().iterator().next();
		System.err.println(lastCommitRemote.getShortMessage()); // TODO: remove it
		
		if( !(lastCommitLocal.equals(lastCommitRemote))) {
			fail("Local branch and remote one should be synchronized...");
		}
		
	}
	*/
}
