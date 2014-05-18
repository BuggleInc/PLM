package plm.core.model.tracking;

import java.io.IOException;
import java.util.Iterator;

import javax.swing.SwingWorker;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import plm.core.model.Game;
import plm.core.model.User;

public class GitPush {

	private String username;
	private String filePath;
	private Repository repository;
	private Git git;

	private String repoName = "PLM-Test";
	private String repoPassword = "PLM-TestPW0";
	private String repoUrl = "http://PLM-Test@bitbucket.org/PLM-Test/plm-test-repo.git"; // "git@bitbucket.org:PLM-Test/plm-test-repo.git";

	public GitPush(Repository repository, Git git) throws IOException, GitAPIException {
		// UUID userId = UUID.randomUUID();

		username = System.getenv("USER");
		if (username == null) {
			username = System.getenv("USERNAME");
		}
		if (username == null) {
			username = "John Doe";
		}

		this.repository = repository;
		this.git = git;
	}

	public void toUserBranch() {
		new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() throws GitAPIException {
				Game game = Game.getInstance();
				User currentUser = game.getUsers().getCurrentUser();

				// credentials
				CredentialsProvider cp = new UsernamePasswordCredentialsProvider(repoName, repoPassword);

				// checkout the user's branch
				git.checkout().setCreateBranch(true).setName(String.valueOf(currentUser.getUserUUID())).call();

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
