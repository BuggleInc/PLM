package jlm.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jlm.lesson.Exercise;
import jlm.lesson.Lesson;
import jlm.lesson.SourceFileRevertable;
import jlm.lesson.SourceFile;


public class FileSessionKit implements ISessionKit {

	private Game game;

	private static String HOME_DIR = System.getProperty("user.home");

	private static String SEP = System.getProperty("file.separator");

	private static File SAVE_DIR = new File(HOME_DIR + SEP + ".jlm");

	public FileSessionKit(Game game) {
		this.game = game;
	}

	public void store() {
		if (!SAVE_DIR.exists())
			if (! SAVE_DIR.mkdir()) {
				Logger.log("FileSessionKit:store", "cannot create session store directory");
			};

		// File lessonDir;
		File exerciseDir;
		for (Lesson lesson : this.game.getLessons()) {
			// lessonDir = new File(saveDir + sep +
			// lesson.getClass().getName());
			// if (!lessonDir.exists())
			// lessonDir.mkdir();
			// // save lesson data

			for (Exercise exercise : lesson.exercises()) {
				exerciseDir = new File(SAVE_DIR, exercise.getClass().getName());
				if (!exerciseDir.exists())
					if (! exerciseDir.mkdir()) 
						Logger.log("FileSessionKit:store", "cannot remove "+exerciseDir+" directory");


				// create file DONE if exercise has been successfully passed
				File exerciseFile = new File(exerciseDir, "DONE");
				if (exercise.isSuccessfullyPassed()) {
					if (!exerciseFile.exists()) {
						try {
							exerciseFile.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					if (exerciseFile.exists()) {
						if (!exerciseFile.delete()) {
							Logger.log("FileSessionKit:store", "cannot remove "+exerciseFile+" directory");
						}
					}
				}

				// save exercise body
				for (int i = 0; i < exercise.publicSourceFileCount(); i++) {
					SourceFile sf = exercise.getPublicSourceFile(i);
					
					if (!(sf instanceof SourceFileRevertable))
						continue;
					
					SourceFileRevertable srcFile = (SourceFileRevertable)sf;
					File outputFile = new File(exerciseDir, srcFile.getName());
					
					if (srcFile.hasChanged()) {
						BufferedWriter bw = null;
						try {
							bw = new BufferedWriter(new FileWriter(outputFile));
							bw.write(srcFile.getBody());
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								bw.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						if (outputFile.exists())
							if (! outputFile.delete())
								Logger.log("FileSessionKit:store", "cannot remove "+outputFile);
					}
				}
			} // end-for exercise
		} // end-for lesson
	}

	public void load() {
		if (!SAVE_DIR.exists())
			return;

		// File lessonDir;
		File exerciseDir;
		for (Lesson lesson : this.game.getLessons()) {
			for (Exercise exercise : lesson.exercises()) {
				exerciseDir = new File(SAVE_DIR, exercise.getClass().getName());
				if (!exerciseDir.exists())
					continue;

				File exerciseFile = new File(exerciseDir, "DONE");
				if (exerciseFile.exists()) {
					exercise.successfullyPassed();
				}

				// load exercise body
				for (int i = 0; i < exercise.publicSourceFileCount(); i++) {
					SourceFile srcFile = exercise.getPublicSourceFile(i);

					File of = new File(exerciseDir, srcFile.getName());
					if (of.exists()) {
						BufferedReader br = null;
						try {
							br = new BufferedReader(new FileReader(of));
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
	}

	public void cleanUp() {
		if (!SAVE_DIR.exists())
			return;

		File exerciseDir;
		for (Lesson lesson : this.game.getLessons()) {
			for (Exercise exercise : lesson.exercises()) {
				exerciseDir = new File(SAVE_DIR, exercise.getClass().getName());
				if (!exerciseDir.exists())
					continue;

				File exerciseFile = new File(exerciseDir, "DONE");
				if (exerciseFile.exists()) {
					if (!exerciseFile.delete()) {
						Logger.log("FileSessionKit:cleanUp", "cannot remove "+exerciseFile);
					};
				}

				for (int i = 0; i < exercise.publicSourceFileCount(); i++) {
					SourceFile srcFile = exercise.getPublicSourceFile(i);

					File of = new File(exerciseDir, srcFile.getName());
					if (of.exists()) {
						if (! of.delete()) {
							Logger.log("FileSessionKit:cleanUp", "cannot remove "+exerciseDir+"/"+srcFile.getName()+" directory");				
						}
					}
				}

				if (! exerciseDir.delete()) {
					Logger.log("FileSessionKit:cleanUp", "cannot remove "+exerciseDir+" directory");					
				}
			} // end-for exercise
		} // end-for lesson
		if (! SAVE_DIR.delete()) {
			Logger.log("FileSessionKit:cleanup", "cannot remove session store directory");
		}
	}
}
