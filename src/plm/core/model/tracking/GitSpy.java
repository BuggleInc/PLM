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
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.json.simple.JSONObject;

import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;

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

		filePath = path.getAbsolutePath()
				+ System.getProperty("file.separator") + "repository";

		Git.init().setDirectory(new File(filePath)).call();

		repository = FileRepositoryBuilder.create(new File(filePath, ".git"));

		System.out.println("Created a new repository at "
				+ repository.getDirectory());

		repository.close();

		// get the repository
		git = new Git(repository);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		// plm started commit message
		git.commit()
				.setMessage(
						"PLM started at " + dateFormat.format(cal.getTime()))
				.call();
	}

	@Override
	public void executed(Exercise exo) {
		// retrieve the code from the current exercise
		ExecutionProgress lastResult = exo.lastResult;
		String exoCode = exo.getSourceFile(lastResult.language, 0).getBody();

		// create the file containing the current code of the current exercise
		File exoFile = new File(repository.getDirectory().getParent(),
				exo.getId() + ".code");

		// create the file containing the current compilation error of the current exercise
		File errorFile = new File(repository.getDirectory().getParent(),
				exo.getId() + ".error");

		try {
			exoFile.createNewFile();
			errorFile.createNewFile();

			// write the code of the exercise into the file
			FileWriter fwExo = new FileWriter(exoFile.getAbsoluteFile());
			BufferedWriter bwExo = new BufferedWriter(fwExo);
			bwExo.write(exoCode);
			bwExo.close();
			
			// write the compilation of the exercise into the file
			FileWriter fwError = new FileWriter(errorFile.getAbsoluteFile());
			BufferedWriter bwError = new BufferedWriter(fwError);
			bwError.write(lastResult.compilationError == null ? ""
					: lastResult.compilationError);
			bwError.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// run the add-call
			git.add().addFilepattern(exo.getId() + ".code").call();
			git.add().addFilepattern(exo.getId() + ".error").call();

			System.out.println("Added files for " + exo.getId() + " to repository at "
					+ repository.getDirectory());

			// and then commit the changes
			// git.commit().setMessage("Added " + exo.getId()).call();
			git.commit().setMessage(writeCommitMessage(exo)).call();

			System.out.println("Committed files for " + exo.getId()
					+ " to repository at " + repository.getDirectory());
		} catch (GitAPIException e) {
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
	private String writeCommitMessage(Exercise exo) {
		JSONObject jsonObject = new JSONObject();

		Game game = Game.getInstance();
		ExecutionProgress lastResult = exo.lastResult;
		String exoCode = exo.getSourceFile(lastResult.language, 0).getBody();

		// Retrieve appropriate parameters regarding the current exercise
		jsonObject.put("username", username);
		jsonObject.put("course", game.getCourseID());
		jsonObject.put("password", game.getCoursePassword());
		jsonObject.put("exoname", exo.getName());
		jsonObject.put("exolang", lastResult.language.toString());
		// passedTests and totalTests are initialized at -1 and 0 in case of
		// compilation error...
		jsonObject.put("passedtests",
				lastResult.passedTests != -1 ? lastResult.passedTests + ""
						: 0 + "");
		jsonObject.put("totaltests",
				lastResult.totalTests != 0 ? lastResult.totalTests + ""
						: 1 + "");
		jsonObject.put("action", "execute");

		return jsonObject.toString();
	}
}
