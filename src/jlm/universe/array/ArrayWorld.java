package jlm.universe.array;

import java.util.Arrays;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.WorldView;
import jlm.universe.World;

public class ArrayWorld extends World {

	protected int[] values;

	public ArrayWorld(String name) {
		super(name);
	}

	public ArrayWorld(String name, int size) {
		super(name);
		setDelay(50);
		this.values = new int[size];
	}	
	
	public ArrayWorld(ArrayWorld anotherWorld) {
		super(anotherWorld);
		//reset(anotherWorld);
	}

	@Override
	public void reset(World w) {
		ArrayWorld anotherWorld = (ArrayWorld) w;
		this.values = new int[anotherWorld.values.length];
		for (int i=0; i<anotherWorld.values.length; i++ ) {
			this.values[i] = anotherWorld.values[i];
		}
		super.reset(anotherWorld);		
	}
	
	@Override
	public WorldView getView() {
		return new ArrayWorldView(this);
	}
	
	@Override
	public boolean equals(Object o) {
		if (! (o instanceof ArrayWorld)) {
			return false;
		}
		ArrayWorld anotherWorld = (ArrayWorld) o;
		return Arrays.equals(this.values, anotherWorld.values) && super.equals(o);
	}
	
	public int[] getValues() {
		return this.values;
	}
	
	public void setValues(int[] newValues) {
		this.values = newValues;
	}
	@Override
	public void setupBindings(ProgrammingLanguage lang,ScriptEngine engine) throws ScriptException {
		if (lang.equals(Game.PYTHON)) {
			engine.eval(
					"def getValues():\n"+
					"	return entity.getValues()\n"+
					"def setResult(i):\n"+
					"	return entity.setResult(i)\n"
					);
		} else {
			throw new RuntimeException("No binding of ArrayWorld for "+lang);
		}
	}

	@Override
	public String diffTo(World wrong) {
		return toString()+" is not "+wrong.toString();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		for (Integer i : values)
			sb.append(i+",");
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		
		return sb.toString();
	}
	
}
