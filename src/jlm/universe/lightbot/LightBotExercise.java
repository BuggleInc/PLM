package jlm.universe.lightbot;

import java.util.List;

import jlm.core.model.Game;
import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;

public class LightBotExercise extends ExerciseTemplated {
	public LightBotExercise(Lesson lesson) {
		super(lesson);
		setProgLanguages(Game.LIGHTBOT);
		getSourceFiles(Game.LIGHTBOT).add(new LightBotSourceFile("Code"));
	}

	@Override
	protected void setup(World[] ws) {
		for (World w : ws) 
			((LightBotWorld) w).rotateLeft();
		
		worldDuplicate(ws);
		/* remove entities from the answer world: we don't care of where the bot is at the end */
		for (World w :answerWorld)
			w.emptyEntities();
		computeAnswer();
	}

	@Override
	protected void computeAnswer() {
		for (int w=0;w<answerWorld.length;w++) {
			LightBotWorld.CellIterator ci = ((LightBotWorld)answerWorld[w]).new CellIterator();
			while (ci.hasNext()) {
				ci.next().setLightOn();
			}
		}
	}
	@Override
	public void check() {
		lastResult = new ExecutionProgress();
		for (int w=0;w<currentWorld.length;w++) {
			LightBotWorld.CellIterator ci = ((LightBotWorld)currentWorld[w]).new CellIterator();
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
	public void run(List<Thread> runnerVect){
		reset();

		for (int i=0; i<currentWorld.length; i++)
			currentWorld[i].doDelay();

		for (int i=0; i<currentWorld.length; i++)
			currentWorld[i].runEntities(runnerVect);
	}

	@Override
	public void runDemo(List<Thread> runnerVect){		
		/* No demo for lightbot: this is a puzzle game, you have to search for the answer by yourself */
	}
}
