package plm.core.model.session;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.UserAbortException;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;

public class GitSessionKit implements ISessionKit {

	private Game game;
	private String reponame;

	public GitSessionKit(Game game, String userUUID) {
		this.game = game;
		this.reponame = userUUID;
	}

	/**
	 * Store the user source code for all the opened lessons. It doesn't need to
	 * save the lesson summaries : it's compute on start
	 *
	 * @param path
	 * @throws UserAbortException
	 */
	@Override
	public void storeAll(File path) throws UserAbortException {
		Collection<Lesson> lessons = this.game.getLoadedLessons();
		for (Lesson lesson : lessons) {
			File repoDir = new File(path.getAbsolutePath() + System.getProperty("file.separator") + reponame);

			// First save the bodies
			storeLesson(repoDir, lesson);

			// Recompute the summaries
			File summary = new File(repoDir, lesson.getId() + ".summary");
			try {
				FileWriter fwSummary = new FileWriter(summary.getAbsoluteFile());
				BufferedWriter bwSummary = new BufferedWriter(fwSummary);
				bwSummary.write(game.studentWork.lessonSummary(lesson.getId()));
				bwSummary.close();
			} catch (IOException ex) {
				game.getLogger().log(LogHandler.ERROR, "Failed to write the lesson summary on disk: "+ex.getLocalizedMessage());
			}
		}
	}

	/**
	 * Load the user source code of the lessons' exercises. Also get the per
	 * lesson summaries
	 *
	 * @param path
	 */
	@Override
	public void loadAll(final File path) {
		File gitDir = new File(Game.getSavingLocation() + System.getProperty("file.separator") + reponame);
		if (! gitDir.exists()) {
			// It should never happen since the session content should be created by the git spy module.
			game.getLogger().log(LogHandler.ERROR, getGame().i18n.tr("Something weird happened. Your session was not created/reloaded properly. Please report this issue."));
		}

		// Load bodies
		for (Lesson lesson : this.game.getLessons()) {
			loadLesson(path, lesson);
		}

		// Load summary from the lastly saved files,
		// but don't trust the game.getLessons that is empty at startup, so search for existing files on disk
		String pattern = "*.summary";
		FileSystem fs = FileSystems.getDefault();
		final PathMatcher matcher = fs.getPathMatcher("glob:" + pattern); // to match file names ending with string "summary"

		FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
				if (! matcher.matches(file.getFileName()))  // Not a summary file. Who cares?
					return FileVisitResult.CONTINUE;

				String lessonId = file.getFileName().toString();
				lessonId = lessonId.substring(0,lessonId.length() - ".summary".length());

				//  1. Read file content in String
				StringBuilder sb = new StringBuilder();
				FileReader fr;
				BufferedReader br = null;
				try {
					fr = new FileReader(file.toFile().getAbsoluteFile());
					br = new BufferedReader(fr);
					String line;
					while ((line = br.readLine()) != null)
						sb.append(line);
				} catch (FileNotFoundException e) {
					// If there is no summary file, then don't do nothing for that lesson
				} catch (IOException e) {
					System.err.println("Error while reading the summary of lesson "+lessonId+": "+e.getLocalizedMessage());
				} finally {
					if (br != null)
						try {
							br.close();
						} catch (IOException e) {
							// I don't care about not being able to close a file that I *read*
						}
				}

				// 2. Pass that string to the sessionDB
				game.studentWork.lessonSummaryParse(lessonId, sb.toString());

				return FileVisitResult.CONTINUE;
			}
		};

		try {
			Files.walkFileTree(Paths.get(path.getAbsolutePath() + System.getProperty("file.separator") + reponame), matcherVisitor);
		} catch (IOException ex) {
			ex.printStackTrace();
		}


	}

	/**
	 * Store the user source code for a specified lesson
	 *
	 * @param path where to save
	 * @param lesson the lesson to save
	 * @throws UserAbortException
	 */
	@Override
	public void storeLesson(File path, Lesson lesson) throws UserAbortException {
		/* Everything's done by spy */
	}

	/**
	 * Load the lesson's exercises user source code
	 *
	 * @param path
	 * @param lesson
	 */
	@Override
	public void loadLesson(File path, Lesson lesson) {
		for (Lecture lecture : lesson.exercises()) {
			if (lecture instanceof Exercise) {
				Exercise exercise = (Exercise) lecture;
				for (ProgrammingLanguage lang : exercise.getProgLanguages()) {
					// check if exercise already done correctly
					String doneFile = path.getAbsolutePath() + System.getProperty("file.separator") + reponame + System.getProperty("file.separator") + exercise.getId() + "." + lang.getExt() + ".DONE";
					if (new File(doneFile).exists()) { // if the file exists, the exercise was correct
						game.studentWork.setPassed(exercise, lang, true);
					} else {
						game.studentWork.setPassed(exercise, lang, false);
					}
					// load source code
					SourceFile srcFile = exercise.getSourceFile(lang, lang.getVisualIndex());
					String fileName = path.getAbsolutePath() + System.getProperty("file.separator") + reponame + System.getProperty("file.separator") + exercise.getId() + "." + lang.getExt() + lang.getVisualExt();
					//getGame().getLogger().log(fileName);
					String line;
					StringBuilder b = new StringBuilder();
					try {
						FileReader fileReader = new FileReader(fileName);
						try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
							while ((line = bufferedReader.readLine()) != null) {
								b.append(line);
								b.append("\n");
							}
						}
						srcFile.setBody(b.toString());
					} catch (FileNotFoundException fnf) {
						/* that's fine, we never did that exercise */
					} catch (IOException ex) {
						ex.printStackTrace();
					}

				}
			}
		}
	}

	@Override
	public void cleanAll(File path) {
		game.getLogger().log(LogHandler.INFO, "Clean all lessons. Your session is now lost.");
		for (Lesson lesson : this.game.getLessons()) {
			cleanLesson(new File(path.getAbsolutePath() + System.getProperty("file.separator") + reponame), lesson);
		}
	}

	@Override
	public void cleanLesson(File path, Lesson l) {

		String pattern = l.getId() + "*";
		FileSystem fs = FileSystems.getDefault();
		final PathMatcher matcher = fs.getPathMatcher("glob:" + pattern);
		FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
				Path name = file.getFileName();
				if (matcher.matches(name)) {
					new File(name + "").delete(); // delete files related to the selected Lesson
				}
				return FileVisitResult.CONTINUE;
			}
		};
		try {
			Files.walkFileTree(Paths.get(path.getPath()), matcherVisitor);
		} catch (IOException ex) {

		}
	}

	public Game getGame() {
		return game;
	}

	@Override
	public void setUserUUID(String userUUID) {
		reponame = userUUID;
	}
}
