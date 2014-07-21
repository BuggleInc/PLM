package plm.core.model.session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.UserAbortException;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;

public class GitSessionKit implements ISessionKit {

	private Game game;
	private String reponame;

	public GitSessionKit(Game game) {
		this.game = game;
		String userUUID = String.valueOf(game.getUsers().getCurrentUser().getUserUUID());
		reponame = userUUID.substring(0, 8);
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
		/* First save the bodies */
		Collection<Lesson> lessons = this.game.getLessons();
		for (Lesson lesson : lessons) {
			storeLesson(new File(path.getAbsolutePath() + System.getProperty("file.separator") + reponame), lesson);
		}

		/* No need to save the lesson summaries : it's computed on start */
	}

	/**
	 * Load the user source code of the lessons' exercises. Also get the per
	 * lesson summaries
	 *
	 * @param path
	 */
	@Override
	public void loadAll(final File path) {
		for (Lesson lesson : this.game.getLessons()) {
			loadLesson(new File(path.getAbsolutePath() + System.getProperty("file.separator") + reponame), lesson);
		}

		// check how many exercises are done per lesson
		String pattern = "*.[0-9]*";
		FileSystem fs = FileSystems.getDefault();
		final PathMatcher matcher = fs.getPathMatcher("glob:" + pattern); // to match file names ending with digits

		FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
				Path name = file.getFileName();
				if (matcher.matches(name)) { // if the file exists, the tests were run at least once
					String s = name + "";
					String[] tab = s.split("\\.", 0);
					String lessonNameTmp = "";
					for (int i = 0; i < tab.length - 2; i++) { // get the lesson id
						lessonNameTmp += tab[i];
					}
					final String lessonName = lessonNameTmp;
					String ext = tab[tab.length - 2]; // get the programming language
					int possible = Integer.parseInt(tab[tab.length - 1]); // get the number of exercises
					if (possible > 0) {
						for (final ProgrammingLanguage p : Game.getProgrammingLanguages()) { // for each programming language, how many exercises are done
							if (p.getExt().equals(ext)) {
								//System.out.println(lessonName + "   " + p + "   " + possible);
								Game.getInstance().studentWork.setPossibleExercises((String) lessonName, p, possible);
								String pattern = lessonName + ".*." + p.getExt() + ".DONE";
								FileSystem fs = FileSystems.getDefault();
								final PathMatcher matcher = fs.getPathMatcher("glob:" + pattern);

								FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
									private int passed = 0;

									@Override
									public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
										Path name = file.getFileName();
										if (matcher.matches(name)) {
											passed++; // incr each time we find an exercise that is correctly done for the programming language p
											Game.getInstance().studentWork.setPassedExercises(lessonName, p, passed);
										}
										return FileVisitResult.CONTINUE;
									}

									public int getPassed() {
										return passed;
									}
								};
								try {
									Files.walkFileTree(Paths.get(path.getAbsolutePath() + System.getProperty("file.separator") + reponame), matcherVisitor);
								} catch (IOException ex) {

								}
							}
						}
					}
				}
				return FileVisitResult.CONTINUE;
			}
		};
		try {
			Files.walkFileTree(Paths.get(path.getAbsolutePath() + System.getProperty("file.separator") + reponame), matcherVisitor);
		} catch (IOException ex) {

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
//		for (Lecture lecture : lesson.exercises()) {
//			if (lecture instanceof Exercise) {
//				Exercise exercise = (Exercise) lecture;
//				for (ProgrammingLanguage lang : exercise.getProgLanguages()) {
//					SourceFile sf = exercise.getSourceFile(lang, 0);
//					String str  =sf.getBody();
//					if (str.length() != 0) {
//						File sourceFileDisk = new File(path, exercise.getId() + "." + lang.getExt() + ".code");
//						try {
//							FileWriter fwExo = new FileWriter(sourceFileDisk.getAbsoluteFile());
//							BufferedWriter bwExo = new BufferedWriter(fwExo);
//							bwExo.write(sf.getBody());
//							bwExo.close();
//						} catch (IOException ex) {
//
//						}
//					}
//				}
//			}
//		}
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
					String doneFile = path.getAbsolutePath() + System.getProperty("file.separator") + reponame + System.getProperty("file.separator")
							+ exercise.getId() + "." + lang.getExt() + ".DONE";
					if (new File(doneFile).exists()) { // if the file exists, the exercise was correct
						Game.getInstance().studentWork.setPassed(exercise, lang, true);
					}
					// load source code 
					SourceFile srcFile = exercise.getSourceFile(lang, 0);
					String fileName = path.getAbsolutePath() + System.getProperty("file.separator") + reponame + System.getProperty("file.separator")
							+ exercise.getId() + "." + lang.getExt() + ".code";
					//System.out.println(fileName);
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
					} catch (FileNotFoundException ex) {
						// System.out.println("Il n'y a rien en " + fileName);
					} catch (IOException ex) {
					}

				}
			}
		}
	}

	@Override
	public void cleanAll(File path) {
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

}
