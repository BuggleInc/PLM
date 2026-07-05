package lessons.welcome.loopdowhile;

import java.io.IOException;
import java.util.Arrays;

import lessons.turmites.helloturmite.HelloTurmiteEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class Poucet2 extends ExerciseTemplated {

	public Poucet2(Lesson lesson) throws IOException, BrokenWorldFileException {
		super(lesson);
		tabName = "Poucet";
		
		/* Create initial situation */
		World[] myWorlds = new World[] {
				BuggleWorld.newFromFile("lessons/welcome/loopdowhile/Poucet"),
				BuggleWorld.newFromFile("lessons/welcome/loopdowhile/Poucet3"),
		};
		for (World w: myWorlds)
			w.setDelay(50); // moving a bit faster than usual

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(Poucet2Entity::new)).toList().toArray(new World[0]);
		setup(myWorlds);
	}
}