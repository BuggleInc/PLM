package plm.core.model.tracking;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.SwingWorker;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import plm.core.model.Game;
import plm.core.model.User;

public class GitUtils {

	private Git git;

	private String repoName = Game.getProperty("plm.git.server.username");
	private String repoPassword = Game.getProperty("plm.git.server.password");

	private static boolean currentlyPushing = false;

	public GitUtils(Git git) throws IOException, GitAPIException {
		this.git = git;
	}

	public void checkoutUserBranch(User currentUser) throws GitAPIException {
		String userUUID = String.valueOf(currentUser.getUserUUID());
		String userBranch;

		try {
			userBranch = "PLM"+sha1(userUUID); // For some reason, github don't like when the branch name consists of 40 hexadecimal, so we add "PLM" in front of it

			// create a branch for the current user if it's not already there
			if (git.getRepository().getRef(userBranch) == null) {
				try { 
					// consider there is a remote branch but not a local one
					CreateBranchCommand create = git.branchCreate();
					create.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM);
					create.setName(userBranch);
					create.setStartPoint("origin/" + userBranch);
					create.setForce(true);
					create.call();

					//System.out.println("Branch " + userBranch + " created");
					git.checkout().setName(userBranch).call();
				} catch (GitAPIException ex) {
					// if consideration is wrong, it's a new session from scratch : create the local branch as usual
					CreateBranchCommand create = git.branchCreate();
					create.setName(userBranch);
					create.call();
					git.checkout().setName(userBranch).call();
				}
			} 
		} catch (org.eclipse.jgit.api.errors.TransportException e) {
			System.err.println(Game.i18n.tr("Don't save code remotely, as the network seems unreachable. That's fine."));
			if (Game.getInstance().isDebugEnabled())
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Push the data to the github servers. 
	 * 
	 * Beware, you don't want to do that too much to not overload the github servers (see maybePushToUserBranch() below)
	 */
	public void forcefullyPushToUserBranch() {
		// credentials
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(repoName, repoPassword);

		// push
		PushCommand pc = git.push();
		pc.setCredentialsProvider(cp).setForce(true).setPushAll();
		try {
			for (PushResult pr: pc.call()) 
				if (!pr.getMessages().equals(""))
					System.err.println("Pushed to "+pr.getURI()+". Message: "+pr.getMessages());
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			System.out.println(Game.i18n.tr("Cannot synchronize your session with the servers (network down)."));
			if (Game.getInstance().isDebugEnabled())
				e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
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
	public void maybePushToUserBranch() {	
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
					Thread.sleep(30 /*mn*/ * 60 /* ->sec */ * 1000 /* ->milli */);
				} catch (InterruptedException e1) {
					/* Ok, that's fine, what ever */
				}

				// Do it now, and allow next request to occur
				forcefullyPushToUserBranch();
				
				synchronized(GitUtils.class) {
					currentlyPushing = false;
				}
				
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
}
