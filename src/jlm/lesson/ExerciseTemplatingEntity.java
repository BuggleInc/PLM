package jlm.lesson;

import java.util.List;

import jlm.universe.World;

public abstract class ExerciseTemplatingEntity extends ExerciseTemplated {
	
	public ExerciseTemplatingEntity(Lesson lesson) {
		super(lesson);
	}
	protected void setup(World[] ws, String entName, String template) {
		this.tabName=entName;
		worldDuplicate(ws);
		newSourceFromFile(entName, getClass().getCanonicalName(), "java"); 
		SourceFile sf = sourceFiles.get(0);
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
