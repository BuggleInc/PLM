package jlm.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


import lessons.Exercise;
import lessons.Lesson;
import lessons.RevertableSourceFile;
import lessons.SourceFile;

public class ZipSessionKit implements ISessionKit {

	private Game game;

	private static String HOME_DIR = System.getProperty("user.home");

	private static String SEP = System.getProperty("file.separator");

	private static File SAVE_DIR = new File(HOME_DIR + SEP + ".jlm");

	private static File SAVE_FILE = new File(SAVE_DIR, "jlm.zip");

	public ZipSessionKit(Game game) {
		this.game = game;
	}

	public void store() {
		if (!SAVE_DIR.exists()) {
			if (! SAVE_DIR.mkdir()) {
				Logger.log("ZipSessionKit:store", "cannot create session store directory");
			};
		}

		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(SAVE_FILE));
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
						RevertableSourceFile srcFile = exercise.getPublicSourceFile(i);

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

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}

	public void load() {
		if (!SAVE_FILE.exists())
			return;

		ZipFile zf = null;
		try {
			zf = new ZipFile(SAVE_FILE);

			for (Lesson lesson : this.game.getLessons()) {
				for (Exercise exercise : lesson.exercises()) {

					ZipEntry entry = zf.getEntry(exercise.getClass().getName() + "/DONE");
					if (entry != null) {
						exercise.successfullyPassed();
					}

					for (int i = 0; i < exercise.publicSourceFileCount(); i++) {
						SourceFile srcFile = exercise.getPublicSourceFile(i);

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

		} catch (ZipException zex) {
			zex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				zf.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public void cleanUp() {
		if (!SAVE_FILE.exists())
			return;
		else {
			if (SAVE_FILE.delete()) {
				Logger.log("ZipSessionKit:cleanup", "cannot remove session store directory");
			};
		}

	}
}
