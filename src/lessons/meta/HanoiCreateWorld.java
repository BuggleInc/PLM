package lessons.meta;

import java.util.List;

import jlm.lesson.Lesson;
import jlm.universe.World;

public class HanoiCreateWorld extends MetaExercise {
	public HanoiCreateWorld(Lesson lesson) {
		super(lesson);
		HanoiMetaWorld[] w = new HanoiMetaWorld[3];
		w[0]=new HanoiMetaWorld("HanoiWorld({1,2,3,4,5,6,7,8},{},{})",this,
				new Integer[] {1,2,3,4,5,6,7,8}, new Integer[0], new Integer[0]);
		w[1]=new HanoiMetaWorld("HanoiWorld({},{1,2,3,4,5,6,7,8},{})",this,
				new Integer[0], new Integer[] {1,2,3,4,5,6,7,8}, new Integer[0]);
		w[2]=new HanoiMetaWorld("HanoiWorld({},{},{1,2,3,4,5,6,7,8})",this,
				new Integer[0], new Integer[0], new Integer[] {1,2,3,4,5,6,7,8});
		
		worldDuplicate(w);

		newSourceFromFile("HanoiWorld","src/universe/hanoi/HanoiWorld","java");
		newSourceFromFile("HanoiInvalidMove","src/universe/hanoi/HanoiInvalidMove","java");
		getSourceFile("HanoiInvalidMove").setEditable(false);
		debug=true;

		/* compute answer */
		for (World it:answerWorld) 
			((HanoiMetaWorld)it).isAnswer=true;
	}
	
	@Override
	public void run(List<Thread> runnerVect) {
		for (World it:currentWorld) {
			HanoiMetaWorld w = (HanoiMetaWorld)it;
			w.setServant(compiledClasses.get(className("HanoiWorld")));
			w.getView()[0].repaint();
			w.values(0);
			w.values(1);
			w.values(2);
		}
	}
}
