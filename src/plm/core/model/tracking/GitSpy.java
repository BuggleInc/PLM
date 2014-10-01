package plm.core.model.tracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.json.simple.JSONObject;

import plm.core.UserSwitchesListener;
import plm.core.model.Game;
import plm.core.model.User;
import plm.core.model.Users;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;

public class GitSpy implements ProgressSpyListener, UserSwitchesListener {

	private File plmDir;
	private Git git;
	
	private ProgressMonitor progress = NullProgressMonitor.INSTANCE; //new TextProgressMonitor(new PrintWriter(System.out));


	private String repoUrl = Game.getProperty("plm.git.server.url"); // https://github.com/mquinson/PLM-data.git
	private File repoDir;

	public GitSpy(File path, Users users) throws IOException, GitAPIException {
		this.plmDir = path;

		users.addUserSwitchesListener(this);
		userHasChanged(users.getCurrentUser());
	}
	
	
	// TODO: all the static method have to be moved to a git utils helper class.
	
	public static void initLocalRepository(File repoDirectory, String repoUrl) throws GitAPIException, IOException {
		Git git = Git.init().setDirectory(repoDirectory).call();
		git.close();
	}
		
	public static boolean createBranchFromRemoteBranch(File repoDirectory, String repoUrl, String userBranchHash) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {		
		String repoName = "origin";
		Git git = Git.open(repoDirectory);
			
		try {
			StoredConfig cfg = git.getRepository().getConfig();
			cfg.setString("remote", repoName, "url", repoUrl);
			cfg.setString("remote", repoName, "fetch", "+refs/heads/"+userBranchHash+":refs/remotes/"+repoName+"/"+userBranchHash);		
			cfg.save();		

			System.out.println(Game.i18n.tr("Retrieving your session from the servers..."));
			FetchResult res = git.fetch().call();
		} catch (TransportException ex) {
			if (ex.getMessage().equals("Remote does not have refs/heads/"+userBranchHash+" available for fetch.")) {
				return false;
			} else {
				throw ex;
			}
		} finally {
			git.close();
		}
		
		return true;
	}
	
	public void createLocalUserBranch(File repoDirectory, String repoName, String userBranchHash) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		Git git = Git.open(repoDirectory);
				
		// We must create an initial commit before creating a specific branch for the user
		git.commit().setMessage("Empty initial commit")
			.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
			.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
			.call();		
		git.checkout().setCreateBranch(true).setName(userBranchHash).call();
		git.close();
	}

	public static void checkoutExistingUserBranch(File repoDirectory, String userBranchHash) throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		Git git = Git.open(repoDirectory);
		try {
			git.checkout().setCreateBranch(false).setName(userBranchHash).call();
		} finally {
			git.close();
		}
	}
	
	public static void pullExistingBranch(File repoDirectory, String userBranchHash) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		// TODO: we should protect this method call from concurrent execution with pushing methods
		Git git = Git.open(repoDirectory);
		try {
			git.fetch().setCheckFetchedObjects(true).setRefSpecs(new RefSpec("+refs/heads/"+userBranchHash+":refs/remotes/origin/"+userBranchHash)).call();
		} catch (Exception ex) {
			System.err.println(Game.i18n.tr("Can't retrieve data stored on server."));
			throw ex;
		} finally {
			git.close();
		}
		
		try {
			MergeResult res = git.merge().setCommit(true).setFastForward(MergeCommand.FastForwardMode.FF).setStrategy(MergeStrategy.RESOLVE).include(git.getRepository().getRef("refs/remotes/origin/"+userBranchHash)).call();
			System.out.println(res.getMergeStatus()); // TODO: to remove
		} catch (Exception ex) {
			System.err.println(Game.i18n.tr("Can't merge data retrieved from server with local session data."));
			throw ex;
		} finally {
			git.close();
		}
	}
	
	/** 
	 * Initialize locally the git repo for that user
	 */
	@Override
	public void userHasChanged(User newUser) {
		try {
			String userUUID = newUser.getUserUUIDasString();
			String userBranch = "PLM"+GitUtils.sha1(userUUID);

			repoDir = new File(plmDir.getAbsolutePath() + System.getProperty("file.separator") + userUUID);

			if (!repoDir.exists()) {
				initLocalRepository(repoDir, repoUrl); // TODO: remove last parameter (repoUrl)
				// try to get the branch as stored remotely
				if (createBranchFromRemoteBranch(repoDir, repoUrl, userBranch)) {
					checkoutExistingUserBranch(repoDir, userBranch);
					System.out.println(Game.i18n.tr("Your session {0} was automatically retrieved from the servers.",userBranch));
				} else {
					// If no branch can be found remotely, create a new one.
					System.out.println(Game.i18n.tr("Creating a new session locally, as no corresponding session could be retrieved from the servers.",userBranch));
					createLocalUserBranch(repoDir, "origin", userBranch);
				}
			} else {		
				 Git git = Git.open(repoDir);
				 if (git.getRepository().getRef(userBranch) != null) {
					 git.close();
					 checkoutExistingUserBranch(repoDir, userBranch);
					 pullExistingBranch(repoDir, userBranch);
				 } else { // FIXME: this case should never happenedn, therefore it should be reported to the end-user
					 git.close();
					 System.out.println("WARNING: trying to checkout a non existing git user branch during user switching.");
				 }
			}
			this.git = Git.open(repoDir);

			// Log into the git that the PLM just started
			this.git.commit().setMessage(writePLMStartedCommitMessage())
			.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
			.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
			.call();

			// and push to ensure that everything remains in sync
			GitUtils gitUtils = new GitUtils(git);
			gitUtils.maybePushToUserBranch(userBranch, progress); 
		} catch (Exception e) {
			System.err.println(Game.i18n.tr("You found a bug in the PLM. Please report it with all possible details (including the stacktrace below)."));
			e.printStackTrace();
		}
	}

	@Override
	public void executed(Exercise exo) {
		try {

			// checkout the branch of the current user (just in case it changed in between)
			// if this case might happen, then you should also checkout back the current user branch...
			//GitUtils gitUtils = new GitUtils(git);
			//gitUtils.checkoutUserBranch(Game.getInstance().getUsers().getCurrentUser(), progress);
			
			// Change the files locally
			createFiles(exo);
			checkSuccess(exo);

			// run the add-call
			git.add().addFilepattern(".").call();

			// and then commit the changes
			git.commit().setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
			            .setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
			            .setMessage(writeCommitMessage(exo, null, "executed", new JSONObject()))
			            .call();

			// push to the remote repository
			GitUtils gitUtils = new GitUtils(git);
			
			String userUUID = Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString();
			String userBranch = "PLM"+GitUtils.sha1(userUUID);
			gitUtils.maybePushToUserBranch(userBranch, progress);
		} catch (GitAPIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void switched(Exercise exo) {
		//TODO: remove duplicated code. Looks like the executed(Exercise exo) code
		Exercise lastExo = (Exercise) Game.getInstance().getLastExercise();
		if (lastExo == null)
			return;

		if (lastExo.lastResult != null) {
			createFiles(lastExo);

			try {
				GitUtils gitUtils = new GitUtils(git);

				// run the add-call
				git.add().addFilepattern(".").call();

				// and then commit the changes
				git.commit().setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
				.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
				.setMessage(writeCommitMessage(lastExo, exo, "switched", new JSONObject()))
				.call();

				// push to the remote repository
				String userUUID = Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString();
				String userBranch = "PLM"+GitUtils.sha1(userUUID);
				gitUtils.maybePushToUserBranch(userBranch, progress);
			} catch (GitAPIException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void heartbeat() {
	}

	@Override
	public String join() {
		return "";
	}

	@Override
	public void leave() {	
		System.out.println(Game.i18n.tr("Pushing to the remote repository before exiting"));
		try {
			GitUtils gitUtils = new GitUtils(git);
			// push to the remote repository
			String userUUID = Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString();
			String userBranch = "PLM"+GitUtils.sha1(userUUID);
			gitUtils.forcefullyPushToUserBranch(userBranch, progress);

		} catch (org.eclipse.jgit.api.errors.TransportException e) {
			System.err.println(Game.i18n.tr("Don't save code remotely, as the network seems unreachable."));
			if (Game.getInstance().isDebugEnabled())
				e.printStackTrace();

		} catch (GitAPIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper methods
	 */
	@SuppressWarnings("unchecked")
	private String writeCommitMessage(Exercise exoFrom, Exercise exoTo, String evt_type, JSONObject logmsg) {

		ExecutionProgress lastResult = exoFrom.lastResult;

		// Retrieve appropriate parameters regarding the current exercise
		logmsg.put("course", Game.getInstance().getCourseID());
		logmsg.put("exo", exoFrom.getId());
		logmsg.put("lang", lastResult.language.toString());
		
		if (lastResult.outcome != null) {
			switch (lastResult.outcome) {
			case COMPILE:  logmsg.put("outcome", "compile");  break;
			case FAIL:     logmsg.put("outcome", "fail");     break;
			case PASS:     logmsg.put("outcome", "pass");     break;
			default:       logmsg.put("outcome", "UNKNOWN");  break;
			}
		}
		
		if (lastResult.totalTests > 0) {
			logmsg.put("passedtests", lastResult.passedTests + "");
			logmsg.put("totaltests", lastResult.totalTests + "");
		}
		
		if (exoFrom.lastResult.feedbackDifficulty != null)
			logmsg.put("exoDifficulty", exoFrom.lastResult.feedbackDifficulty);
		if (exoFrom.lastResult.feedbackInterest != null)
			logmsg.put("exoInterest", exoFrom.lastResult.feedbackInterest);
		if (exoFrom.lastResult.feedback != null)
			logmsg.put("exoComment", exoFrom.lastResult.feedback);
		
		if (exoTo != null)
			logmsg.put("switchto", exoTo.getId());

		// Misuses JSON to ensure that the kind is always written first so that we can read github commit lists
		return "{\"kind\":\""+evt_type+"\","+logmsg.toString().substring(1);
	}

	/**
	 * This method creates a String which contains debug informations. This String will be used as the commit message
	 * set when the PLM is started by the user.
	 * 
	 * @return the JSON String that will be used as the commit message
	 */
	@SuppressWarnings("unchecked")
	private String writePLMStartedCommitMessage() {
		JSONObject jsonObject = new JSONObject();

		// Retrieve the feedback informations
		jsonObject.put("java", System.getProperty("java.version") + " (VM: " + System.getProperty("java.vm.name") + "; version: " + System.getProperty("java.vm.version") + ")");
		jsonObject.put("os", System.getProperty("os.name") + " (version: " + System.getProperty("os.version") + "; arch: " + System.getProperty("os.arch") + ")");
		jsonObject.put("plm", Game.getProperty("plm.major.version", "internal", false) + " (" + Game.getProperty("plm.minor.version", "internal", false) + ")");

		// Misuses JSON to ensure that the kind is always written first so that we can read github commit lists
		return "{\"kind\":\"start\","+jsonObject.toString().substring(1);
	}

	private void createFiles(Exercise exo) {
		ExecutionProgress lastResult = exo.lastResult;

		String exoCode = exo.getSourceFile(lastResult.language, 0).getBody(); // retrieve the code from the student
		String exoError = lastResult.compilationError; // retrieve the compilation error
		if (lastResult.compilationError == null) 
			exoError = lastResult.executionError; 
		String exoCorrection = exo.getSourceFile(lastResult.language, 0).getCorrection(); // retrieve the correction
		String exoMission = exo.getMission(lastResult.language); // retrieve the mission

		// create the different files
		String ext = "." + Game.getProgrammingLanguage().getExt();

		File exoFile = new File(repoDir, exo.getId() + ext + ".code");
		File errorFile = new File(repoDir, exo.getId() + ext + ".error");
		File correctionFile = new File(repoDir, exo.getId() + ext + ".correction");
		File missionFile = new File(repoDir, exo.getId() + ext + ".mission");

		try {
			// write the code of the exercise into the file
			FileWriter fwExo = new FileWriter(exoFile.getAbsoluteFile());
			BufferedWriter bwExo = new BufferedWriter(fwExo);
			bwExo.write(exoCode == null ? "" : exoCode);
			bwExo.close();

			// write the compilation error of the exercise into the file
			FileWriter fwError = new FileWriter(errorFile.getAbsoluteFile());
			BufferedWriter bwError = new BufferedWriter(fwError);
			bwError.write(exoError == null ? "" : exoError);
			bwError.close();

			// write the correction of the exercise into the file
			FileWriter fwCorrection = new FileWriter(correctionFile.getAbsoluteFile());
			BufferedWriter bwCorrection = new BufferedWriter(fwCorrection);
			bwCorrection.write(exoCorrection == null ? "" : exoCorrection);
			bwCorrection.close();

			// write the instructions of the exercise into the file
			FileWriter fwMission = new FileWriter(missionFile.getAbsoluteFile());
			BufferedWriter bwMission = new BufferedWriter(fwMission);
			bwMission.write(exoMission == null ? "" : exoMission);
			bwMission.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create some files to know how many exercises there is by programming languages for this lesson. Also add a file
	 * to know if the exercise has been done correctly.
	 * 
	 * @param exo
	 */
	private void checkSuccess(Exercise exo) {
		ExecutionProgress lastResult = exo.lastResult;
		String ext = "." + Game.getProgrammingLanguage().getExt();

		// if exercise is done correctly
		File doneFile = new File(repoDir, exo.getId() + ext + ".DONE");
		if (lastResult.outcome == ExecutionProgress.outcomeKind.PASS) {
			try {
				FileWriter fwExo = new FileWriter(doneFile.getAbsoluteFile());
				BufferedWriter bwExo = new BufferedWriter(fwExo);
				bwExo.write("");
				bwExo.close();
			} catch (IOException ex) {
				System.out.println("Failed to write on disk that the exercise is passed: "+ex.getLocalizedMessage());
			}
		} else {
			if (doneFile.exists())
				doneFile.delete(); // not passed anymore. Sad thing.
		}
	}

	@Override
	public void callForHelp(String studentInput) {		
		recordHelpInGit("callForHelp",studentInput);
	}

	@Override
	public void cancelCallForHelp() {
		recordHelpInGit("cancelCallForHelp",null);
	}
	
	public void recordHelpInGit(String evt_type, String studentInput) {
		Exercise lastExo = (Exercise) Game.getInstance().getCurrentLesson().getCurrentExercise();
		ExecutionProgress execProg = lastExo.lastResult;
		String exoCode = lastExo.getSourceFile(execProg.language, 0).getBody();
		String ext = "." + Game.getProgrammingLanguage().getExt();
		File exoFile = new File(repoDir, lastExo.getId() + ext + ".code");
		
		try {
			// write the code of the exercise into the file
			FileWriter fwExo = new FileWriter(exoFile.getAbsoluteFile());
			BufferedWriter bwExo = new BufferedWriter(fwExo);
			bwExo.write(exoCode == null ? "" : exoCode);
			bwExo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			GitUtils gitUtils = new GitUtils(git);
			git.add().addFilepattern(".").call();

			JSONObject msg = new JSONObject();
			msg.put("studentInput", studentInput);
			// and then commit the changes
			git.commit().setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
					.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
					.setMessage(writeCommitMessage(lastExo, null, evt_type, msg))
					.call();

			// push to the remote repository
			String userUUID = Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString();
			String userBranch = "PLM"+GitUtils.sha1(userUUID);
			gitUtils.maybePushToUserBranch(userBranch, progress);
		} catch (IOException | GitAPIException ex) { 
			ex.printStackTrace();
		}
	}
	@Override
	public void readTip(String id, String mission) {
		Exercise lastExo = (Exercise) Game.getInstance().getCurrentLesson().getCurrentExercise();
		String ext = "." + Game.getProgrammingLanguage().getExt();
		File missionFile = new File(repoDir, lastExo.getId() + ext + ".mission");
		
		try {
			// write the instructions of the exercise into the file
			FileWriter fwMission = new FileWriter(missionFile.getAbsoluteFile());
			BufferedWriter bwMission = new BufferedWriter(fwMission);
			bwMission.write(mission == null ? "" : mission);
			bwMission.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			GitUtils gitUtils = new GitUtils(git);
			git.add().addFilepattern(".").call();

			JSONObject msg = new JSONObject();
			msg.put("id", id);
			// and then commit the changes
			git.commit().setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
					.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
					.setMessage(writeCommitMessage(lastExo, null, "readTip", msg))
					.call();

			// push to the remote repository
			String userUUID = Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString();
			String userBranch = "PLM"+GitUtils.sha1(userUUID);
			gitUtils.maybePushToUserBranch(userBranch, progress);
		} catch (IOException | GitAPIException ex) { 
			ex.printStackTrace();
		}
	}
}
