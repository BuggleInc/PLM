package plm.core.model.tracking;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

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
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteRefUpdate;

import plm.core.log.LogHandler;
import plm.core.log.Logger;
import plm.core.model.I18nManager;

public class GitUtils {

	private Git git;
	private File repoDir;
	private Locale locale;

	public GitUtils(Locale locale) {
		this.locale = locale;
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
			Logger.debug(I18nManager.getI18n(locale).tr("An error occurred while configuring the repository..."));
			e.printStackTrace();
		}
	}

	public boolean fetchBranchFromRemoteBranch(String userBranchHash) throws InvalidRemoteException, GitAPIException {
		Logger.debug(I18nManager.getI18n(locale).tr("Retrieving your session from the servers..."));
		try {
			FetchResult result = git.fetch().setCheckFetchedObjects(true).setRefSpecs(new RefSpec("+refs/heads/"+userBranchHash+":refs/remotes/origin/"+userBranchHash)).call();
			if(result.getTrackingRefUpdates().isEmpty()) {
				return false;
			}
		} catch (GitAPIException ex) {
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
			Logger.debug(I18nManager.getI18n(locale).tr("An error occurred while checking out the user's branch: ")+userBranchHash);
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
				Logger.error(I18nManager.getI18n(locale).tr("last session data successfully retrieved"));
			}
			else if(res.getMergeStatus() == MergeResult.MergeStatus.MERGED) {
				Logger.error(I18nManager.getI18n(locale).tr("last session data successfully merged"));
			}
			else if(res.getMergeStatus() == MergeResult.MergeStatus.CONFLICTING) {
				Logger.error(I18nManager.getI18n(locale).tr("Conflicts have been detected while synchronizing with last session data, trying to resolve it..."));
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

				Logger.log(LogHandler.ERROR, "All conflicts have been manually handled ;)");
				// TODO: check if the commit is mandatory
				git.commit().setMessage("Manual merging")
				.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
				.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
				.call();
			}
			else if(res.getMergeStatus() == MergeResult.MergeStatus.FAILED) {
				// TODO: handle this case
				Logger.log(LogHandler.ERROR, I18nManager.getI18n(locale).tr("Canceled the merge operation because of the following failures:"));
				Map<String, MergeFailureReason> allFailures = res.getFailingPaths();
				for(String path : allFailures.keySet()) {
					Logger.log(LogHandler.ERROR, path + " : " + allFailures.get(path));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(I18nManager.getI18n(locale).tr("Can't merge data retrieved from server with local session data."));
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
			Logger.debug(I18nManager.getI18n(locale).tr("Cannot synchronize your session with the servers (network down)."));
			Logger.debug(e.getStackTrace().toString());
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return success;
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

	public void dispose() {
		git.close();
	}

	public Ref getRepoRef(String branch) throws IOException {
		return git.getRepository().getRef(branch);
	}
}
