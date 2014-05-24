package plm.core.model.tracking;

import java.io.IOException;
import java.util.Iterator;

import javax.swing.SwingWorker;

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
		String userBranch = String.valueOf(currentUser.getUserUUID());

		try {
			if (git.getRepository().getRef("master") == null) {
				git.branchCreate().setName("master").setUpstreamMode(SetupUpstreamMode.TRACK).setStartPoint("origin/master").call();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// checkout master branch so that we start with a clean base
		git.checkout().setName("master").call();

		// eventually create the branch of the current user
		try {
			if (git.getRepository().getRef(userBranch) == null) {
				git.branchCreate().setName(userBranch).setUpstreamMode(SetupUpstreamMode.TRACK).setStartPoint("origin/" + userBranch).call();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// checkout the branch of the current user
		git.checkout().setName(userBranch).call();
		git.pull().call();
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
}
