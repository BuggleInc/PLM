package lessons.lightbot;

import java.util.List;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.World;

public class LightBotExercise extends ExerciseTemplated {
	public LightBotExercise(Lesson lesson) {
		super(lesson);
	}

	@Override
	protected void setup(World[] ws) {		
		worldDuplicate(ws);
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
	public boolean check() {
		boolean result = true;
		for (int w=0;w<currentWorld.length;w++) {
			LightBotWorld.CellIterator ci = ((LightBotWorld)currentWorld[w]).new CellIterator();
			while (ci.hasNext()) {
				LightBotWorldCell cell = ci.next();
				if (cell.isLight())
					System.out.println(""+cell.getX()+","+cell.getY()+": "+(cell.isLight()?(cell.isLightOn()?"LIGHT ON":"LIGHT OFF"):"no light"));
				if (! cell.getLightOnOrNone())
					result = false;
			}
		}
		return result;	
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
		for (int i=0; i<initialWorld.length; i++) { 
			answerWorld[i].reset(initialWorld[i]);
			answerWorld[i].doDelay();
		}
				
		for (int i=0; i<answerWorld.length; i++)
			answerWorld[i].runEntities(runnerVect);
	}
}
