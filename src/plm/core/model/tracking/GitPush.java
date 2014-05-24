package plm.core.model.tracking;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import javax.swing.SwingWorker;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import plm.core.model.User;
import plm.core.model.Users;

public class GitPush {

	private Git git;

	private String repoName = "PLM-Test";
	private String repoPassword = "PLM-TestPW0";

	public GitPush(Git git) throws IOException, GitAPIException {
		this.git = git;
	}

	public void checkoutUserBranch(Users users) throws GitAPIException {
		User currentUser = users.getCurrentUser();
		String userUUID = String.valueOf(currentUser.getUserUUID());
		String userBranch;

		try {
			userBranch = sha1(userUUID);

			if (git.getRepository().getRef("master") == null) {
				git.branchCreate().setName("master").call();
			}

			// checkout master branch so that we start with a clean base
			git.checkout().setName("master").call();

			// eventually create the branch of the current user
			if (git.getRepository().getRef(userBranch) == null) {
				//git.branchCreate().setName(userBranch).call();
				try {
					CreateBranchCommand create = git.branchCreate();
					create.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM);
					create.setName(userBranch);
					create.setStartPoint("origin/" + userBranch);
					create.setForce(true);
					create.call();

					System.out.println("Branch " + userBranch + " created");
					git.checkout().setName(userBranch).call();
				} catch (GitAPIException ex) { // it's a new session : create the local branch as usual
					CreateBranchCommand create = git.branchCreate();
					create.setName(userBranch);
					create.call();
					git.checkout().setName(userBranch).call();
				}
			} else { // branch already exists, we set it to track the remote one
				CreateBranchCommand create = git.branchCreate();
				create.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM);
				create.setName(userBranch);
				create.setStartPoint("origin/" + userBranch);
				create.setForce(true);
				create.call();
				System.out.println("Branch " + userBranch + " track the remote");
				git.checkout().setName(userBranch).call();
				git.pull().call();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void toUserBranch() {
		new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() throws GitAPIException {
				// credentials
				CredentialsProvider cp = new UsernamePasswordCredentialsProvider(repoName, repoPassword);

				// push
				PushCommand pc = git.push();
				pc.setCredentialsProvider(cp).setForce(false).setPushAll();
				try {
					Iterator<PushResult> it = pc.call().iterator();
					if (it.hasNext()) {
						System.out.println(it.next().toString());
					}
				} catch (InvalidRemoteException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	public void toRemote() throws GitAPIException {
		new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() throws GitAPIException {
				// credentials
				CredentialsProvider cp = new UsernamePasswordCredentialsProvider(repoName, repoPassword);

				// push
				PushCommand pc = git.push();
				pc.setCredentialsProvider(cp).setForce(true).setPushAll();
				try {
					Iterator<PushResult> it = pc.call().iterator();
					if (it.hasNext()) {
						System.out.println(it.next().toString());
					}
				} catch (InvalidRemoteException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	// Helper methods
	private String sha1(String input) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
}
