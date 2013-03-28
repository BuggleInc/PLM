package lessons.meta;

import java.util.List;

import jlm.core.model.Game;
import jlm.core.model.lesson.Lesson;
import jlm.core.model.lesson.NoSuchEntityException;
import jlm.core.ui.MainFrame;
import jlm.universe.World;

public class HanoiCreateWorld extends MetaExercise {
	public HanoiCreateWorld(Lesson lesson) {
		super(lesson);
		debug=true;
		
		HanoiMetaWorld[] w = new HanoiMetaWorld[3];
		w[0]=new HanoiMetaWorld("HanoiWorld({8,7,6,5,4,3,2,1},{},{})",this,
				new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0], new Integer[0]);
		w[1]=new HanoiMetaWorld("HanoiWorld({},{8,7,6,5,4,3,2,1},{})",this,
				new Integer[0], new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0]);
		w[2]=new HanoiMetaWorld("HanoiWorld({},{},{8,7,6,5,4,3,2,1})",this,
				new Integer[0], new Integer[0], new Integer[] {8,7,6,5,4,3,2,1});
		
		worldDuplicate(w);

		try {
			newSourceFromFile(Game.JAVA,"HanoiWorld","src/jlm/universe/hanoi/HanoiWorld","java");
			newSourceFromFile(Game.JAVA,"HanoiInvalidMove","src/jlm/universe/hanoi/HanoiInvalidMove","java");
		} catch (NoSuchEntityException e) {
			System.out.println("Cannot find my files. Please go fix your pathes and such");
			e.printStackTrace();
		}
		debug=true;

		/* compute answer */
		for (World it:answerWorld) 
			((HanoiMetaWorld)it).isAnswer=true;
	}
	
	@Override
	public void run(List<Thread> runnerVect) {
		for (World it:currentWorld) {
			HanoiMetaWorld w = (HanoiMetaWorld)it;
			/* Setup the intercepter */
			w.setServant(compiledClasses.get(className("HanoiWorld")));
			/* Ask for a (future) redraw */
			// asking to redraw the view is not enough, apparently. Ask full windows
			MainFrame.getInstance().repaint();
			/* leave some time to the system to actually draw the stuff */
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/* Access the values from the student's code, to get the intercepter check them */
			w.values(0);
			w.values(1);
			w.values(2);
		}
	}
}
