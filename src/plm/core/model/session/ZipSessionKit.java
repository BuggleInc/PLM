package plm.core.model.session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.UserAbortException;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;

/**
 * Implementation of the {@link ISessionKit} saving the student data in a zip file.
 * 
 * Unless you edit the source, this is the session kit used.
 */
public class ZipSessionKit implements ISessionKit {

	private Game game;
	
	public ZipSessionKit(Game game) {
		this.game = game;
	}

	private File openSaveFile(File path, Lesson lesson) {
		return new File(path, "plm-"+lesson.getId()+".zip");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void storeAll(File path) throws UserAbortException {
		/* First save the bodies */
		for (Lesson lesson : this.game.getLessons()) 
			storeLesson(path, lesson);
			
		/* Save the per lesson summaries */
		JSONObject allLessons = new JSONObject();
		for (String lessonName : this.game.studentWork.getLessonsNames()) {
			JSONObject allLangs = new JSONObject();
			for (ProgrammingLanguage lang: Game.getProgrammingLanguages()) {
				int possible = game.studentWork.getPossibleExercises(lessonName, lang);
				int passed = game.studentWork.getPassedExercises(lessonName, lang);

				if (possible>0) {
					JSONObject oneLang = new JSONObject();
					oneLang.put("possible",possible);
					oneLang.put("passed",passed);
					allLangs.put(lang.getLang(),oneLang);
				}
			}
			if (allLangs.size()>0) 
				allLessons.put(lessonName, allLangs);
		}
		//getGame().getLogger().log("JSON written: "+allLessons.toJSONString());
		

		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(new File(path, "overview.zip")));
			zos.setMethod(ZipOutputStream.DEFLATED);
			zos.setLevel(Deflater.BEST_COMPRESSION);

			zos.putNextEntry(new ZipEntry("passed"));
			zos.write(allLessons.toJSONString().getBytes());
			zos.closeEntry();
		} catch (IOException ex) { // FileNotFoundException or IOException
			// It's ok to loose this data as it will be recomputed when the lessons are actually loaded

		} finally {
			try {
				if (zos != null)
					zos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	@Override
	public void loadAll(File path) {
		/* First get the bodies */
		for (Lesson lesson : this.game.getLessons())
			loadLesson(path, lesson);
		
		/* Also get the per lesson summaries */
		
		// get the zip content
		String content = null;
		File file = new File(path, "overview.zip");
		if (!file.exists())
			return;
		ZipFile zf = null;
		BufferedReader br = null;
		try {
			zf = new ZipFile(file);
			ZipEntry entry = zf.getEntry("passed");
			if (entry == null) 
				return;
			
			InputStream is = zf.getInputStream(entry);

			br = new BufferedReader(new InputStreamReader(is));
			String s;
			StringBuffer b = new StringBuffer();

			while ((s = br.readLine()) != null) 
				b.append(s);

			content = b.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zf != null)
					zf.close();
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (content == null)
			return;
		//getGame().getLogger().log("JSON Read: "+content);
		
		// now parse it
		Object value = null;
		try {
			value = JSONValue.parseWithException(content);
		} catch (ParseException e) {
			System.err.println("Parse error while reading the scores from disk:");
			e.printStackTrace();
		}
		if (! (value instanceof JSONObject)) {
			System.err.println("Retrieved passed-values is not a JSONObject: "+value);
			return;
		}
		JSONObject allLessons = (JSONObject) value; 
		for (Object lessonName: allLessons.keySet()) {
			JSONObject allLangs = (JSONObject) allLessons.get(lessonName);
			for (Object langName: allLangs.keySet()) {
				ProgrammingLanguage lang = null;
				for (ProgrammingLanguage l:Game.getProgrammingLanguages())
					if (l.getLang().equals(langName))
						lang = l;
				
				JSONObject oneLang = (JSONObject) allLangs.get(langName);
				int possible = Integer.parseInt(""+oneLang.get("possible"));
				int passed = Integer.parseInt(""+oneLang.get("passed"));
				game.studentWork.setPossibleExercises((String) lessonName, lang, possible);
				game.studentWork.setPassedExercises((String) lessonName, lang, passed);
			}
		}
	}

	@Override
	public void cleanAll(File path) {
		for (Lesson lesson : this.game.getLessons())
			cleanLesson(path, lesson);
	}

	@Override
	public void storeLesson(File path, Lesson lesson) throws UserAbortException {
		File saveFile = openSaveFile(path, lesson);

		if (!saveFile.exists()) {
			File parentDirectory = saveFile.getParentFile().getAbsoluteFile();
			if (!parentDirectory.exists()) {
				if (!parentDirectory.mkdir()) {
					throw new RuntimeException("cannot create session store directory '" + parentDirectory + "'");
				}
			}
		}

		ZipOutputStream zos = null;
		boolean wroteSomething = false;
		try {
			zos = new ZipOutputStream(new FileOutputStream(saveFile));
			zos.setMethod(ZipOutputStream.DEFLATED);
			zos.setLevel(Deflater.BEST_COMPRESSION);

			zos.putNextEntry(new ZipEntry("README"));
			String text = "This file is a PLM session file. Please see http://www.loria.fr/~quinson/Teaching/PLM/ for more details";
			zos.write(text.getBytes());
			zos.closeEntry();
			
			for (Lecture lecture : lesson.exercises()) {
				if (lecture instanceof Exercise) {
					Exercise exercise = (Exercise) lecture;
					for (ProgrammingLanguage lang:exercise.getProgLanguages()) {
						// flag successfully passed exercise
						if (game.studentWork.getPassed(exercise, lang)) {
							ZipEntry ze = new ZipEntry(exercise.getId() + "/DONE."+lang.getExt());
							zos.putNextEntry(ze);
							byte[] bytes = new byte[1];
							// bytes[0] = 'x';
							zos.write(bytes);
							zos.closeEntry();
							wroteSomething  = true;
						}

					// save exercise body
						for (int i = 0; i < exercise.getSourceFileCount(lang); i++) {
							SourceFile sf = exercise.getSourceFile(lang,i);

							if (!(sf instanceof SourceFileRevertable))
								continue;

							SourceFileRevertable srcFile = (SourceFileRevertable) sf;

							ZipEntry ze = new ZipEntry(lang+"/"+exercise.getId() + "/" + srcFile.getName());
							zos.putNextEntry(ze);

							String content = srcFile.getBody();

							if (content.length() > 0 && content.charAt(content.length() - 1) != '\n') {
								content = content + "\n";
							}

							byte[] bytes = srcFile.getBody().getBytes();
							zos.write(bytes);
							zos.closeEntry();
							wroteSomething = true;
						}
					} // foreach lang
				} // is exercise
			} // end-for lecture
			ZipEntry ze = new ZipEntry("currently_selected_exercise");
			zos.putNextEntry(ze);
			zos.write(lesson.getCurrentExercise().getClass().getCanonicalName().getBytes());
			zos.closeEntry();

		} catch (IOException ex) { // FileNotFoundException or IOException
			// FIXME: should raise an exception and not show a dialog (it is not a UI class)
			ex.printStackTrace();
			Object[] options = { getGame().i18n.tr("Ok, quit and lose my data"), getGame().i18n.tr("Please stop! I'll save it myself first") };
			int n = JOptionPane.showOptionDialog(null, getGame().i18n.tr("The PLM were unable to save your session file for lesson {0} ({1}:{2}).\n\n"
					+ " Would you like proceed anyway (and lose any solution typed so far)?",
					lesson.getName(),ex.getClass().getSimpleName(),ex.getLocalizedMessage()),
					getGame().i18n.tr("Your changes are NOT saved"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
					null, options, options[1]);
			if (n == 1) {
				throw new UserAbortException(getGame().i18n.tr("User aborted saving on system error"),ex);
			}


		} finally {
			try {
				if (zos != null && wroteSomething)
					zos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			if (!wroteSomething || saveFile.length() == 0)
				saveFile.delete();
		}
	}

	public void loadLesson(File path, Lesson lesson) {
		File saveFile = openSaveFile(path, lesson);

		if (!saveFile.exists() || saveFile.length()==0)
			return;

		ZipFile zf = null;
		try {
			zf = new ZipFile(saveFile);

			for (Lecture lecture : lesson.exercises()) {
				if (lecture instanceof Exercise) {
					Exercise exercise = (Exercise) lecture;

					for (ProgrammingLanguage lang:exercise.getProgLanguages()) {
						ZipEntry entry = zf.getEntry(exercise.getId() + "/DONE."+lang.getExt());
						if (entry != null) {
							game.studentWork.setPassed(exercise, lang, true);
						}

						for (int i = 0; i < exercise.getSourceFileCount(lang); i++) {
							SourceFile srcFile = exercise.getSourceFile(lang,i);

							ZipEntry srcEntry = zf.getEntry(lang+"/"+exercise.getId() + "/" + srcFile.getName());
							if (srcEntry == null) /* try to load using the old format (not specifying the programming language) */
								srcEntry = zf.getEntry(exercise.getId() + "/" + srcFile.getName());

							if (srcEntry != null) {
								InputStream is = zf.getInputStream(srcEntry);

								BufferedReader br = null;
								try {
									br = new BufferedReader(new InputStreamReader(is));

									String s;
									StringBuffer b = new StringBuffer();

									while ((s = br.readLine()) != null) {
										b.append(s);
										b.append("\n");
									}

									srcFile.setBody(b.toString());
								} catch (IOException e) {
									e.printStackTrace();
								} finally {
									try {
										br.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
					} // foreach lang
				} // is exercise
			} // end-for lecture

			/* Get the previously selected exercise */
			ZipEntry entry = zf.getEntry("currently_selected_exercise");
			if (entry != null) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(zf.getInputStream(entry)));
					String exoName = br.readLine();

					lesson.setCurrentExercise(exoName);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (IOException ex) { // ZipExecption or IOException
			// FIXME: should raise an exception and not show a dialog (it is not a UI class)
			ex.printStackTrace();
			Object[] options = { getGame().i18n.tr("Proceed"), getGame().i18n.tr("Abort") };
			int n = JOptionPane.showOptionDialog(null, getGame().i18n.tr("The PLM were unable to load your code for lesson {0} ({1}:{2}).\n\n"
					+ " Would you like proceed anyway (and lose any solution typed previously)?",
					lesson.getName(), ex.getClass().getSimpleName(), ex.getMessage()),
					getGame().i18n.tr("Error while loading your session"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
					null, options, options[1]);
			if (n == 1) {
				System.err.println(getGame().i18n.tr("Abording on user request"));
				// should throw exception to calling method, no System.exit() here !!!
				System.exit(1);
			}
		} finally {
			try {
				if (zf != null)
					zf.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	@Override
	public void cleanLesson(File path, Lesson lesson) {
		File saveFile = openSaveFile(path, lesson);

		if (saveFile.exists()) {
			if (saveFile.delete()) {
				game.getLogger().log("ZipSessionKit:cleanup");
				game.getLogger().log("cannot remove session store directory");
			}
		}
	}
	
	public Game getGame() {
		return game;
	}

	@Override
	public void setUserUUID(String userUUID) { 
		// Do nothing
	}
}
