package plm.core.model.tracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.StoredConfig;
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

	private String repoUrl = Game.getProperty("plm.git.server.url"); // https://github.com/mquinson/PLM-data.git
	private File repoDir;

	public GitSpy(File path, Users users) throws IOException, GitAPIException {
		this.plmDir = path;

		users.addUserSwitchesListener(this);
		userHasChanged(users.getCurrentUser());
	}
	/** 
	 * Initialize locally the git repo for that user
	 */
	@Override
	public void userHasChanged(User newUser) {
		try {
			String userUUID = String.valueOf(newUser.getUserUUID());
			String userBranch = "PLM"+GitUtils.sha1(userUUID); // For some reason, github don't like when the branch name consists of 40 hexadecimal, so we add "PLM" in front of it

			repoDir = new File(plmDir.getAbsolutePath() + System.getProperty("file.separator") + userUUID);

			if (!repoDir.exists()) {
				// try to get the branch as stored remotely

				git = Git.cloneRepository().setURI(repoUrl).setDirectory(repoDir).setBranchesToClone(Arrays.asList(userBranch)).call();

				// If no branch can be found remotely, create a new one.
				if (git == null) { 
					git = Git.init().setDirectory(repoDir).call();
					StoredConfig cfg = git.getRepository().getConfig();
					cfg.setString("remote", "origin", "url", repoUrl);
					cfg.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
					cfg.save();
					git.commit().setMessage("Empty initial commit").setAuthor(new PersonIdent("John Doe", "john.doe@plm.net")).setCommitter(new PersonIdent("John Doe", "john.doe@plm.net")).call();
					System.out.println(Game.i18n.tr("Creating a new session locally, as no corresponding session could be retrieved from the servers.",userBranch));
				} else {
					System.out.println(Game.i18n.tr("Your session {0} was automatically retrieved from the servers.",userBranch));
				}
			} else {
				git = Git.open(repoDir);
				
				new GitUtils(git).checkoutUserBranch(newUser);
			}

			GitUtils gitUtils = new GitUtils(git);

			// checkout the branch of the current user
			gitUtils.checkoutUserBranch(newUser);

			// Log into the git that the PLM just started
			git.commit().setMessage(writePLMStartedCommitMessage())
			.setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
			.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
			.call();

			// and push to ensure that everything remains in sync
			gitUtils.pushToUserBranch();
		} catch (Exception e) {
			System.err.println(Game.i18n.tr("You found a bug in the PLM. Please report it with all possible details (including the stacktrace below)."));
			e.printStackTrace();
		}
	}

	@Override
	public void executed(Exercise exo) {
		try {
			GitUtils gitUtils = new GitUtils(git);

			// checkout the branch of the current user (just in case it changed in between)
			gitUtils.checkoutUserBranch(Game.getInstance().getUsers().getCurrentUser());

			// Change the files locally
			createFiles(exo);
			checkSuccess(exo);

			// run the add-call
			git.add().addFilepattern(".").call();

			// and then commit the changes
			git.commit().setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
			            .setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
			            .setMessage(writeCommitMessage(exo, null, "executed"))
			            .call();

			// push to the remote repository
			gitUtils.pushToUserBranch();
		} catch (GitAPIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void switched(Exercise exo) {
		Exercise lastExo = (Exercise) Game.getInstance().getLastExercise();
		if (lastExo == null)
			return;

		lastExo.lastResult = ExecutionProgress.newCompilationError("");

		if (lastExo.lastResult != null) {
			createFiles(lastExo);

			try {
				GitUtils gitUtils = new GitUtils(git);

				// run the add-call
				git.add().addFilepattern(".").call();

				// and then commit the changes
				git.commit().setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
				.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
				.setMessage(writeCommitMessage(lastExo, exo, "switched"))
				.call();

				// push to the remote repository
				gitUtils.pushToUserBranch();
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
	}

	/**
	 * Helper methods
	 */
	@SuppressWarnings("unchecked")
	private String writeCommitMessage(Exercise exoFrom, Exercise exoTo, String evt_type) {
		JSONObject jsonObject = new JSONObject();

		Game game = Game.getInstance();
		ExecutionProgress lastResult = exoFrom.lastResult;

		// Retrieve appropriate parameters regarding the current exercise
		jsonObject.put("course", game.getCourseID());
		jsonObject.put("exo", exoFrom.getId());
		jsonObject.put("lang", lastResult.language.toString());
		// passedTests and totalTests are initialized at -1 and 0 in case of compilation error...
		jsonObject.put("passedtests", lastResult.passedTests != -1 ? lastResult.passedTests + "" : 0 + "");
		jsonObject.put("totaltests", lastResult.totalTests != 0 ? lastResult.totalTests + "" : 1 + "");

		if (exoFrom.lastResult.feedbackDifficulty != null)
			jsonObject.put("exoDifficulty", exoFrom.lastResult.feedbackDifficulty);
		if (exoFrom.lastResult.feedbackInterest != null)
			jsonObject.put("exoInterest", exoFrom.lastResult.feedbackInterest);
		if (exoFrom.lastResult.feedback != null)
			jsonObject.put("exoComment", exoFrom.lastResult.feedback);
		
		if (exoTo != null)
			jsonObject.put("switchto", exoTo.getId());

		// Misuses JSON to ensure that the kind is always written first so that we can read github commit lists
		return "{\"kind\":\""+evt_type+"\","+jsonObject.toString().substring(1);
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
		if (lastResult.totalTests != 0 && lastResult.totalTests == lastResult.passedTests) {
			File doneFile = new File(repoDir, exo.getId() + ext + ".DONE");
			try {
				FileWriter fwExo = new FileWriter(doneFile.getAbsoluteFile());
				BufferedWriter bwExo = new BufferedWriter(fwExo);
				bwExo.write("");
				bwExo.close();
			} catch (IOException ex) {
				System.out.println("Failed to write on disk that the exercise is passed: "+ex.getLocalizedMessage());
			}
		}
	}

	@Override
	public void callForHelp() {		
		recordHelpInGit("callForHelp");
	}

	@Override
	public void cancelCallForHelp() {
		recordHelpInGit("cancelCallForHelp");
	}
	
	public void recordHelpInGit(String evt_type) {
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

			// and then commit the changes
			git.commit().setAuthor(new PersonIdent("John Doe", "john.doe@plm.net"))
					.setCommitter(new PersonIdent("John Doe", "john.doe@plm.net"))
					.setMessage(writeCommitMessage(lastExo, null, evt_type))
					.call();

			// push to the remote repository
			gitUtils.pushToUserBranch();
		} catch (IOException | GitAPIException ex) { 
			ex.printStackTrace();
		}
	}
}
