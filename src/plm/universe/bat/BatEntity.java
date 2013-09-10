package plm.universe.bat;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.lesson.ExecutionProgress;
import plm.universe.Entity;
import plm.universe.World;

public class BatEntity extends Entity {
	
	public BatEntity() {
		super();
	}
	
	public BatEntity(String name, World w) {
		super(name,w);
	}
	
	public BatEntity(BatEntity other) {
		super();
		copy(other);
	}

	@Override
	public Entity copy() {
		return new BatEntity(this);
	}
	
	
	@Override
	public void copy(Entity o) {
		super.copy(o);
	}
	

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BatEntity)) {
			return false;
		}
		return (super.equals(o));
	}
	
	@Override
	protected void run() {
		throw new RuntimeException ("I thought this method was useless. Please report.");
	}

	protected void run(BatTest t) {
		// To be overriden by child classes
	}
	
	@Override 
	public void runIt(ExecutionProgress progress) {
		ProgrammingLanguage pl = Game.getProgrammingLanguage();
		if (pl.equals(Game.JAVA) || pl.equals(Game.SCALA)) {
			for (BatTest t:((BatWorld) world).getTests())
				try {
					run(t);
				} catch (Exception e) {
					t.setResult(Game.i18n.tr("This test raised a {0} exception: {1}",e.getClass().getName(), e.getMessage()));
				}
		} else if (pl.equals(Game.PYTHON)) {
			ScriptEngine engine ;

			ScriptEngineManager manager = new ScriptEngineManager();       
			engine = manager.getEngineByName("python");
			if (engine==null) 
				throw new RuntimeException("Failed to start an interpreter for python");
			
			try {
				engine.eval(
						"import java.lang.System.err\n"+
						"def log(a):\n"+
						"  java.lang.System.err.print(\"%s: %s\" %(entity.getName(),a))\n");
				String script = getScript(Game.PYTHON);
				if (script == null)
					throw new RuntimeException("No script found for "+Game.getInstance().getCurrentLesson().getCurrentExercise());
				engine.eval(script);
			} catch (ScriptException e1) {
				progress.setCompilationError( e1.getCause().toString() );
				e1.printStackTrace();
			}									

			for (BatTest t:((BatWorld) getWorld()).getTests())
				try {
					engine.put("thetest",t);
					engine.eval("thetest.setResult("+t.getName()+")");
				} catch (Exception e) {
					t.setResult(Game.i18n.tr("This test raised a {0} exception: {1}",e.getClass().getName(), e.getMessage()));
				}
		} else {
			throw new RuntimeException("BatWorld was not ported to "+pl.getLang()+" yet.");
		}
		
	}

}
