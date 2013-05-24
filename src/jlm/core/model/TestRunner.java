package jlm.core.model;

import java.io.IOException;
import java.util.Locale;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lecture;
import jlm.universe.Entity;
import jlm.universe.World;

public class TestRunner {
	int worldTotal = 0, worldFailed=0;
	int exoTotal = 0, exoFailed=0;

	private void testLesson(String name) {
		Game g = Game.getInstance();

		System.err.println("Loading lesson "+name);
		g.setProgramingLanguage(Game.JAVA);
		g.switchLesson(name);
		System.err.println("Lesson "+name+" loaded");

		for (Lecture l : g.getCurrentLesson().exercises()) {
			if (l instanceof ExerciseTemplated) {
				ExerciseTemplated exo = (ExerciseTemplated) l;
				System.err.println("XX Testing exercise "+exo.getClass().getName());
				Game.getInstance().setCurrentExercise(l);
				exoTotal++;
				boolean failingWorld = false;

				for (int worldRank=0; worldRank<exo.getWorldCount(); worldRank++) {
					World initial = exo.getInitialWorldList().get(worldRank); 
					World current = exo.getCurrentWorldList().get(worldRank); 
					World answer  = exo.getAnswerWorldList().get(worldRank);
					initial.setDelay(0);

					for (ProgrammingLanguage lang : exo.getProgLanguages()) {
						if (lang.equals(Game.JAVASCRIPT) || lang.equals(Game.JAVA))
							continue;
						worldTotal++;
						System.err.println("  XX Testing world "+current.getName()+" in "+lang);
						g.setProgramingLanguage(lang);

						current.reset(initial);

						for (Entity ent: current.getEntities()) {
							StringBuffer sb = null;
							try {
								sb = FileUtils.readContentAsText(exo.getNameOfCorrectionEntity(), lang.getExt(), false);
							} catch (IOException ex) {
								System.err.println("Missing entity "+exo.getNameOfCorrectionEntity()+"."+lang.getExt()+"; test aborted");			
								worldFailed++;
								failingWorld = true;
							}
							ent.setScript(lang, sb.toString());
							ent.runIt();
						}

						if (!current.equals(answer)) {
							String diff = answer.diffTo(current);
							System.err.println("Test failed: the worlds "+worldRank+" differ in "+lang+":\n"+diff);
							worldFailed++;
							failingWorld = true;
							System.exit(1);
						}
					}
				}
				if (failingWorld)
					exoFailed++;
			} else {
				System.err.println("XX Exercise "+l+" is not templated");
			}
		}
		System.err.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.err.println("Failing exercises:"+exoFailed+" of "+exoTotal+" (failing worlds: "+worldFailed+" of "+worldTotal+")");
	}


	public static void main(String[] args) {

		FileUtils.setLocale(new Locale("en"));
		TestRunner tr = new TestRunner();
		
		tr.testLesson("lessons.welcome");
	}
}
