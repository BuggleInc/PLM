package plm.core.model.tracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.json.simple.JSONObject;
import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.Users;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;

public class GitSpy implements ProgressSpyListener {

	private String filePath;
	private File path;
	private Repository repository;
	private Git git;

	private String repoUrl = Game.getProperty("plm.git.server.url"); //"https://PLM-Test@bitbucket.org/PLM-Test/plm-test-repo.git";

	public GitSpy(File path, Users users) throws IOException, GitAPIException {
		this.path = path;

		initializeRepoDir(users);
	}

	private void initializeRepoDir(Users users) throws IOException, GitAPIException {
		String userUUID = String.valueOf(users.getCurrentUser().getUserUUID());
		String reponame = userUUID.substring(0, 8);

		filePath = path.getAbsolutePath() + System.getProperty("file.separator") + reponame;
		File repoFile = new File(filePath);

		if (!repoFile.exists()) {
			git = Git.cloneRepository().setURI(repoUrl).setDirectory(repoFile).setBranchesToClone(Arrays.asList("master")).call();
		}

		repository = FileRepositoryBuilder.create(new File(filePath, ".git"));

		// setup the remote repository
		// final StoredConfig config = repository.getConfig();
		// config.setString("remote", "origin", "url", "https://PLM-Test@bitbucket.org/PLM-Test/plm-test-repo.git");
		// config.save();

		// get the repository
		git = new Git(repository);

		GitPush gitPush = new GitPush(git);

		// checkout the branch of the current user
		gitPush.checkoutUserBranch(users);

		// plm started commit message
		git.commit().setMessage(writePLMStartedCommitMessage()).setAuthor(new PersonIdent("John Doe", "john.doe@plm.net")).setCommitter(new PersonIdent("John Doe", "john.doe@plm.net")).call();
	}

	@Override
	public void executed(Exercise exo) {
		createFiles(exo);
		checkSuccess(exo);

		try {
			GitPush gitPush = new GitPush(git);

			// checkout the branch of the current user
			// gitPush.checkoutUserBranch();

			// run the add-call
			git.add().addFilepattern(".").call();

			// and then commit the changes
			git.commit().setAuthor(new PersonIdent("John Doe", "john.doe@plm.net")).setCommitter(new PersonIdent("John Doe", "john.doe@plm.net")).setMessage(writeCommitMessage(exo, null, "executed")).call();

			// push to the remote repository
			gitPush.toUserBranch();
		} catch (GitAPIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		repository.close();
	}

	@Override
	public void switched(Exercise exo) {
		Game game = Game.getInstance();
		Exercise lastExo = (Exercise) game.getLastExercise();
		System.out.println(lastExo.getName());
		lastExo.lastResult = ExecutionProgress.newCompilationError("");

		if (lastExo.lastResult != null) {
			createFiles(lastExo);

			try {
				GitPush gitPush = new GitPush(git);

				// checkout the branch of the current user
				// gitPush.checkoutUserBranch();

				// run the add-call
				git.add().addFilepattern(".").call();

				// and then commit the changes
				git.commit().setAuthor(new PersonIdent("John Doe", "john.doe@plm.net")).setCommitter(new PersonIdent("John Doe", "john.doe@plm.net")).setMessage(writeCommitMessage(lastExo, exo, "switched")).call();

				// push to the remote repository
				gitPush.toUserBranch();
			} catch (GitAPIException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			repository.close();
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

		jsonObject.put("evt_type", evt_type);
		// Retrieve appropriate parameters regarding the current exercise
		jsonObject.put("course", game.getCourseID());
		jsonObject.put("exoname", exoFrom.getName());
		jsonObject.put("exolang", lastResult.language.toString());
		// passedTests and totalTests are initialized at -1 and 0 in case of compilation error...
		jsonObject.put("passedtests", lastResult.passedTests != -1 ? lastResult.passedTests + "" : 0 + "");
		jsonObject.put("totaltests", lastResult.totalTests != 0 ? lastResult.totalTests + "" : 1 + "");
		if (exoTo != null) {
			jsonObject.put("exoswitchto", exoTo.getName());
		}

		return jsonObject.toString();
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

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		jsonObject.put("evt_type", "started");
		// Retrieve the feedback informations
		jsonObject.put("start_date", dateFormat.format(cal.getTime()));
		jsonObject.put("java_version", System.getProperty("java.version") + " (VM: " + System.getProperty("java.vm.name") + "; version: " + System.getProperty("java.vm.version") + ")");
		jsonObject.put("os", System.getProperty("os.name") + " (version: " + System.getProperty("os.version") + "; arch: " + System.getProperty("os.arch") + ")");
		jsonObject.put("plm_version", Game.getProperty("plm.major.version", "internal", false) + " (" + Game.getProperty("plm.minor.version", "internal", false) + ")");

		return jsonObject.toString();
	}

	private void createFiles(Exercise exo) {
		ExecutionProgress lastResult = exo.lastResult;

		String exoCode = exo.getSourceFile(lastResult.language, 0).getBody(); // retrieve the code from the student
		String exoError = lastResult.compilationError; // retrieve the compilation error
		String exoCorrection = exo.getSourceFile(lastResult.language, 0).getCorrection(); // retrieve the correction
		String exoMission = exo.getMission(lastResult.language); // retrieve the mission

		// create the different files
		String repoDir = repository.getDirectory().getParent();
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
		String repoDir = repository.getDirectory().getParent();
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

			}
		}

		for (ProgrammingLanguage lang : Game.getProgrammingLanguages()) {
			int possible = Game.getInstance().studentWork.getPossibleExercises(exo.getLesson().getId(), lang);
			if (possible == 0) {
				return;
			}
			File countFile = new File(repoDir, exo.getLesson().getId() + "." + lang.getExt() + "." + possible);
			try {
				FileWriter fwExo = new FileWriter(countFile.getAbsoluteFile());
				BufferedWriter bwExo = new BufferedWriter(fwExo);
				bwExo.write("");
				bwExo.close();
			} catch (IOException ex) {

			}
		}
	}

}
