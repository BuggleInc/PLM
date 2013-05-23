package jlm.core.model.lesson;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.universe.World;

/** This class of Exercises are useful to merge the entity and world within the same class.
 * #BatExercice is a known implementation 
 *
 *
 */

public abstract class ExerciseTemplatingEntity extends ExerciseTemplated {
	
	public ExerciseTemplatingEntity(Lesson lesson) {
		super(lesson);
	}
	protected void setup(World[] ws, String entName, String template) {
		boolean foundOne = false;
		this.tabName=entName;
		setupWorlds(ws);
		
		for (ProgrammingLanguage pl : Game.getProgrammingLanguages()) {
			try {
				newSourceFromFile(pl, entName, getClass().getCanonicalName());
				foundOne = true;
				addProgLanguage(pl);
			} catch (NoSuchEntityException e) {
				/* Ok, this language does not work. I can deal with it */
			} 
		}
		if (!foundOne)
			throw new RuntimeException("Cannot find an entity of name "+entName+" for this exercise. You should fix your pathes and such");

		SourceFile sf = sourceFiles.get(Game.JAVA).get(0);
		sf.setTemplate("$package "+template+" "+sf.getTemplate()+" $body }");
		//System.out.println("New template: "+sf.getTemplate());
		computeAnswer();
	}
	protected void langTemplate(ProgrammingLanguage pl, String entName, String initialCode) {
		newSource(pl, entName, initialCode, "$body");
		addProgLanguage(pl);
	}
	protected void computeAnswer() {
		for (World aw : answerWorld) 
			run(aw);
	}

	public abstract void run(World w);
}
