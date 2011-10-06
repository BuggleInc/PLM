package jlm.core.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import jlm.core.model.lesson.SourceFile;
import jlm.core.model.lesson.SourceFileAliased;
import jlm.core.model.lesson.SourceFileRevertable;

/**
 * Implementation of the {@link ISessionKit} saving the student data in a zip file.
 * 
 * Unless you edit the source, this is the session kit used.
 */
public class ZipSessionKit implements ISessionKit {

	private Game game;

	private static String HOME_DIR = System.getProperty("user.home");

	private static String SEP = System.getProperty("file.separator");

	private static File SAVE_DIR = new File(HOME_DIR + SEP + ".jlm");

	public ZipSessionKit(Game game) {
		this.game = game;
	}

	private File openSaveFile(File path, Lesson lesson) {
		if (path == null)
			path = SAVE_DIR;

		String name = lesson.getClass().getCanonicalName();
		Pattern namePattern = Pattern.compile(".Main$");
		Matcher nameMatcher = namePattern.matcher(name);
		name = nameMatcher.replaceAll("");

		namePattern = Pattern.compile("^lessons.");
		nameMatcher = namePattern.matcher(name);
		name = nameMatcher.replaceAll("");

		return new File(path, "jlm-"+name+".zip");
	}

	@Override
	public void storeAll(File path) throws UserAbortException {
		for (Lesson lesson : this.game.getLessons())
			storeLesson(path, lesson);
	}

	@Override
	public void loadAll(File path) {
		for (Lesson lesson : this.game.getLessons())
			loadLesson(path, lesson);
	}

	@Override
	public void cleanAll() {
		for (Lesson lesson : this.game.getLessons())
			cleanLesson(lesson);
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
			String text = "This file is a JLM session file. Please see http://www.loria.fr/~quinson/Teaching/JLM/ for more details";
			zos.write(text.getBytes());
			zos.closeEntry();
			
			for (Lecture lecture : lesson.exercises()) {
				if (lecture instanceof Exercise) {
					Exercise exercise = (Exercise) lecture;
					// flag successfully passed exercise
					if (exercise.isSuccessfullyPassed()) {
						ZipEntry ze = new ZipEntry(exercise.getClass().getName() + "/DONE");
						zos.putNextEntry(ze);
						byte[] bytes = new byte[1];
						// bytes[0] = 'x';
						zos.write(bytes);
						zos.closeEntry();
						wroteSomething  = true;
					}

					// save exercise body
					for (ProgrammingLanguage lang:exercise.getProgLanguages()) 
						for (int i = 0; i < exercise.publicSourceFileCount(lang); i++) {
							SourceFile sf = exercise.getPublicSourceFile(lang,i);

							if (!(sf instanceof SourceFileRevertable))
								continue;

							SourceFileRevertable srcFile = (SourceFileRevertable) sf;

							ZipEntry ze = new ZipEntry(lang+"/"+exercise.getClass().getName() + "/" + srcFile.getName());
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
				} // is exercise
			} // end-for lecture

		} catch (IOException ex) { // FileNotFoundException or IOException
			// FIXME: should raise an exception and not show a dialog (it is not a UI class)
			ex.printStackTrace();
			Object[] options = { "Ok, quit and lose my data", "Please stop! I'll save it myself first" };
			int n = JOptionPane.showOptionDialog(null, "JLM were unable to save your session file for lesson "+lesson.getName()+"("
					+ ex.getClass().getSimpleName() + ":" + ex.getMessage() + ").\n\n"
					+ " Would you like proceed anyway (and lose any solution typed so far)?",
					"Your changes are NOT saved", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
					null, options, options[1]);
			if (n == 1) {
				throw new UserAbortException("User aborted saving on system error",ex);
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

					ZipEntry entry = zf.getEntry(exercise.getClass().getName() + "/DONE");
					if (entry != null) {
						exercise.successfullyPassed();
					}

					for (ProgrammingLanguage lang:exercise.getProgLanguages())
						for (int i = 0; i < exercise.publicSourceFileCount(lang); i++) {
							SourceFile srcFile = exercise.getPublicSourceFile(lang,i);

							if (srcFile instanceof SourceFileAliased)
								continue;

							ZipEntry srcEntry = zf.getEntry(lang+"/"+exercise.getClass().getName() + "/" + srcFile.getName());
							if (srcEntry == null) /* try to load using the old format (not specifying the programming language) */
								srcEntry = zf.getEntry(exercise.getClass().getName() + "/" + srcFile.getName());

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
				} // is exercise
			} // end-for lecture

		} catch (IOException ex) { // ZipExecption or IOException
			// FIXME: should raise an exception and not show a dialog (it is not a UI class)
			ex.printStackTrace();
			Object[] options = { "Proceed", "Abort" };
			int n = JOptionPane.showOptionDialog(null, "JLM were unable to load your code for lesson "+lesson.getName()+" ("
					+ ex.getClass().getSimpleName() + ":" + ex.getMessage() + ").\n\n"
					+ " Would you like proceed anyway (and lose any solution typed previously)?",
					"Error while loading your session", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
					null, options, options[1]);
			if (n == 1) {
				System.err.println("Abording on user request");
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
	public void cleanLesson(Lesson lesson) {
		File saveFile = openSaveFile(null, lesson);

		if (saveFile.exists()) {
			if (saveFile.delete()) {
				Logger.log("ZipSessionKit:cleanup", "cannot remove session store directory");
			}
		}
	}

	@Override
	public void storeAll() throws UserAbortException {
		storeAll(null);
	}

	@Override
	public void loadAll() {
		loadAll(null);
	}

}
