package plm.core.model.session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.UserAbortException;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;

public class GitSessionKit implements ISessionKit {

	private Game game;
	private Repository repository;

	public GitSessionKit(Game game, File path) {
		this.game = game;
		try {
			repository = FileRepositoryBuilder.create(new File(path.getAbsolutePath() + System.getProperty("file.separator") + "repository", ".git"));
		} catch (IOException ex) {
			repository = null;
		}
	}

	@Override
	public void storeAll(File path) throws UserAbortException {
		/* Save the per lesson summaries */
//		JSONObject allLessons = new JSONObject();
//		for (String lessonName : this.game.studentWork.getLessonsNames()) {
//			JSONObject allLangs = new JSONObject();
//			for (ProgrammingLanguage lang : Game.getProgrammingLanguages()) {
//				int possible = Game.getInstance().studentWork.getPossibleExercises(lessonName, lang);
//				int passed = Game.getInstance().studentWork.getPassedExercises(lessonName, lang);
//
//				if (possible > 0) {
//					JSONObject oneLang = new JSONObject();
//					oneLang.put("possible", possible);
//					oneLang.put("passed", passed);
//					allLangs.put(lang.getLang(), oneLang);
//				}
//			}
//			if (allLangs.size() > 0) {
//				allLessons.put(lessonName, allLangs);
//			}
//		}
//		ZipOutputStream zos = null;
//		try {
//			zos = new ZipOutputStream(new FileOutputStream(new File(path, "repository/overview.zip")));
//			zos.setMethod(ZipOutputStream.DEFLATED);
//			zos.setLevel(Deflater.BEST_COMPRESSION);
//
//			zos.putNextEntry(new ZipEntry("passed"));
//			zos.write(allLessons.toJSONString().getBytes());
//			zos.closeEntry();
//		} catch (IOException ex) { // FileNotFoundException or IOException
//			// It's ok to loose this data as it will be recomputed when the lessons are actually loaded
//
//		} finally {
//			try {
//				if (zos != null) {
//					zos.close();
//				}
//			} catch (IOException ioe) {
//			}
//		}
//		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	/**
	 * Load the user source code of the lessons' exercises.
	 * Also get the per lesson summaries
	 * @param path 
	 */
	@Override
	public void loadAll(final File path) {
		for (Lesson lesson : this.game.getLessons()) {
			loadLesson(path, lesson);
		}

		// check how many exercises are done by lesson
		String pattern = "*.[0-9]*";
		FileSystem fs = FileSystems.getDefault();
		final PathMatcher matcher = fs.getPathMatcher("glob:" + pattern);

		FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
				Path name = file.getFileName();
				if (matcher.matches(name)) {
					String s = name + "";
					String[] tab = s.split("\\.", 0);
					String lessonNameTmp = "";
					for (int i = 0; i < tab.length - 2; i++) {
						lessonNameTmp += tab[i];
					}
					final String lessonName = lessonNameTmp;
					String ext = tab[tab.length - 2];
					int possible = Integer.parseInt(tab[tab.length - 1]);
					if (possible > 0) {
						for (final ProgrammingLanguage p : Game.getProgrammingLanguages()) {
							if (p.getExt().equals(ext)) {
								System.out.println(lessonName + "   " + p + "   " + possible);
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
											String s = name + "";
											System.err.println(s);
											passed++;
											Game.getInstance().studentWork.setPassedExercises(lessonName, p, passed);
										}
										return FileVisitResult.CONTINUE;
									}
									
									public int getPassed() {
										return passed;
									}
								};
								try {
									Files.walkFileTree(Paths.get(path.getPath()), matcherVisitor);
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
			Files.walkFileTree(Paths.get(path.getPath()), matcherVisitor);
		} catch (IOException ex) {

		}
	}

	@Override
	public void storeLesson(File path, Lesson l) throws UserAbortException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	/**
	 * Load the lesson's exercises user source code
	 * @param path
	 * @param lesson 
	 */
	@Override
	public void loadLesson(File path, Lesson lesson) {
		for (Lecture lecture : lesson.exercises()) {
			if (lecture instanceof Exercise) {
				Exercise exercise = (Exercise) lecture;
				for (ProgrammingLanguage lang : exercise.getProgLanguages()) {
					// check if exercice already done correctly
					if (new File(repository.getDirectory().getParent() + "/"
							+ exercise.getId() + "." + lang.getExt() + ".DONE").exists()) { // if the file exists, the tests were passed
						Game.getInstance().studentWork.setPassed(exercise, lang, true);
					}
					// load source code 
					SourceFile srcFile = exercise.getSourceFile(lang, 0);
					String fileName = repository.getDirectory().getParent() + "/"
							+ exercise.getId() + "." + lang.getExt() + ".code";
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
					} catch (IOException ex) {
					}

				}
			}
		}
	}

	@Override
	public void cleanAll(File path) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void cleanLesson(File path, Lesson l) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
