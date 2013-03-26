package jlm.core.model.lesson;

import java.util.List;

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
		worldDuplicate(ws);
		
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
	protected void computeAnswer() {
		for (World aw : answerWorld) 
			run(aw);
	}

	@Override
	public void run(List<Thread> runnerVect){
		mutateEntity(tabName);
		for (int i=0; i<currentWorld.length; i++) {
			currentWorld[i].doDelay();
			currentWorld[i].runEntities(runnerVect);
		}
	}
	public abstract void run(World w);
}
