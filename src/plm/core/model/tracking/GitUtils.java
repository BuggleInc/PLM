package plm.core.model.tracking;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;

import javax.swing.SwingWorker;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.api.Status;
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
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.ResolveMerger.MergeFailureReason;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import plm.core.model.Game;
import plm.core.model.LogHandler;

public class GitUtils {

	private Git git;
	private File repoDir;
	private Game game;

	private String username = "";
	private String password = Game.getProperty("plm.git.server.password");

	private static boolean currentlyPushing = false;

	public GitUtils(String username) {
		this.username = username;
	}

	public void setGame(Game g) {
		this.game = g;
	}

	public File getRepoDir() {
		return repoDir;
	}

	public void initLocalRepository(File repoDirectory) throws GitAPIException, IOException {
		git = Git.init().setDirectory(repoDirectory).call();
	}

	public void setUpRepoConfig(String repoUrl, String userBranchHash) {
		String repoName = "origin";

		StoredConfig cfg = git.getRepository().getConfig();
		cfg.setString("remote", repoName, "url", repoUrl);
		cfg.setString("remote", repoName, "fetch", "+refs/heads/"+userBranchHash+":refs/remotes/"+repoName+"/"+userBranchHash);
		try {
			cfg.save();
		} catch (IOException e) {
			game.getLogger().log(LogHandler.ERROR, getGame().i18n.tr("An error occurred while configuring the repository..."));
			e.printStackTrace();
		}
	}

	public boolean fetchBranchFromRemoteBranch(String userBranchHash) throws InvalidRemoteException, GitAPIException {
		if(!getGame().getTrackUser()) {
			return false;
		}
		game.getLogger().log(LogHandler.INFO, getGame().i18n.tr("Retrieving your session from the servers..."));
		try {
			git.fetch().setCheckFetchedObjects(true).setRefSpecs(new RefSpec("+refs/heads/"+userBranchHash+":refs/remotes/origin/"+userBranchHash)).call();
		} catch (GitAPIException ex) {
			// FIXME: should display the stacktrace is an error occurs
			/*
			if(game.isDebugEnabled()) {
				ex.printStackTrace();
			}
			*/
			if (ex.getMessage().equals("Remote does not have refs/heads/"+userBranchHash+" available for fetch.")) {
				return false;
			}
			return false;
		}
		return true;
	}

	public void createInitialCommit() throws NoHeadException, NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException, WrongRepositoryStateException, GitAPIException {
		git.commit().setMessage("Empty initial commit")
			.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
			.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
			.call();
	}

	public void createLocalUserBranch(String userBranchHash) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		git.checkout().setCreateBranch(true).setName(userBranchHash).call();
	}

	public boolean checkoutUserBranch(String userBranchHash) {
		boolean success = true;
		try {
			git.checkout().setName(userBranchHash).call();
		} catch (GitAPIException e) {
			game.getLogger().log(LogHandler.ERROR, getGame().i18n.tr("An error occurred while checking out the user's branch: ")+userBranchHash);
			// FIXME: display the stacktrace if debug mode is enabled
			//e.printStackTrace();
			success = false;
		}
		return success;
	}

	public void mergeRemoteIntoLocalBranch(String userBranchHash) throws Exception {
		try {
			MergeResult res = git.merge().setCommit(true).setFastForward(MergeCommand.FastForwardMode.FF).setStrategy(MergeStrategy.RECURSIVE).include(git.getRepository().getRef("refs/remotes/origin/"+userBranchHash)).call();

			if(res.getMergeStatus() == MergeResult.MergeStatus.FAST_FORWARD) {
				game.getLogger().log(LogHandler.INFO, getGame().i18n.tr("last session data successfully retrieved"));
			}
			else if(res.getMergeStatus() == MergeResult.MergeStatus.MERGED) {
				game.getLogger().log(LogHandler.INFO, getGame().i18n.tr("last session data successfully merged"));
			}
			else if(res.getMergeStatus() == MergeResult.MergeStatus.CONFLICTING) {
				game.getLogger().log(LogHandler.INFO, getGame().i18n.tr("Conflicts have been detected while synchronizing with last session data, trying to resolve it..."));
				Map<String, int[][]> allConflicts = res.getConflicts();
				for (String path : allConflicts.keySet()) {
					ObjectId remote = git.getRepository().resolve("origin/"+userBranchHash);

					RevCommit lastCommitLocal = git.log().addPath(path).setMaxCount(1).call().iterator().next();
					RevCommit lastCommitRemote = git.log().add(remote).addPath(path).setMaxCount(1).call().iterator().next();
					long timeLocal = lastCommitLocal.getAuthorIdent().getWhen().getTime();
					long timeRemote = lastCommitRemote.getAuthorIdent().getWhen().getTime();

					if(timeLocal>timeRemote) {
						git.checkout().setStartPoint(lastCommitLocal).addPath(path).call();
					}
					else {
						git.checkout().setStartPoint(lastCommitRemote).addPath(path).call();
					}
					git.add().addFilepattern(path).call();
				}

				game.getLogger().log(LogHandler.INFO, "All conflicts have been manually handled ;)");
				// TODO: check if the commit is mandatory
				git.commit().setMessage("Manual merging")
				.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
				.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
				.call();
			}
			else if(res.getMergeStatus() == MergeResult.MergeStatus.FAILED) {
				// TODO: handle this case
				game.getLogger().log(LogHandler.ERROR, getGame().i18n.tr("Canceled the merge operation because of the following failures:"));
				Map<String, MergeFailureReason> allFailures = res.getFailingPaths();
				for(String path : allFailures.keySet()) {
					game.getLogger().log(LogHandler.ERROR, path + " : " + allFailures.get(path));
				}
			}
		} catch (Exception ex) {
			getGame().getLogger().log(LogHandler.ERROR, getGame().i18n.tr("Can't merge data retrieved from server with local session data."));
			throw ex;
		}
	}

	/** Push the local changes to the user's remote branch
	 *
	 * @return if the modifications have been correctly pushed
	 */
	public boolean pushChanges(String userBranchHash, ProgressMonitor progress, CredentialsProvider cp) {
		boolean success = true;
		PushCommand pc = git.push().setProgressMonitor(progress).setCredentialsProvider(cp).setRefSpecs(new RefSpec("refs/heads/"+userBranchHash+":refs/heads/"+userBranchHash));
		try {
			Iterable<PushResult> resultIterable  = pc.call();
			Iterator<PushResult> it = resultIterable.iterator();
			while(it.hasNext()) {
				PushResult pr = it.next();
				for (RemoteRefUpdate ru: pr.getRemoteUpdates()) {
					if (ru.getMessage() != null) {
						success = false;
						// FIXME: display the stacktrace if debug mode is enabled without instancing Game
						/*
						if(game.isDebugEnabled()) {
							System.err.println("Pushed to "+pr.getURI()+". Message: "+ru.getMessage());
						}
						*/
					}
					if(ru.getStatus()!=RemoteRefUpdate.Status.OK && ru.getStatus()!=RemoteRefUpdate.Status.UP_TO_DATE) {
						success = false;
						// FIXME: display the stacktrace if debug mode is enabled without instancing Game
						/*
						if(game.isDebugEnabled()) {
							System.err.println("Pushed to "+pr.getURI()+". Status: "+ru.getStatus().toString());
						}
						*/
					}
				}
			}
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			game.getLogger().log(LogHandler.ERROR, getGame().i18n.tr("Cannot synchronize your session with the servers (network down)."));
			if (game.isDebugEnabled())
				e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return success;
	}

	/** Push the data to the github servers.
	 *
	 * Beware, you don't want to do that too much to not overload the github servers (see maybePushToUserBranch() below)
	 */
	public void forcefullyPushToUserBranch(String userBranchHash, ProgressMonitor progress) {
		if(!getGame().getTrackUser()) {
			return;
		}

		synchronized(GitUtils.class) {
			currentlyPushing = true;
		}

		// credentials
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);

		// push
		if(pushChanges(userBranchHash, progress, cp)) {
			game.getLogger().log(LogHandler.INFO, getGame().i18n.tr("Your session has been successfully saved into the clouds."));
		}
		else {
			// An error occurred while pushing
			game.getLogger().log(LogHandler.INFO, getGame().i18n.tr("Fetching the server's last version..."));
			try {
				// Try to synchronize with the remote branch before pushing again
				if (fetchBranchFromRemoteBranch(userBranchHash)) {
					mergeRemoteIntoLocalBranch(userBranchHash);
				}
				if(!pushChanges(userBranchHash, progress, cp)) {
					game.getLogger().log(LogHandler.ERROR, getGame().i18n.tr("Fetching the data's last version didn't solve the issue, please report this bug."));
				}
			} catch (Exception e) {
				game.getLogger().log(LogHandler.ERROR, getGame().i18n.tr("A bug occurred while synchronizing your data with the server, please report the following error:"));
				e.printStackTrace();
			}
		}

		synchronized(GitUtils.class) {
			currentlyPushing = false;
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
		if(!getGame().getTrackUser()) {
			return;
		}

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
		if(git != null && git.getRepository().getDirectory().equals(repoDir)) {
			return;
		}
		if(git != null) {
			git.close();
		}
		this.repoDir = repoDir;
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

	public void removeFiles() {
		try {
			Status status = git.status().call();
			RmCommand rm = git.rm();
			if(status.getMissing().size()>0) {
				for(String filename:status.getMissing()) {
					rm.addFilepattern(filename);
				}
				rm.call();
			}
		} catch (NoWorkTreeException | GitAPIException e) {
			e.printStackTrace();
		}

	}

	public void seqAddFilesToPush(String commitMsg, String userBranch,
			ProgressMonitor progress) throws NoFilepatternException, GitAPIException {
		addFiles();
		commit(commitMsg);
		maybePushToUserBranch(userBranch, progress);
	}

	public void dispose() {
		username = null;
		password = null;
		git.close();
	}

	public Ref getRepoRef(String branch) throws IOException {
		return git.getRepository().getRef(branch);
	}

	public Game getGame() {
		return game;
	}
}
