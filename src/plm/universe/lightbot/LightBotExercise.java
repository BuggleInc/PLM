package plm.universe.lightbot;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.World;

public class LightBotExercise extends ExerciseTemplated {
	public LightBotExercise(String id, String name) {
		super(id, name);
		ProgrammingLanguage lightbot = ProgrammingLanguage.getProgrammingLanguage("Lightbot");
		if (getProgLanguages().size()>1) 
			throw new RuntimeException("More than one language defined in a LightbotExercise. Please report this bug.");
		getSourceFilesList(lightbot).add(new LightBotSourceFile("Code"));
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

	// FIXME: Switch to same workflow as other type of worlds
	protected void computeAnswer() {
		for (int w=0;w<answerWorld.size();w++) {
			LightBotWorld.CellIterator ci = ((LightBotWorld)answerWorld.get(w)).new CellIterator();
			while (ci.hasNext()) {
				ci.next().setLightOn();
			}
		}
	}
}
