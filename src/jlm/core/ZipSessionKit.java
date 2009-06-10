package jlm.core;

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

import jlm.lesson.Exercise;
import jlm.lesson.Lesson;
import jlm.lesson.SourceFile;
import jlm.lesson.SourceFileAliased;
import jlm.lesson.SourceFileRevertable;

public class ZipSessionKit implements ISessionKit {

	private Game game;

	private static String HOME_DIR = System.getProperty("user.home");

	private static String SEP = System.getProperty("file.separator");

	private static File SAVE_DIR = new File(HOME_DIR + SEP + ".jlm");

	private static String SAVE_FILENAME = "jlm.zip";
	private static File SAVE_FILE = new File(SAVE_DIR, SAVE_FILENAME);

	public ZipSessionKit(Game game) {
		this.game = game;
	}

	public void store() {
		this.store(SAVE_FILE);
	}

	public void load() {
		this.load(SAVE_FILE);
	}

	public void cleanUp() {
		this.cleanUp(SAVE_FILE);
	}

	public void store(File saveFile) {
		if (!saveFile.exists()) {
			File parentDirectory = saveFile.getParentFile().getAbsoluteFile();
			if (!parentDirectory.exists()) {
				if (!parentDirectory.mkdir()) {
					Logger.log("ZipSessionKit:store", "cannot create session store directory '" + parentDirectory + "'");
				}
			}
		}

		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(saveFile));
			zos.setMethod(ZipOutputStream.DEFLATED);
			zos.setLevel(Deflater.BEST_COMPRESSION);

			for (Lesson lesson : this.game.getLessons()) {
				for (Exercise exercise : lesson.exercises()) {

					// flag successfully passed exercise
					if (exercise.isSuccessfullyPassed()) {
						ZipEntry ze = new ZipEntry(exercise.getClass().getName() + "/DONE");
						zos.putNextEntry(ze);
						byte[] bytes = new byte[1];
						// bytes[0] = 'x';
						zos.write(bytes);
						zos.closeEntry();
					}

					// save exercise body
					for (int i = 0; i < exercise.publicSourceFileCount(); i++) {
						SourceFile sf = exercise.getPublicSourceFile(i);

						if (!(sf instanceof SourceFileRevertable))
							continue;

						SourceFileRevertable srcFile = (SourceFileRevertable) sf;

						ZipEntry ze = new ZipEntry(exercise.getClass().getName() + "/" + srcFile.getName());
						zos.putNextEntry(ze);

						String content = srcFile.getBody();

						if (content.length() > 0 && content.charAt(content.length() - 1) != '\n') {
							content = content + "\n";
						}

						byte[] bytes = srcFile.getBody().getBytes();
						zos.write(bytes);
						zos.closeEntry();
					}
				} // end-for exercise
			} // end-for lesson

		} catch (IOException ex) { // FileNotFoundException or IOException
			// FIXME: should raise an exception and not show a dialog (it is not a UI class)
			JOptionPane.showMessageDialog(null, "JLM were unable to save your session file:\n"
					+ ex.getClass().getSimpleName() + ": " + ex.getMessage(), "Your changes are NOT saved.",
					JOptionPane.ERROR_MESSAGE);

		} finally {
			try {
				if (zos != null)
					zos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}

	public void load(File saveFile) {
		if (!saveFile.exists())
			return;

		ZipFile zf = null;
		try {
			zf = new ZipFile(saveFile);

			for (Lesson lesson : this.game.getLessons()) {
				for (Exercise exercise : lesson.exercises()) {

					ZipEntry entry = zf.getEntry(exercise.getClass().getName() + "/DONE");
					if (entry != null) {
						exercise.successfullyPassed();
					}

					for (int i = 0; i < exercise.publicSourceFileCount(); i++) {
						SourceFile srcFile = exercise.getPublicSourceFile(i);

						if (srcFile instanceof SourceFileAliased)
							continue;

						ZipEntry srcEntry = zf.getEntry(exercise.getClass().getName() + "/" + srcFile.getName());

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
				} // end-for exercise
			} // end-for lesson

		} catch (IOException ex) { // ZipExecption or IOException
			// FIXME: should raise an exception and not show a dialog (it is not a UI class)
			ex.printStackTrace();
			Object[] options = { "Proceed", "Abort" };
			int n = JOptionPane.showOptionDialog(null, "JLM were unable to load your session file ("
					+ ex.getClass().getSimpleName() + ":" + ex.getMessage() + ").\n\n"
					+ " Would you like proceed anyway (and loose any solution typed so far)?",
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

	public void cleanUp(File saveFile) {
		if (!saveFile.exists())
			return;
		else {
			if (saveFile.delete()) {
				Logger.log("ZipSessionKit:cleanup", "cannot remove session store directory");
			}
		}
	}

}
