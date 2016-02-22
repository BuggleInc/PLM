package unit.exercise.universe;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import plm.core.lang.LangBlockly;
import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.universe.World;

public class ExampleWorld extends World {

	private boolean objective;

	public ExampleWorld(String name) {
		super(name);
		objective = false;
	}

	public ExampleWorld(ExampleWorld w2) {
		this(w2.getName());
		reset(w2);
	}

	@Override
	public void reset(World iw) {
		ExampleWorld initialWorld = (ExampleWorld)iw;
		objective = initialWorld.objective;
		super.reset(initialWorld);
	}

	@Override
	public boolean equals(Object o){
		if (!(o instanceof ExampleWorld)) {
			return false;
		}
		ExampleWorld other = (ExampleWorld) o;
		if(this.objective != other.objective) {
			return false;
		}
		return super.equals(o);
	}

	@Override
	public String diffTo(World world) {
		ExampleWorld other = (ExampleWorld) world;
		String s = "No diff";
		if(this.objective != other.objective) {
			s = "Returned "+other.objective+" while "+objective+" was expected...";
		}
		return s;
	}

	public boolean getObjective() {
		return objective;
	}

	public void setObjective(boolean objective) {
		this.objective = objective;
	}
	
	@Override
	public void setupBindings(ProgrammingLanguage lang,ScriptEngine engine) throws ScriptException {
		if (lang instanceof LangPython || lang instanceof LangBlockly) {
			engine.put("w", this);
			engine.eval(
				"def setObjective(objective):\n"+
				"	entity.setObjective(objective)\n");
		}
	}
}
