package plm.core.model.tracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.json.simple.JSONObject;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.utils.FileUtils;

public class GitSpy implements ProgressSpyListener {

	private File plmDir;
	private GitUtils gitUtils;
	private Game game;
	private String userUUID;
	private ProgressMonitor progress = NullProgressMonitor.INSTANCE; //new TextProgressMonitor(new PrintWriter(System.out));


	private String repoUrl = Game.getProperty("plm.git.server.url"); // https://github.com/mquinson/PLM-data.git
	private File repoDir;

	public GitSpy(Game game, File path, GitUtils g, String userUUID) throws IOException, GitAPIException {
		this.game = game;
		this.plmDir = path;
		this.userUUID = userUUID;
		gitUtils = g;
		userHasChanged();
	}

	/**
	 * Initialize locally the git repo for that user
	 */
	public void userHasChanged() {
		String userBranch = "PLM"+GitUtils.sha1(userUUID);

		try {
			repoDir = new File(plmDir.getAbsolutePath() + System.getProperty("file.separator") + userUUID);

			if (!repoDir.exists()) {
				gitUtils.initLocalRepository(repoDir);
				gitUtils.setUpRepoConfig(repoUrl, userBranch);
				// We must create an initial commit before creating a specific branch for the user
				gitUtils.createInitialCommit();
			}

			gitUtils.openRepo(repoDir);
			if (gitUtils.getRepoRef(userBranch) != null) {
				gitUtils.checkoutUserBranch(userBranch);
			}
			else {
				gitUtils.createLocalUserBranch(userBranch);
			}

			// try to get the branch as stored remotely
			if (gitUtils.fetchBranchFromRemoteBranch(userBranch)) {
				gitUtils.mergeRemoteIntoLocalBranch(userBranch);
				game.getLogger().log(LogHandler.INFO, getGame().i18n.tr("Your session {0} was automatically retrieved from the servers.",userBranch));
			}
			else {
				// If no branch can be found remotely, create a new one.
				//getGame().getLogger().log(getGame().i18n.tr("Creating a new session locally, as no corresponding session could be retrieved from the servers.",userBranch));
				game.getLogger().log(LogHandler.INFO, getGame().i18n.tr("Couldn't retrieve a corresponding session from the servers..."));
			}

			// Log into the git that the PLM just started
			gitUtils.commit(writePLMStartedOrLeavedCommitMessage("started"));

			// and push to ensure that everything remains in sync
			gitUtils.maybePushToUserBranch(userBranch, progress);
		} catch (Exception e) {
			game.getLogger().log(LogHandler.ERROR, getGame().i18n.tr("You found a bug in the PLM. Please report it with all possible details (including the stacktrace below)."));
			e.printStackTrace();
		}
	}

	@Override
	public void executed(Exercise exo) {
		try {

			// checkout the branch of the current user (just in case it changed in between)
			// if this case might happen, then you should also checkout back the current user branch...
			//GitUtils gitUtils = new GitUtils(git);
			//gitUtils.checkoutUserBranch(game.getUsers().getCurrentUser(), progress);

			// Change the files locally
			createFiles(exo);
			checkSuccess(exo);

			String commitMsg = writeCommitMessage(exo, null, "executed", new JSONObject());
			String userBranch = "PLM"+GitUtils.sha1(userUUID);

			gitUtils.removeFiles();
			gitUtils.seqAddFilesToPush(commitMsg, userBranch, progress);
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void switched(Exercise exo) {
		Exercise lastExo = (Exercise) game.getLastExercise();
		if (lastExo == null)
			return;

		if (lastExo.lastResult != null) {
			createFiles(lastExo);

			try {
				String commitMsg = writeCommitMessage(lastExo, exo, "switched", new JSONObject());
				String userBranch = "PLM"+GitUtils.sha1(userUUID);

				gitUtils.seqAddFilesToPush(commitMsg, userBranch, progress);
			} catch (GitAPIException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void reverted(Exercise exo) {
		try {
			deleteFiles(exo);

			String commitMsg = writeCommitMessage(exo, null, "reverted", new JSONObject());
			String userBranch = "PLM"+GitUtils.sha1(userUUID);

			gitUtils.removeFiles();
			gitUtils.commit(commitMsg);
			gitUtils.maybePushToUserBranch(userBranch, progress);
		} catch (GitAPIException e) {
			e.printStackTrace();
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
		game.getLogger().log(LogHandler.INFO, getGame().i18n.tr("Pushing to the remote repository before exiting"));

		// push to the remote repository
		String commitMsg = writePLMStartedOrLeavedCommitMessage("leaved");
		String userBranch = "PLM"+GitUtils.sha1(userUUID);

		try {
			gitUtils.addFiles();
			gitUtils.commit(commitMsg);
			gitUtils.forcefullyPushToUserBranch(userBranch, progress);

		} catch (GitAPIException e) {
			System.err.println("An error occurred while quitting the program, please report the following issue:");
			e.printStackTrace();
		}

		gitUtils.dispose();
	}

	/**
	 * Helper methods
	 */
	@SuppressWarnings("unchecked")
	private String writeCommitMessage(Exercise exoFrom, Exercise exoTo, String evt_type, JSONObject logmsg) {

		ExecutionProgress lastResult = exoFrom.lastResult;

		// Retrieve appropriate parameters regarding the current exercise
		logmsg.put("course", game.getCourseID());
		logmsg.put("exo", exoFrom.getId());

		if(lastResult != null) {
			if(lastResult.language != null) {
				logmsg.put("lang", lastResult.language.toString());
			}
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

			if (lastResult.feedbackDifficulty != null)
				logmsg.put("exoDifficulty", exoFrom.lastResult.feedbackDifficulty);
			if (lastResult.feedbackInterest != null)
				logmsg.put("exoInterest", exoFrom.lastResult.feedbackInterest);
			if (lastResult.feedback != null)
				logmsg.put("exoComment", exoFrom.lastResult.feedback);
		}
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
	private String writePLMStartedOrLeavedCommitMessage(String kind) {
		JSONObject jsonObject = new JSONObject();

		// Retrieve the feedback informations
		jsonObject.put("java", System.getProperty("java.version") + " (VM: " + System.getProperty("java.vm.name") + "; version: " + System.getProperty("java.vm.version") + ")");
		jsonObject.put("os", System.getProperty("os.name") + " (version: " + System.getProperty("os.version") + "; arch: " + System.getProperty("os.arch") + ")");
		jsonObject.put("plm", Game.getProperty("plm.major.version", "internal", false) + " (" + Game.getProperty("plm.minor.version", "internal", false) + ")");
		jsonObject.put("webplm.version", game.getLocalProperty("webplm.version"));
		jsonObject.put("webplm.user-agent", game.getLocalProperty("webplm.user-agent"));

		// Misuses JSON to ensure that the kind is always written first so that we can read github commit lists
		return "{\"kind\":\""+kind+"\","+jsonObject.toString().substring(1);
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
		String ext = "." + lastResult.language.getExt();

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

			// write the workspace code of the exercise into the file if it exist
			if(lastResult.language.getVisualFile()){
				String wksCode = exo.getSourceFile(lastResult.language, lastResult.language.getVisualIndex()).getBody();
				File wksFile = new File(repoDir, exo.getId() + ext + lastResult.language.getVisualExt());
				FileWriter fwWks = new FileWriter(wksFile.getAbsoluteFile());
				BufferedWriter bwWks = new BufferedWriter(fwWks);
				bwWks.write(exoCode == null ? "" : wksCode);
				bwWks.close();
			}

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

	private void deleteFiles(Exercise exo) {

		List<String> suffixes = new ArrayList<String>();
		suffixes.add(".code");
		suffixes.add(".workspace");
		suffixes.add(".error");
		suffixes.add(".correction");
		suffixes.add(".mission");
		suffixes.add(".DONE");

		for(ProgrammingLanguage pl : Game.getProgrammingLanguages()) {
			String ext = "." + pl.getExt();
			for(String suffix:suffixes) {
				File file = new File(repoDir, exo.getId() + ext + suffix);
				if(file.exists()) {
					file.delete();
				}
			}
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
		String ext = "." + exo.lastResult.language.getExt();

		// if exercise is done correctly
		File doneFile = new File(repoDir, exo.getId() + ext + ".DONE");
		if (lastResult.outcome == ExecutionProgress.outcomeKind.PASS) {
			try {
				FileWriter fwExo = new FileWriter(doneFile.getAbsoluteFile());
				BufferedWriter bwExo = new BufferedWriter(fwExo);
				bwExo.write("");
				bwExo.close();
			} catch (IOException ex) {
				game.getLogger().log(LogHandler.ERROR, "Failed to write on disk that the exercise is passed: "+ex.getLocalizedMessage());
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

	@SuppressWarnings("unchecked")
	public void recordHelpInGit(String evt_type, String studentInput) {
		Exercise lastExo = (Exercise) game.getCurrentLesson().getCurrentExercise();
		ExecutionProgress execProg = lastExo.lastResult;
		String exoCode = lastExo.getSourceFile(execProg.language, 0).getBody();
		String ext = "." + game.getProgrammingLanguage().getExt();
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

		JSONObject msg = new JSONObject();
		msg.put("studentInput", studentInput);
		String commitMsg = writeCommitMessage(lastExo, null, evt_type, msg);
		String userBranch = "PLM"+GitUtils.sha1(userUUID);

		// push to the remote repository
		try {
			gitUtils.seqAddFilesToPush(commitMsg, userBranch, progress);
		} catch (GitAPIException e) {
			System.err.println("An error occurred while pushing the fact that you called for help...\n"
					+ "Please send a bug report with the following trace:");
			e.printStackTrace();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readTip(String id, String mission) {
		Exercise lastExo = (Exercise) game.getCurrentLesson().getCurrentExercise();
		String ext = "." + game.getProgrammingLanguage().getExt();
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

		JSONObject msg = new JSONObject();
		msg.put("id", id);
		String commitMsg = writeCommitMessage(lastExo, null, "readTip", msg);
		String userBranch = "PLM"+GitUtils.sha1(userUUID);

		try {
			gitUtils.seqAddFilesToPush(commitMsg, userBranch, progress);
		} catch (GitAPIException e) {
			System.err.println("An error occurred while pushing the fact that you took a look at the hint...\n"
					+ "Please send a bug report with the following trace:");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void commonErrorFeedback(int commonErrorID, int accuracy, int help, String comment) {
		Exercise lastExo = (Exercise) game.getCurrentLesson().getCurrentExercise();
		String ext = "." + game.getLocale();
		File commonErrHTML = new File(repoDir, lastExo.getId() + ext + ".commonErrHTML");
		File commonErrJAVA = new File(repoDir, lastExo.getId() + ".commonErrJAVA");

		String path = Game.JAVA.nameOfCommonError(lastExo, commonErrorID).replaceAll("\\.", "/");
		String commonErrorText = "", commonErrorCode = "";
		try {
			StringBuffer sbHtml = FileUtils.readContentAsText(path, getGame().getLocale(), "html", true);
			StringBuffer sbJava = FileUtils.readContentAsText(path, null, "java", false);
			commonErrorText = sbHtml.toString();
			commonErrorCode = sbJava.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// write the instructions of the exercise into the file
			FileWriter fwHtml = new FileWriter(commonErrHTML.getAbsoluteFile());
			BufferedWriter bwHtml = new BufferedWriter(fwHtml);
			bwHtml.write(commonErrorText);
			bwHtml.close();
			FileWriter fwJava = new FileWriter(commonErrJAVA.getAbsoluteFile());
			BufferedWriter bwJava = new BufferedWriter(fwJava);
			bwJava.write(commonErrorCode);
			bwJava.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject msg = new JSONObject();
		msg.put("commonErrorID", commonErrorID);
		msg.put("accuracy", accuracy);
		msg.put("help", help);
		msg.put("comment", comment);
		msg.put("exoID", lastExo.getId());
		String commitMsg = writeCommitMessage(lastExo, null, "commonErrorFeedback", msg);
		String userBranch = "PLM"+GitUtils.sha1(userUUID);

		try {
			gitUtils.seqAddFilesToPush(commitMsg, userBranch, progress);
		} catch (GitAPIException e) {
			System.err.println("An error occurred while pushing the fact that you took a look at the hint...\n"
					+ "Please send a bug report with the following trace:");
			e.printStackTrace();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void idle(String start, String end, String duration) {
		Exercise lastExo = (Exercise) game.getCurrentLesson().getCurrentExercise();

		JSONObject msg = new JSONObject();
		msg.put("start", start);
		msg.put("end", end);
		msg.put("duration", duration);
		String commitMsg = writeCommitMessage(lastExo, null, "idle", msg);
		String userBranch = "PLM"+GitUtils.sha1(userUUID);

		try {
			gitUtils.seqAddFilesToPush(commitMsg, userBranch, progress);
		} catch (GitAPIException e) {
			System.err.println("An error occurred while pushing the fact that you were idle...\n"
					+ "Please send a bug report with the following trace:");
			e.printStackTrace();
		}
	}

	public Game getGame() {
		return game;
	}

	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
		userHasChanged();
	}
}
