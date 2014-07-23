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

	public GitUtils(Git git) throws IOException, GitAPIException {
		this.git = git;
	}

	public void checkoutUserBranch(User currentUser) throws GitAPIException {
		String userUUID = String.valueOf(currentUser.getUserUUID());
		String userBranch;

		try {
			userBranch = "PLM"+sha1(userUUID); // For some reason, github don't like when the branch name consists of 40 hexadecimal, so we add "PLM" in front of it

			
			/*
			if (git.getRepository().getRef("master") == null) {
				git.branchCreate().setName("master").call();
			}

			// checkout master branch so that we start with a clean base
			git.checkout().setName("master").call();
*/

			// create the branch of the current user if it's not already there
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pushToUserBranch() {
		new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() {
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
					e.printStackTrace();
				} catch (GitAPIException e) {
					e.printStackTrace();
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
