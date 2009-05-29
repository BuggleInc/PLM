package lessons.meta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import jlm.ui.WorldView;
import jlm.universe.World;
import universe.hanoi.HanoiWorld;
import universe.hanoi.HanoiWorldView;

public class HanoiMetaWorld extends HanoiWorld {
	/* If i'm the answer world, cheat and return use the implementation of my ancestor directly */
	boolean isAnswer=false;
	/* if i'm not the answer, ask our servant guy written by the student */
	World servant;
	
	/* We need to ask the view to redraw everything at some point of the testing process */
	WorldView view=null;
	/* We report errors to the exercise directly */
	MetaExercise exercise;
	
	public HanoiMetaWorld(String name, MetaExercise exo, Integer[] slotA, Integer[] slotB, Integer[] slotC) {
		super(name,slotA,slotB,slotC);
		exercise = exo;
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
		exercise = other.exercise;
		super.reset(w);		
	}
	@Override
	public WorldView[] getView() {
		if (view == null)
			view = new HanoiWorldView(this);
		return new WorldView[] {view};
	}

	/* helper function for testing */
	protected void setServant(Class<Object> servantClass) {
		try {
			Constructor<Object> c = servantClass.getConstructor(String.class,Integer[].class,Integer[].class,Integer[].class);			
			servant = (World) c.newInstance("",rawValues(0),rawValues(1),rawValues(2));
		} catch (Exception e) {
			error("Cannot instantiate your world implementation",e);
		}

	}
	private void error(String msg) {
		error(msg,null);
	}
	private void error(String msg, Exception e) {
		System.err.println(msg);
		exercise.error = true;
		if (e != null)
			e.printStackTrace();		
	}
	
	/* Intercepting all methods of interest */
	@Override
	protected Integer[] values(int slot) {
		Integer[] answer = rawValues(slot);
		Integer[] res = null;
		
		if (isAnswer)
			return answer;
		if (servant == null)
			return null;
		
		try {
			Method m = servant.getClass().getMethod("values", Integer.class);
			res = (Integer[]) m.invoke(servant, slot);
		} catch (NoSuchMethodException e) {
			error("Method values(Integer) not found in your World implementation. Did you mark it public?");
		} catch (Exception e) {
			error("Cannot invoke the values() method of your World implementation",e);
		}
		if (res == null) {
			error("Your values("+slot+") method returned null");
			return null;
		} 
		if (res.length != answer.length) {
			error("Your values("+slot+") method did not return the values provided during the instanciation");
			System.err.print("Provided: ");
			for (Integer i:answer)
				System.err.print(i+" ");
			System.err.println("(length:"+answer.length+")");
			System.err.print("Retrieved: ");
			for (Integer i:res)
				System.err.print(i+" ");
			System.err.println("(length:"+res.length+")");
		}
		for (int cpt=0;cpt<res.length;cpt++)
			if (res[cpt] != answer[cpt]) {
				error("Your values("+slot+") method did not return the values provided during the instanciation");
				System.err.print("Provided: ");
				for (Integer i:answer)
					System.err.print(i+" ");
				System.err.println("(length:"+answer.length+")");
				System.err.print("Retrieved: ");
				for (Integer i:res)
					System.err.print(i+" ");
				System.err.println("(length:"+res.length+")");
			}
		return res;
	}

	public Integer[] rawValues(int i) {
		return super.values(i);
	}
}
