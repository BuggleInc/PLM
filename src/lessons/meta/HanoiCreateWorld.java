package lessons.meta;

import java.lang.reflect.Constructor;
import java.util.List;

import jlm.core.Game;
import jlm.exception.JLMCompilerException;
import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.World;

public class HanoiCreateWorld extends ExerciseTemplated {
	boolean error = false;
	public HanoiCreateWorld(Lesson lesson) {
		super(lesson);
		HanoiCreateWorldWorld w = new HanoiCreateWorldWorld("hanoi",
					new Integer[] {8, 7, 6, 5, 4, 3, 2, 1}, new Integer[0], new Integer[0]);
		setup(w);
	}
	@Override
	protected void setup(World[] ws) {
		worldDuplicate(ws);

		newSourceFromFile("HanoiWorld","src/lessons/meta/HanoiCreateWorldAnswer","java",
					"s/HanoiCreateWorldAnswer/HanoiWorld/;");
		/*newSourceFromFile("HanoiView","src/lessons/meta/HanoiCreateWorldEntityView","java",
				"s/HanoiCreateWorldEntityWorld/HanoiWorld/;"+
				"s/HanoiCreateWorldEntityView/HanoiView/");*/

		/* compute answer */
		((HanoiCreateWorldWorld)answerWorld[0]).isAnswer=true;
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
				HanoiCreateWorldWorld w = (HanoiCreateWorldWorld)currentWorld[0];
				try {
					Constructor<Object> c = compiledClasses.get(className("HanoiWorld")).
					getConstructor(String.class,Integer[].class,Integer[].class,Integer[].class);			
					w.servant = (World) c.newInstance("",w.slotA,w.slotB,w.slotC);
				} catch (Exception e1) {
					System.err.println("Cannot instantiate your world implementation");
					error = true;
					e1.printStackTrace();
				}
				w.view.repaint();
			}
		});
		
		runner.start();
		runnerVect.add(runner);
	}
	
	@Override
	public boolean check() {
		return !error;
	}
}
