package plm.test;

import static java.nio.file.FileVisitResult.CONTINUE;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import plm.core.model.User;
import plm.core.model.tracking.GitUtils;

public class GitUtilsTest {

	private GitUtils gitUtils;
	private File repoDirectory;
	private User testUser;
	private String localBranchName;
	
	public GitUtilsTest() {
		testUser = new User("testUser");
		localBranchName = testUser.getUserUUIDasString();
		repoDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".plm-test" + System.getProperty("file.separator") + localBranchName);
		gitUtils = new GitUtils();
		
		System.out.println("repoDirectory: "+ repoDirectory.getAbsolutePath());
	}
	
	@Before 
	public void setUp() {
		try {
			gitUtils.initLocalRepository(repoDirectory);
		} catch (GitAPIException | IOException e) {
			System.err.println("An error occurred while initializing the repository...");
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown() {
		gitUtils.dispose();
		deleteRepo(repoDirectory);
	}

	@Test
	public void testCreateLocalUserBranchShouldCreateBranch() throws GitAPIException {
		Git git = gitUtils.getGit();
		
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
		Git git = gitUtils.getGit();
		
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
		Git git = gitUtils.getGit();
		
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
		Git git = gitUtils.getGit();
		
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
	
	private void deleteRepo(File repoDirectory) {
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
}
