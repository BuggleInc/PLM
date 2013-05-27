package jlm.universe.lightbot;

import java.util.List;
import java.util.Vector;

import jlm.core.model.Game;
import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;

public class LightBotExercise extends ExerciseTemplated {
	public LightBotExercise(Lesson lesson) {
		super(lesson);
		addProgLanguage(Game.LIGHTBOT);
		if (getProgLanguages().size()>1) 
			throw new RuntimeException("More than one language defined in a LightbotExercise. Please report this bug.");
		getSourceFilesList(Game.LIGHTBOT).add(new LightBotSourceFile("Code"));
	}

	@Override
	protected void setup(World[] ws) {
		for (World w : ws) 
			((LightBotWorld) w).rotateLeft();
		
		setupWorlds(ws);
		/* remove entities from the answer world: we don't care of where the bot is at the end */
		for (World w :answerWorld)
			w.emptyEntities();
		computeAnswer();
	}

	@Override
	protected void computeAnswer() {
		for (int w=0;w<answerWorld.size();w++) {
			LightBotWorld.CellIterator ci = ((LightBotWorld)answerWorld.get(w)).new CellIterator();
			while (ci.hasNext()) {
				ci.next().setLightOn();
			}
		}
	}
	@Override
	public void check() {
		lastResult = new ExecutionProgress();
		for (int w=0;w<currentWorld.size();w++) {
			LightBotWorld.CellIterator ci = ((LightBotWorld)currentWorld.get(w)).new CellIterator();
			while (ci.hasNext()) {
				LightBotWorldCell cell = ci.next();
				if (cell.isLight()) {
					lastResult.totalTests++;
					if (cell.isLightOn())
						lastResult.passedTests++;
				}
			}
			int stillOff = lastResult.totalTests - lastResult.passedTests;
			if (stillOff == 1) 
				lastResult.details = "The light is still off";
			if (stillOff > 1)
				lastResult.details = stillOff + " lights (out of "+lastResult.totalTests+") are still off";

		}
	}
	@Override
	public void run(List<Thread> runnerVect){ // FIXME: that's a redefinition to the same, right?
		reset();

		for (int i=0; i<currentWorld.size(); i++)
			currentWorld.get(i).doDelay();

		for (int i=0; i<currentWorld.size(); i++)
			currentWorld.get(i).runEntities(runnerVect, lastResult);
	}

	@Override
	public void runDemo(List<Thread> runnerVect){		
		/* No demo for lightbot: this is a puzzle game, you have to search for the answer by yourself */
	}
	
	@Override
	final protected void mutateEntities(Vector<World> worlds, String newClassName) {
		throw new RuntimeException("Why are you trying to mutate Lightbot entities, you weirdo?! super.mutateEntities() is not ready for that.");
	}
}
