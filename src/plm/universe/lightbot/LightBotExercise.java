package plm.universe.lightbot;

import java.util.List;

import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;

public class LightBotExercise extends ExerciseTemplated {
	public LightBotExercise(Game game, Lesson lesson) {
		super(game, lesson);
		addProgLanguage(Game.LIGHTBOT);
		if (getProgLanguages().size()>1) 
			throw new RuntimeException("More than one language defined in a LightbotExercise. Please report this bug.");
		getSourceFilesList(Game.LIGHTBOT).add(new LightBotSourceFile(game, "Code"));
	}

	@Override
	protected void setup(World[] ws) {
		for (World w : ws) 
			((LightBotWorld) w).rotateLeft();
		
		setupWorlds(ws, 0);
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
		lastResult = new ExecutionProgress(Game.LIGHTBOT);
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
				lastResult.executionError = getGame().i18n.tr("A light is still off.");
			if (stillOff > 1)
				lastResult.executionError = getGame().i18n.tr("{0} lights (out of {1}) are still off.",stillOff,lastResult.totalTests);

			if (stillOff == 0)
				lastResult.outcome = ExecutionProgress.outcomeKind.PASS;
			else
				lastResult.outcome = ExecutionProgress.outcomeKind.FAIL;
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
	final public void mutateEntities(WorldKind kind, StudentOrCorrection what) {
		throw new RuntimeException("Why are you trying to mutate Lightbot entities, you weirdo?! super.mutateEntities() is not ready for that.");
	}
}
