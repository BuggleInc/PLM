package plm.core.model.tracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.json.simple.JSONObject;

import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class GitSpy implements ProgressSpyListener {

	private String username;
	private String filePath;
	private Repository repository;
	private Git git;

	public GitSpy(File path) throws IOException, GitAPIException {
		username = System.getenv("USER");
		if (username == null)
			username = System.getenv("USERNAME");
		if (username == null)
			username = "John Doe";

		filePath = path.getAbsolutePath() + System.getProperty("file.separator") + "repository";

		Git.init().setDirectory(new File(filePath)).call();

		repository = FileRepositoryBuilder.create(new File(filePath, ".git"));

		// System.out.println("Created a new repository at " + repository.getDirectory());

		repository.close();

		// setup the remote repository
		final StoredConfig config = repository.getConfig();
		config.setString("remote", "origin", "url", "https://PLM-Test@bitbucket.org/PLM-Test/plm-test-repo.git"); //"git@bitbucket.org:PLM-Test/plm-test-repo.git");
		config.save();

		// get the repository
		git = new Git(repository);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		// plm started commit message
		git.commit().setMessage(writePLMStartedCommitMessage()).call();
	}

	@Override
	public void executed(Exercise exo) {
		ExecutionProgress lastResult = exo.lastResult;

		String exoCode = exo.getSourceFile(lastResult.language, 0).getBody(); // retrieve the code from the student
		String exoError = lastResult.compilationError; // retrieve the compilation error
		String exoCorrection = exo.getSourceFile(lastResult.language, 0).getCorrection(); // retrieve the correction
		String exoMission = exo.getMission(lastResult.language); // retrieve the mission

		// create the different files
		String repoDir = repository.getDirectory().getParent();
		String ext = "." + Game.getProgrammingLanguage().getExt(); // the language extension

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

		try {
			// run the add-call
			git.add().addFilepattern(".").call();

			// System.out.println("Added files for " + exo.getId() + " to repository at " + repository.getDirectory());

			// and then commit the changes
			git.commit().setMessage(writeCommitMessage(exo)).call();

			// System.out.println("Committed files for " + exo.getId() + " to repository at " + repository.getDirectory());

			// push to the remote repository
			GitPush gitPush = new GitPush(repository, git);
			gitPush.toRemote();
		} catch (GitAPIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		repository.close();
	}

	@Override
	public void switched(Exercise exo) {
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
	private String writeCommitMessage(Exercise exo) {
		JSONObject jsonObject = new JSONObject();

		Game game = Game.getInstance();
		ExecutionProgress lastResult = exo.lastResult;

		jsonObject.put("evt_type", "executed");
		// Retrieve appropriate parameters regarding the current exercise
		jsonObject.put("username", username);
		jsonObject.put("course", game.getCourseID());
		jsonObject.put("password", game.getCoursePassword());
		jsonObject.put("exoname", exo.getName());
		jsonObject.put("exolang", lastResult.language.toString());
		// passedTests and totalTests are initialized at -1 and 0 in case of compilation error...
		jsonObject.put("passedtests", lastResult.passedTests != -1 ? lastResult.passedTests + "" : 0 + "");
		jsonObject.put("totaltests", lastResult.totalTests != 0 ? lastResult.totalTests + "" : 1 + "");
		jsonObject.put("action", "execute");

		return jsonObject.toString();
	}
	
	@SuppressWarnings("unchecked")
	private String writePLMStartedCommitMessage() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("evt_type", "started");
		// Retrieve the feedback informations
		jsonObject.put("java_version", System.getProperty("java.version") + " (VM: " + System.getProperty("java.vm.name") + "; version: " + System.getProperty("java.vm.version") + ")");
		jsonObject.put("os", System.getProperty("os.name") + " (version: " + System.getProperty("os.version") + "; arch: " + System.getProperty("os.arch") + ")");
		jsonObject.put("plm_version", Game.getProperty("plm.major.version", "internal", false) + " (" + Game.getProperty("plm.minor.version", "internal", false) + ")");

		return jsonObject.toString();
	}
}
