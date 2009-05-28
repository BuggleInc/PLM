package lessons.meta;

import java.lang.reflect.Method;

import jlm.core.Game;
import jlm.ui.WorldView;
import jlm.universe.World;

public class HanoiCreateWorldWorld extends World {
	/* If i'm the answer world, cheat and return the world state directly */
	boolean isAnswer=false;
	Integer[] slotA;
	Integer[] slotB;
	Integer[] slotC;
	/* if i'm not the answer, ask our servant guy written by the student */
	World servant;
	HanoiCreateWorldWorldView view;
	
	public HanoiCreateWorldWorld(String name, Integer[] slotA, Integer[] slotB, Integer[] slotC) {
		super(name);
		this.slotA = slotA;
		this.slotB = slotB;
		this.slotC = slotC;
	}

	public HanoiCreateWorldWorld(HanoiCreateWorldWorld w2) {
		super(w2);
	}
	@Override
	public void reset(World w) {
		HanoiCreateWorldWorld other = (HanoiCreateWorldWorld) w;
		slotA = new Integer[other.slotA.length];
		for (int i=0;i<slotA.length;i++)
			slotA[i] = other.slotA[i];
		slotB = new Integer[other.slotB.length];
		for (int i=0;i<slotB.length;i++)
			slotB[i] = other.slotB[i];
		slotC = new Integer[other.slotC.length];
		for (int i=0;i<slotC.length;i++)
			slotC[i] = other.slotC[i];
		super.reset(w);		
	}
	@Override
	public WorldView[] getView() {
		view = new HanoiCreateWorldWorldView(this);
		return new WorldView[] {view};
	}
	
	protected Integer[] values(int slot) {
		Integer[] answer = null;
		switch (slot) {
		case 0: answer=slotA; break;
		case 1: answer=slotB; break;
		case 2: answer=slotC; break;
		}
		if (isAnswer)
			return answer;
		
		Integer[] res = null;
		if (servant == null)
			return null;
		try {
			Method m = servant.getClass().getMethod("values", Integer.class);
			res = (Integer[]) m.invoke(servant, slot);
		} catch (NoSuchMethodException e) {
			System.err.println("Method values(Integer) not found in your World implementation. Did you mark it public?");
			((HanoiCreateWorld)Game.getInstance().getCurrentLesson().getCurrentExercise()).error = true;
		} catch (Exception e) {
			System.err.println("Cannot invoke the values() method of your World implementation");
			((HanoiCreateWorld)Game.getInstance().getCurrentLesson().getCurrentExercise()).error = true;
			e.printStackTrace();
		}
		if (res == null) {
			System.err.println("Your values() method returned null");
			((HanoiCreateWorld)Game.getInstance().getCurrentLesson().getCurrentExercise()).error = true;
		} else if (!res.equals(answer)) {
			System.err.println("Your values() method did not return the values provided during the instanciation");
			((HanoiCreateWorld)Game.getInstance().getCurrentLesson().getCurrentExercise()).error = true;			
		}
		return res;
	}
}
