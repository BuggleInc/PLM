package jlm.core.model.lesson;

import java.util.HashMap;
import java.util.Map;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.session.SourceFile;
import jlm.universe.World;

/** This class of Exercises are useful to merge the entity and world within the same class.
 * #BatExercice is a known implementation 
 *
 *
 */

public abstract class ExerciseTemplatingEntity extends ExerciseTemplated {
	protected Map<ProgrammingLanguage,String> corrections = new HashMap<ProgrammingLanguage, String>();
	private boolean isSetup = false;
	
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
			throw new RuntimeException("Cannot find an entity of name "+entName+" for this exercise. You should fix your paths and/or use langTemplate().");

		SourceFile sf = sourceFiles.get(Game.JAVA).get(0);
		sf.setTemplate("$package "+template+" "+sf.getTemplate()+" $body }");
		//System.out.println("New template: "+sf.getTemplate());
		computeAnswer();
		isSetup  = true;
	}
	protected void langTemplate(ProgrammingLanguage pl, String entName, String initialCode, String correction) {
		/* The following test is intended to make sure that this function is called before setup() right above.
		 * This is because setup() needs all programming languages to be declared when it runs */
		if (isSetup)
			throw new RuntimeException("The exercise "+getName()+" is already setup, too late to add a programming language template");
		
		newSource(pl, entName, initialCode, "$body",0,"(templating entity)");
		corrections.put(pl, initialCode+correction);
		addProgLanguage(pl);
	}
}
