package lessons.meta;

import java.lang.reflect.Constructor;

import jlm.core.ui.WorldView;
import jlm.universe.World;
import jlm.universe.hanoi.HanoiInvalidMove;
import jlm.universe.hanoi.HanoiWorld;
import jlm.universe.hanoi.HanoiWorldView;

public class HanoiMetaWorld extends HanoiWorld {
	/* If i'm the answer world, cheat and return use the implementation of my ancestor directly */
	boolean isAnswer=false;
	/* if i'm not the answer, ask our servant guy written by the student */
	World servant;
	
	/* We need to ask the view to redraw everything at some point of the testing process */
	WorldView view=null;
	/* We report errors to the exercise directly */
	MetaExercise exo;
	
	public HanoiMetaWorld(String name, MetaExercise exo, Integer[] slotA, Integer[] slotB, Integer[] slotC) {
		super(name,slotA,slotB,slotC);
		this.exo = exo;
	}

	public HanoiMetaWorld(HanoiMetaWorld w2) {
		super(w2);
	}
	@Override
	public void reset(World w) {
		HanoiMetaWorld other = (HanoiMetaWorld) w;
		isAnswer = other.isAnswer;
		servant = other.servant;
		view = other.view;
		exo = other.exo;
		super.reset(w);		
	}
	@Override
	public WorldView[] getView() {
		if (view == null)
			view = new HanoiWorldView(this);
		return new WorldView[] {view};
	}

	/* Set tested code in position */
	protected void setServant(Class<Object> servantClass) {
		try {
			Constructor<Object> c = servantClass.getConstructor(String.class,Integer[].class,Integer[].class,Integer[].class);
			System.out.println("New instance "+rawValues(0).length+" "+rawValues(1).length+" "+rawValues(2).length);
			servant = (World) c.newInstance("",rawValues(0),rawValues(1),rawValues(2));
		} catch (Exception e) {
			exo.error("Cannot instantiate your world implementation",e);
		}
	}
	
	/* Intercepting all methods of interest */
	public Integer[] rawValues(int i) {
		return super.values(i);
	}
	@Override
	public Integer[] values(Integer slot) {
		Integer[] answer = rawValues(slot);
		Integer[] res = null;
		
		if (isAnswer)
			return answer;
		if (servant == null)
			return null;

		/* get a result from the servant, if possible */
		try {
			res = (Integer[]) 
				servant.getClass().getMethod("values", Integer.class)
				.invoke(servant, slot);
		} catch (NoSuchMethodException e) {
			exo.error("Method values(Integer) not found in your World implementation. Did you mark it public?");
		} catch (Exception e) {
			exo.error("Cannot invoke the values() method of your World implementation",e);
		}
		/* Check that the servant returned the right value */
		if (res == null) {
			exo.error("Your values("+slot+") method returned null");
			return null;
		} 
		boolean match = true;
		if (res.length != answer.length) {
			match = false;
		} else {
			for (int cpt=0;cpt<res.length;cpt++)
				if (res[cpt] != answer[cpt]) {
					match = false;
				}
		}
		if (!match) {
			exo.error("Your values("+slot+") method did not return the right values");
			System.err.print("Right   : ");
			for (Integer i:answer)
				System.err.print(i+" ");
			System.err.println("(length:"+answer.length+")");
			System.err.print("Returned: ");
			for (Integer i:res)
				System.err.print(i+" ");
			System.err.println("(length:"+res.length+")");
		}
		return res;
	}
	@Override
	public void move(Integer src, Integer dest) throws HanoiInvalidMove {
		if (isAnswer) {
			super.move(src,dest);
			return;
		}
		if (servant == null)
			return;

		/* get a result from the servant, if possible */
		try {
			servant.getClass().getMethod("move", Integer.class, Integer.class)
			.invoke(servant, src,dest);
		} catch (NoSuchMethodException e) {
			exo.error("Method move(Integer,Integer) not found in your World implementation. Did you mark it public?");
		} catch (Exception e) {
			exo.error("Cannot invoke the move(Integer,Integer) method of your World implementation",e);
		}

	}

}
