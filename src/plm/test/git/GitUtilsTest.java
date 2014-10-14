package plm.test.git;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
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
	private String repoUrl;
	private Utils utils;
	
	public GitUtilsTest() {
		Game.i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",FileUtils.getLocale(), I18nFactory.FALLBACK);
		
		testUser = new User("testUser");
		localBranchName = testUser.getUserUUIDasString();
		repoDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator")
				+ ".plm-test" + System.getProperty("file.separator") + localBranchName);
		Game.loadProperties();
		repoUrl = Game.getProperty("plm.git.server.url");
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
			gitUtils.createLocalUserBranch(repoDirectory, localBranchName);
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
			gitUtils.createLocalUserBranch(repoDirectory, localBranchName);
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
	public void testCheckoutUserBranchShouldCheckoutBranch() throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {		
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
		
		try {
			gitUtils.checkoutUserBranch(repoDirectory, localBranchName, false);
		} catch (IOException e) {
			System.err.println("An error occurred while checking out the local branch...");
			e.printStackTrace();
			fail("Should be able to checkout the local branch...");
		}
		
		currentBranch = git.getRepository().getBranch();
		if(!currentBranch.equals(localBranchName)) {
			fail("Should be on the "+localBranchName+" now...");
		}
	}
	
	@Test (expected = RefNotFoundException.class)
	public void testCheckoutUserBranchShouldNotCreateBranch() throws GitAPIException, IOException {
		Iterator<Ref> it;
		
		it = git.branchList().call().iterator();
		while(it.hasNext()) {
			Ref ref = it.next();
			if(ref.getName().equals(localBranchName)) {
				fail("The local branch shouldn't yet exist.");
			}
		}
		
		gitUtils.checkoutUserBranch(repoDirectory, localBranchName, false);
		
		String currentBranch = "";
		try{
			currentBranch = git.getRepository().getBranch();
		}
		catch (IOException e) {
			System.err.println("An error occurred while retrieving the current branch's name...");
			e.printStackTrace();
			fail("Should not have gone that far in the execution...");
		}
		
		if(currentBranch.equals(localBranchName)) {
			fail("The "+localBranchName+" should not exist...");
		}
	}
	
	@Test
	public void testFetchBranchFromRemoteBranchShouldReturnTrueIfExisting() throws InvalidRemoteException, IOException, GitAPIException {
		String userBranchHash = "PLM6dd35951d44843079580ea78409560139e7e4503"; // Existing branch
		boolean successfulFetch = gitUtils.fetchBranchFromRemoteBranch(repoDirectory, repoUrl, userBranchHash);
		assertTrue("Fetching a existing remote branch should be OK...", successfulFetch);
	}
	
	@Test
	public void testFetchBranchFromRemoteBranchShouldReturnFalseIfNotExisting() throws InvalidRemoteException, IOException, GitAPIException {
		String userBranchHash = utils.generateRandomString(32);
		boolean successfulFetch = gitUtils.fetchBranchFromRemoteBranch(repoDirectory, repoUrl, userBranchHash);
		assertFalse("Fetching a existing remote branch should be OK...", successfulFetch);
	}
	
	@Test
	public void testFetchBranchFromRemoteBranchShouldReturnFalseIfNoInternet() {
		// TODO: check if possible to disable internet connexion
	}
	
	
}
