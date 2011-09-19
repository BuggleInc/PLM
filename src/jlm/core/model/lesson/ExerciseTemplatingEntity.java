package jlm.core.model.lesson;

import java.util.List;

import jlm.core.model.Game;
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
		this.tabName=entName;
		worldDuplicate(ws);
		newSourceFromFile(Game.JAVA, entName, getClass().getCanonicalName(), "java"); 
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
