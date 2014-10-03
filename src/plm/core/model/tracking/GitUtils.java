package plm.core.model.tracking;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.swing.SwingWorker;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.ResolveMerger.MergeFailureReason;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import plm.core.model.Game;
import sun.misc.Regexp;

public class GitUtils {

	private Git git;

	private String repoName = Game.getProperty("plm.git.server.username");
	private String repoPassword = Game.getProperty("plm.git.server.password");

	private static boolean currentlyPushing = false;

	// TODO: remove git.close() ?
	
	public GitUtils() {
		super();
	}
	
	@Deprecated
	public GitUtils(Git git) throws IOException, GitAPIException {
		this.git = git;
	}
	
	public void initLocalRepository(File repoDirectory, String repoUrl) throws GitAPIException, IOException {
		git = Git.init().setDirectory(repoDirectory).call();
/*
		git.commit().setMessage("Empty initial commit")
		.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
		.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
		.call();		
	*/	
		git.close();
	}
		
	public boolean fetchBranchFromRemoteBranch(File repoDirectory, String repoUrl, String userBranchHash) throws IOException,  InvalidRemoteException, GitAPIException {		
		String repoName = "origin";
		
		git = Git.open(repoDirectory);	
		
		try {
			StoredConfig cfg = git.getRepository().getConfig();
			cfg.setString("remote", repoName, "url", repoUrl);
			cfg.setString("remote", repoName, "fetch", "+refs/heads/"+userBranchHash+":refs/remotes/"+repoName+"/"+userBranchHash);
			cfg.save();		

			System.out.println(Game.i18n.tr("Retrieving your session from the servers..."));
			FetchResult res = git.fetch().call();
			System.err.println("FetchResult: "+res); // TODO: remove it
		} catch (GitAPIException ex) {
			// FIXME: We should display the stacktrace sometimes
			/*
			if(Game.getInstance().isDebugEnabled()) {
				ex.printStackTrace();
			}
			*/
			if (ex.getMessage().equals("Remote does not have refs/heads/"+userBranchHash+" available for fetch.")) {
				return false;
			}
			return false;
		} finally {
			git.close();
		}
		
		return true;
	}
	
	public void createLocalUserBranch(File repoDirectory, String repoName, String userBranchHash) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		git = Git.open(repoDirectory);
				
		// We must create an initial commit before creating a specific branch for the user
		git.commit().setMessage("Empty initial commit")
			.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
			.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
			.call();
					
		git.checkout().setCreateBranch(true).setName(userBranchHash).call();
		git.close();
	}

	public void checkoutUserBranch(File repoDirectory, String userBranchHash, boolean create) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		git = Git.open(repoDirectory);
		try {
			git.checkout().setCreateBranch(create).setName(userBranchHash).setStartPoint("refs/remotes/origin/"+userBranchHash).call();
		} finally {
			git.close();
		}
	}
	
	public void pullExistingBranch(File repoDirectory, String userBranchHash) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		// TODO: we should protect this method call from concurrent execution with pushing methods
		git = Git.open(repoDirectory);
		try {
			git.fetch().setCheckFetchedObjects(true).setRefSpecs(new RefSpec("+refs/heads/"+userBranchHash+":refs/remotes/origin/"+userBranchHash)).call();
		} catch (GitAPIException ex) {
			System.err.println(Game.i18n.tr("Can't retrieve data stored on server."));
			return;
		}
		
		try {
			MergeResult res = git.merge().setCommit(true).setFastForward(MergeCommand.FastForwardMode.FF).setStrategy(MergeStrategy.THEIRS).include(git.getRepository().getRef("refs/remotes/origin/"+userBranchHash)).call();
			
			// TODO: replace the current merge strategy with a better one
			/*
			MergeResult res = git.merge().setCommit(true).setFastForward(MergeCommand.FastForwardMode.FF).setStrategy(MergeStrategy.RECURSIVE).include(git.getRepository().getRef("refs/remotes/origin/"+userBranchHash)).call();
			
			if(res.equals(MergeResult.MergeStatus.FAST_FORWARD)) {
				System.out.println(Game.i18n.tr("last session data successfully retrieved"));
			}
			else if(res.equals(MergeResult.MergeStatus.MERGED)) {
				System.out.println(Game.i18n.tr("last session data successfully merged"));
			}
			else if(res.equals(MergeResult.MergeStatus.CONFLICTING)) {
				System.out.println(Game.i18n.tr("Conflicts have been detected while synchronizing with last session data, trying to resolve it..."));
				Map<String, int[][]> allConflicts = res.getConflicts();
				for (String path : allConflicts.keySet()) {
					System.out.println("Conflicts detected in file: "+path);
					// TODO: check if 
					// - summary: delete it
					// - others cases: get last commit editing the files
				}
			}
			else if(res.equals(MergeResult.MergeStatus.FAILED)) {
				// TODO: handle this case
				System.out.println(Game.i18n.tr("Cancelled the merge operation because of the following failures:"));
				Map<String, MergeFailureReason> allFailures = res.getFailingPaths();
				for(String path : allFailures.keySet()) {
					System.out.println(path + " : " + allFailures.get(path));
				}
			}
			*/
			
			if(res.equals(MergeResult.MergeStatus.FAST_FORWARD)) {
				System.out.println(Game.i18n.tr("last session data successfully retrieved"));
			}
			else if(res.equals(MergeResult.MergeStatus.MERGED)) {
				System.out.println(Game.i18n.tr("last session data successfully merged"));
			}
			else {
				System.out.println(res.getMergeStatus()); // TODO: to remove
			}
		} catch (Exception ex) {
			System.err.println(Game.i18n.tr("Can't merge data retrieved from server with local session data."));
			throw ex;
		} finally {
			git.close();
		}
	}

	/** Push the data to the github servers. 
	 * 
	 * Beware, you don't want to do that too much to not overload the github servers (see maybePushToUserBranch() below)
	 */
	public void forcefullyPushToUserBranch(String userBranchHash, ProgressMonitor progress) {
		synchronized(GitUtils.class) {
			currentlyPushing = true;
		}
		
		// credentials
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(repoName, repoPassword);

		// push
		PushCommand pc = git.push().setRefSpecs(new RefSpec("+refs/heads/"+userBranchHash+":refs/remotes/origin/"+userBranchHash)).setProgressMonitor(progress);
		
		// TODO: replace setForce with a push, if error then pull before pushing again
		pc.setCredentialsProvider(cp).setForce(true).setPushAll();
		try {
			boolean error = false;
			for (PushResult pr: pc.call()) { 
				if (!pr.getMessages().equals("")) {
					error = true;
					System.err.println("Pushed to "+pr.getURI()+". Message: "+pr.getMessages());
				}
			}
			if (! error) {
				System.out.println(Game.i18n.tr("Your session has been successfully saved into the clouds."));
			}
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			System.out.println(Game.i18n.tr("Cannot synchronize your session with the servers (network down)."));
			if (Game.getInstance().isDebugEnabled())
				e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}  finally {	
			synchronized(GitUtils.class) {
				currentlyPushing = false;
			}
		}
	}
	
	/** try to push to the git servers, if no request is pending
	 * 
	 * We had issues with the git servers that don't like having more than 1,200 concurrent push requests 
	 * from the same NATed IP address, and our PLM-data repo got temporarily disabled as a result. So now, 
	 * we only push at most once every 30 minutes (unless when the client quits, in which case 
	 * forcefullyPushToUserBranch() is used to get the data immediately out). 
	 * 
	 * Here is my understanding of the github incident:  We had 100 students using the PLM from the same 
	 * external IP address (our school is in a NATed network) at the same time for about 4 hours. Each 
  	 * of them was issuing git push requests each time that the "run" button is hit. Those requests where 
  	 * handled in a new thread to improve the UI reactivity. Since all branches derive from the master
     * branch, it imposes the server to lock that branch for the commit to proceed. Things went seriously 
     * wrong at some point, probably because of the lock. As a result, the github servers got up to 1200
     * concurrent push requests, triggering an alert that got the PLM-data repo to get temporarily disabled.
     * 
	 */
	public void maybePushToUserBranch(final String userBranchHash, final ProgressMonitor progress) {	
		synchronized(GitUtils.class) {
			if (currentlyPushing) // Don't try to push if we're already pushing (don't overload github)
				return;
			currentlyPushing = true;
		}
		
		new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() {
				// Reduce the load on the github servers by not pushing more often that once every 20 minutes
				try {
					Thread.sleep(1 /*mn*/ * 60 /* ->sec */ * 1000 /* ->milli */);
				} catch (InterruptedException e1) {
					/* Ok, that's fine, what ever */
				}

				// Do it now, and allow next request to occur
				forcefullyPushToUserBranch(userBranchHash, progress);
				
				return null;
			}
		}.execute();
	}

	// Helper methods
	public static String sha1(String input)  {
		MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(input.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void openRepo(File repoDir) throws IOException {
		// If the repo is already opened, do nothing
		if(git != null && git.getRepository().getDirectory().equals(repoDir)) {
			return;
		}
		// Close the previous repo
		if(git != null) {
			git.close();
		}
		git = Git.open(repoDir);
	}

	public void commit(String msg) throws NoHeadException, NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException, WrongRepositoryStateException, GitAPIException {
		this.git.commit().setMessage(msg)
		.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
		.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
		.call();
	}

	public void addFiles() throws NoFilepatternException, GitAPIException {
		git.add().addFilepattern(".").call();
	}

	public void seqAddFilesToPush(String commitMsg, String userBranch,
			ProgressMonitor progress) throws NoFilepatternException, GitAPIException {
		// run the add-call
		addFiles();

		// and then commit the changes
		commit(commitMsg);

		// push to the remote repository
		maybePushToUserBranch(userBranch, progress);
	}

	public void dispose() {
		repoName = null;
		repoPassword = null;
		git.close();
	}
	
	public Ref getRepoRef(String branch) throws IOException {
		return git.getRepository().getRef(branch);
	}
}
