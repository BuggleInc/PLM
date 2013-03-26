package jlm.universe.bat;

import java.util.List;

import jlm.core.model.lesson.ExerciseTemplatingEntity;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;

public abstract class BatExercise extends ExerciseTemplatingEntity {
	public static final boolean INVISIBLE = false;
	public static final boolean VISIBLE = true;
	
	public BatExercise(Lesson lesson) {
		super(lesson);
		entityName = getClass().getCanonicalName()+".Entity";
	}

	protected void setup(World[] ws) {
		if (ws.length > 1)
			throw new RuntimeException("Bat exercises must have at most one world");
		
		String entName="no name";
		for (World w : ws) {
			entName = w.getName();
			w.addEntity(new BatEntity());
		}
		
		super.setup(ws,entName,
				"import jlm.universe.bat.BatEntity; "+
		        "import jlm.universe.bat.BatWorld; "+
		        "import jlm.universe.bat.BatTest; "+
		        "import jlm.universe.World; "+
		        "public class "+entName+" extends BatEntity { ");
	}
	
	@Override
	protected void computeAnswer() {

		BatWorld answer = (BatWorld) answerWorld[0];
		BatWorld init = (BatWorld) initialWorld[0];
		BatWorld curr = (BatWorld) currentWorld[0];
		
		for (int i=0;i<answer.tests.size();i++) {
			BatTest currTest = answer.tests.get(i);
			currTest.objectiveTest = true;
			
			run(currTest);
			currTest.expected = currTest.result;
			run(currTest); // generate a new result so that expected and result are not the same objects
			init.tests.get(i).expected = currTest.result;
			run(currTest); // and clone the result again			
			curr.tests.get(i).expected = currTest.result;
			run(currTest); // and clone the result again			
		}
		
	}

	@Override
	public void runDemo(List<Thread> runnerVect){
		/* No demo in bat exercises */
	}

	@Override
	public void run(World w) {
		for (BatTest currTest: ((BatWorld) w).tests) 
			run(currTest);
	}
	public abstract void run(BatTest t);
}
