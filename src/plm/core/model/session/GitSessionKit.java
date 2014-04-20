package plm.core.model.session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	public GitSessionKit(Game game) {
		this.game = game;
		try {
			repository = FileRepositoryBuilder.create(new File("D:\\Ced\\.plm\\repository", ".git"));
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

	@Override
	public void loadAll(File path) {
		for (Lesson lesson : this.game.getLessons()) {
			loadLesson(path, lesson);
		}
		String content = null;
		File file = new File(path, "repository/overview.zip");
		if (!file.exists()) {
			return;
		}
		ZipFile zf = null;
		BufferedReader br = null;
		try {
			zf = new ZipFile(file);
			ZipEntry entry = zf.getEntry("passed");
			if (entry == null) {
				return;
			}

			InputStream is = zf.getInputStream(entry);

			br = new BufferedReader(new InputStreamReader(is));
			String s;
			StringBuffer b = new StringBuffer();

			while ((s = br.readLine()) != null) {
				b.append(s);
			}

			content = b.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zf != null) {
					zf.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (content == null) {
			return;
		}
		Object value = null;
		try {
			value = JSONValue.parseWithException(content);
		} catch (ParseException e) {
			System.err.println("Parse error while reading the scores from disk:");
			e.printStackTrace();
		}
		if (!(value instanceof JSONObject)) {
			System.err.println("Retrieved passed-values is not a JSONObject: " + value);
			return;
		}
		JSONObject allLessons = (JSONObject) value;
		for (Object lessonName : allLessons.keySet()) {
			JSONObject allLangs = (JSONObject) allLessons.get(lessonName);
			for (Object langName : allLangs.keySet()) {
				ProgrammingLanguage lang = null;
				for (ProgrammingLanguage l : Game.getProgrammingLanguages()) {
					if (l.getLang().equals(langName)) {
						lang = l;
					}
				}

				JSONObject oneLang = (JSONObject) allLangs.get(langName);
				int possible = Integer.parseInt("" + oneLang.get("possible"));
				int passed = Integer.parseInt("" + oneLang.get("passed"));
				Game.getInstance().studentWork.setPossibleExercises((String) lessonName, lang, possible);
				Game.getInstance().studentWork.setPassedExercises((String) lessonName, lang, passed);
			}
		}
	}

	@Override
	public void storeLesson(File path, Lesson l) throws UserAbortException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void loadLesson(File path, Lesson lesson) {
		for (Lecture lecture : lesson.exercises()) {
			if (lecture instanceof Exercise) {
				Exercise exercise = (Exercise) lecture;
				for (ProgrammingLanguage lang : exercise.getProgLanguages()) {
					// check if exercice already done correctly
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
