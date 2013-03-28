package lessons.meta;

import java.util.List;

import jlm.core.model.Game;
import jlm.core.model.lesson.Lesson;
import jlm.core.model.lesson.NoSuchEntityException;
import jlm.universe.World;
import jlm.universe.hanoi.HanoiInvalidMove;

public class HanoiCreateEntity extends MetaExercise {
	public HanoiCreateEntity(Lesson lesson) {
		super(lesson);
		/* Setup the worlds */
		HanoiMetaWorld[] w = new HanoiMetaWorld[3];
		w[0]=new HanoiMetaWorld("HanoiWorld({8,7,6,5,4,3,2,1},{},{})",this,
				new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0], new Integer[0]);
		w[1]=new HanoiMetaWorld("HanoiWorld({},{8,7,6,5,4,3,2,1},{})",this,
				new Integer[0], new Integer[] {8,7,6,5,4,3,2,1}, new Integer[0]);
		w[2]=new HanoiMetaWorld("HanoiWorld({},{},{8,7,6,5,4,3,2,1})",this,
				new Integer[0], new Integer[0], new Integer[] {8,7,6,5,4,3,2,1});
		new HanoiMetaEntity("Entity",w[0],this);
		new HanoiMetaEntity("Entity",w[1],this);
		new HanoiMetaEntity("Entity",w[2],this);

		worldDuplicate(w);

		/* compute answer */
		System.out.println("Compute answers "+answerWorld.length);
		for (World it:answerWorld) { 
			((HanoiMetaWorld)it).isAnswer=true;
			solve((HanoiMetaWorld) it);
		}		
		System.out.println("Done computing answers");
		
		/* setup the source files */
//		newSourceAliased(Game.JAVA,"lessons.meta.Main","jlm.lessons.meta.HanoiCreateWorld","HanoiWorld");
		try {
			newSourceFromFile(Game.JAVA,"HanoiEntity","src/jlm/universe/hanoi/HanoiEntity",
			       "s/HanoiCreateEntityAnswer/HanoiEntity/;");
		} catch (NoSuchEntityException e) {
			System.out.println("Cannot find my files. Please go fix your paths and such");
			e.printStackTrace();
		}
	}

	protected void solve(HanoiMetaWorld w,int src, int dst, int height) throws HanoiInvalidMove {
		if (height == 8)
			System.out.println("Solve("+src+","+dst+")");
		if (height!=0) {
			int other=-1;
			if (src+dst==1) /* 0+1 */
				other=2;
			if (src+dst==2) /* 0+2 */
				other=1;
			if (src+dst==3) /* 1+2 */
				other=0;

			solve(w,src,other, height-1);
//			System.out.println("move("+src+","+dst+")");
			w.move(src,dst);
			solve(w,other,dst,height-1);
		}
	}
	protected void solve(HanoiMetaWorld w) {
		try {
			for (int slot=0;slot<3;slot++)
				if (w.getSlotSize(slot) != 0) {					
					solve(w, slot, (slot+1)%3, w.getSlotSize(slot));
					return; /* don't move the same slot again: in next iteration here, the slot is also full since we just filled it */ 
				}
		} catch (HanoiInvalidMove e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void run(List<Thread> runnerVect) {
		Thread runner = new Thread(new Runnable() {
			public void run() {
				for (World it:currentWorld) {
					HanoiMetaWorld w = (HanoiMetaWorld)it;
					System.out.println("Set Servant");
					w.setServant(compiledClasses.get(className("HanoiWorld")));
					((HanoiMetaEntity) w.getEntity(0)).setServant(compiledClasses.get(className("HanoiEntity")));
					w.doDelay();
					solve(w);
					w.getView()[0].repaint();
				}
			}
		});

		runner.start();
		runnerVect.add(runner);
	}
	
	@Override 
	public void runDemo(List<Thread> runnerVect) {
		Thread runner = new Thread(new Runnable() {
			public void run() {
				for (int i=0; i<initialWorld.length; i++) { 
					answerWorld[i].reset(initialWorld[i]);
					answerWorld[i].doDelay();
					((HanoiMetaWorld)answerWorld[i]).isAnswer = true;
					solve((HanoiMetaWorld) answerWorld[i]);
				}
			}
		});

		runner.start();
		runnerVect.add(runner);
		
	}
}
