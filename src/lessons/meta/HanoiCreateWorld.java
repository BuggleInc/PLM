package lessons.meta;

import java.util.List;

import jlm.core.Game;
import jlm.exception.JLMCompilerException;
import jlm.lesson.Lesson;
import jlm.universe.World;

public class HanoiCreateWorld extends MetaExercise {
	public HanoiCreateWorld(Lesson lesson) {
		super(lesson);
		HanoiMetaWorld w = new HanoiMetaWorld("hanoi",this,
					new Integer[] {1,2,3,4,5,6,7,8}, new Integer[0], new Integer[0]);
		setup(w);
	}
	@Override
	protected void setup(World[] ws) {
		worldDuplicate(ws);

		newSourceFromFile("HanoiWorld","src/lessons/meta/HanoiCreateWorldAnswer","java",
					"s/HanoiCreateWorldAnswer/HanoiWorld/;");

		/* compute answer */
		((HanoiMetaWorld)answerWorld[0]).isAnswer=true;
	}
	
	@Override
	public void run(List<Thread> runnerVect) {
		error = false;
		Thread runner = new Thread(new Runnable() {
			public void run() {
				try {
					compileAll(Game.getInstance().getOutputWriter());
				} catch (JLMCompilerException e) {
					System.err.println("Cannot compile your world implementation");
					error = true;
					e.printStackTrace();
					return;
				}
				HanoiMetaWorld w = (HanoiMetaWorld)currentWorld[0];
				w.setServant(compiledClasses.get(className("HanoiWorld")));
				w.view.repaint();
			}
		});
		
		runner.start();
		runnerVect.add(runner);
	}
}
