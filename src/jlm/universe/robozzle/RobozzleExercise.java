package jlm.universe.robozzle;

import java.util.List;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;

public class RobozzleExercise extends ExerciseTemplated {
	public RobozzleExercise(Lesson lesson) {
		super(lesson);
		//sourceFiles.add(new LightBotSourceFile("Code"));
	}

	@Override
	protected void setup(World[] ws) {
		worldDuplicate(ws);
		/* remove entities from the answer world: we don't care of where the bot is at the end */
		for (World w :answerWorld)
			w.emptyEntities();
		computeAnswer();
	}

	@Override
	protected void computeAnswer() {
		for (int w=0;w<answerWorld.length;w++) {
			RobozzleWorld.CellIterator ci = ((RobozzleWorld)answerWorld[w]).new CellIterator();
			while (ci.hasNext()) {
				ci.next().removeStar();
			}
		}

	}
	@Override
	public boolean check() {
		for (int w=0;w<currentWorld.length;w++) {
			RobozzleWorld.CellIterator ci = ((RobozzleWorld)currentWorld[w]).new CellIterator();
			while (ci.hasNext()) {
				RobozzleWorldCell cell = ci.next();
				if (cell.hasStar())
					return false;
			}
		}
		return true;	
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
		/* No demo for robozzle: this is a puzzle game, you have to search for the answer by yourself */
	}
}
